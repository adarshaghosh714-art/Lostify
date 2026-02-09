package com.example.lostify.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: LostItemRepository

    init {
        val dao = LostifyDatabase
            .getDatabase(application)
            .lostItemDao()

        repository = LostItemRepository(dao)
    }


    val items = repository.getAllItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun getItemById(itemId: Int) =
        repository.getItemById(itemId)
}