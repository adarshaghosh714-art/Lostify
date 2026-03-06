package com.example.lostify.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lostify.data.LostItemViewModel

class HomeViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LostItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LostItemViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}