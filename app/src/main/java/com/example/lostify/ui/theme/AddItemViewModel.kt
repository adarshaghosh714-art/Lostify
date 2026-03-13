package com.example.lostify.ui.theme

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostify.data.ItemType
import com.example.lostify.data.LostItemRepository
import kotlinx.coroutines.launch

class AddItemViewModel : ViewModel() {

    private val repository = LostItemRepository()

    fun addItem(
        type: ItemType,
        title: String,
        location: String,
        description: String,
        contactNumber: String,
        email: String,
        imageUri: Uri?,
        onComplete: () -> Unit
    ) {


        if (title.isBlank() || location.isBlank()) return

        viewModelScope.launch {

            repository.addItem(
                type = type.name,
                title = title.trim(),
                location = location.trim(),
                description = description.trim(),
                contactNumber = contactNumber.trim(),
                email = email.trim(),
                imageUri = imageUri
            )


            onComplete()
        }
    }
}