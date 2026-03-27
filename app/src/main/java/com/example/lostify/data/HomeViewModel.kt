package com.example.lostify.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.net.Uri

class LostItemViewModel : ViewModel() {

    private val repository = LostItemRepository()

    private val _items = MutableStateFlow<List<FirebaseLostItem>>(emptyList())
    val items: StateFlow<List<FirebaseLostItem>> = _items


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        observeItems()
    }


    private fun observeItems() {
        repository.listenForItems { itemList ->
            _items.value = itemList
        }
    }


    fun addItem(
        type: String,
        title: String,
        location: String,
        description: String,
        contactNumber: String,
        email: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                repository.addItem(
                    type,
                    title,
                    location,
                    description,
                    contactNumber,
                    email,
                    imageUri
                )
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun getItemById(id: String): FirebaseLostItem? {
        return items.value.find { it.id == id }
    }

   
    fun deleteItem(item: FirebaseLostItem) {
        viewModelScope.launch {
            _error.value = null

            try {
                repository.deleteItem(item)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}