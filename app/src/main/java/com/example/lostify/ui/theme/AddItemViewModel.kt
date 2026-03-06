
package com.example.lostify.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostify.data.FirebaseLostItem
import com.example.lostify.data.ItemType
import com.example.lostify.data.LostItem
import com.example.lostify.data.LostItemEntity
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
        imageUri: String?
    ) {

        // Prevent empty submissions
        if (title.isBlank() || location.isBlank()) return

        val newItem = FirebaseLostItem(
            id = "",
            type = type.name,
            title = title.trim(),
            location = location.trim(),
            description = description.trim(),
            timestamp = System.currentTimeMillis(),
            date = System.currentTimeMillis(),
            imageUri = imageUri,
            contactNumber = contactNumber.trim(),
            email = email.trim()
        )

        viewModelScope.launch {
            repository.addItem(newItem)
        }
    }
}

