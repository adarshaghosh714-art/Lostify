package com.example.lostify.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
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

    private val auth = FirebaseAuth.getInstance()

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

        val currentUser = auth.currentUser
            ?: throw Exception("User not logged in")

        val docRef = itemsCollection.document()

        var imageUrl: String? = null

        if (imageUri != null) {
            try {
                imageUrl = uploadImageToCloudinary(imageUri)
            } catch (e: Exception) {
                e.printStackTrace()
                imageUrl = null
            }
        }

        val newItem = FirebaseLostItem(
            id = docRef.id,
            userId = currentUser.uid,
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


    suspend fun deleteItem(item: FirebaseLostItem) {

        val currentUser = auth.currentUser
            ?: throw Exception("User not logged in")

        if (currentUser.uid != item.userId) {
            throw Exception("You are not allowed to delete this item")
        }

        itemsCollection
            .document(item.id)
            .delete()
            .await()
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

            val json = JSONObject(responseString)

            return@withContext json.getString("secure_url")
        }
}