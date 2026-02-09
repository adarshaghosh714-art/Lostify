package com.example.lostify.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostify.data.ItemType
import com.example.lostify.data.LostItemEntity
import com.example.lostify.data.LostItemRepository
import kotlinx.coroutines.launch

class AddItemViewModel(
    private val repository: LostItemRepository
) : ViewModel() {

    fun addItem(
        type: ItemType,
        title: String,
        location: String,
        description: String,
        contactNumber: String,
        email: String
    ) {
        val item = LostItemEntity(
            id = 0,
            type = type,
            title = title,
            location = location,
            description = description,
            timestamp = System.currentTimeMillis(),
            date = System.currentTimeMillis(),
            imageRes = null,
            contactNumber = contactNumber,
            email = email
        )

        viewModelScope.launch {
            repository.insertItem(item)
        }
    }
}
