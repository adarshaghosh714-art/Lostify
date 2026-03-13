package com.example.lostify.data

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.io.InputStream

class LostItemRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val itemsCollection = firestore.collection("items")

    // Cloudinary config
    private val cloudName = "dsy9ukvha"
    private val uploadPreset = "Lostify"

    suspend fun addItem(
        type: String,
        title: String,
        location: String,
        description: String,
        contactNumber: String,
        email: String,
        imageUri: Uri?
    ) {

        val docRef = itemsCollection.document()

        var imageUrl: String? = null

       
        if (imageUri != null) {
            try {
                android.util.Log.d("CLOUDINARY_UPLOAD", "Uploading image...")
                imageUrl = uploadImageToCloudinary(imageUri)
                android.util.Log.d("CLOUDINARY_UPLOAD", "IMAGE URL RETURNED: $imageUrl")
            } catch (e: Exception) {
                android.util.Log.e("CLOUDINARY_UPLOAD", "UPLOAD FAILED", e)
                imageUrl = null
            }
        }

        val newItem = FirebaseLostItem(
            id = docRef.id,
            type = type,
            title = title,
            location = location,
            description = description,
            timestamp = System.currentTimeMillis(),
            date = System.currentTimeMillis(),
            imageUrl = imageUrl,
            contactNumber = contactNumber,
            email = email
        )

        docRef.set(newItem).await()
    }

    private suspend fun uploadImageToCloudinary(imageUri: Uri): String =
        withContext(Dispatchers.IO) {

            val context = AppContextHolder.context

            val inputStream: InputStream =
                context.contentResolver.openInputStream(imageUri)
                    ?: throw Exception("Failed to open image")

            val bytes = inputStream.readBytes()

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file",
                    "image.jpg",
                    bytes.toRequestBody("image/*".toMediaType())
                )
                .addFormDataPart("upload_preset", uploadPreset)
                .build()

            val request = Request.Builder()
                .url("https://api.cloudinary.com/v1_1/$cloudName/image/upload")
                .post(requestBody)
                .build()

            val client = OkHttpClient()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                throw Exception("Cloudinary upload failed: ${response.code}")
            }

            val responseString = response.body?.string() ?: ""

            android.util.Log.d("CLOUDINARY", responseString)

            val json = JSONObject(responseString)

            return@withContext json.getString("secure_url")
        }

    fun listenForItems(onItemsChanged: (List<FirebaseLostItem>) -> Unit) {

        itemsCollection.addSnapshotListener { snapshot, error ->

            if (error != null) {
                error.printStackTrace()
                return@addSnapshotListener
            }

            val items = snapshot?.documents?.mapNotNull { document ->

                val item = document.toObject(FirebaseLostItem::class.java)

                item?.copy(id = document.id)

            } ?: emptyList()

            onItemsChanged(items)
        }
    }
}