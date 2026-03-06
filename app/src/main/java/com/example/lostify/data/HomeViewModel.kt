package com.example.lostify.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LostItemViewModel : ViewModel() {

    private val repository = LostItemRepository()

    private val _items = MutableStateFlow<List<FirebaseLostItem>>(emptyList())
    val items: StateFlow<List<FirebaseLostItem>> = _items

    init {
        observeItems()
    }

    private fun observeItems() {

        repository.listenForItems { itemList ->
            _items.value = itemList
        }
    }

    fun addItem(item: FirebaseLostItem) {
        viewModelScope.launch {
            repository.addItem(item)
        }
    }

    fun getItemById(id: String): FirebaseLostItem? {
        return _items.value.find { it.id == id }
    }
}