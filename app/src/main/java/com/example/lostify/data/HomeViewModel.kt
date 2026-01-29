package com.example.lostify.data



import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.lostify.R
import com.example.lostify.data.ItemType
import com.example.lostify.data.LostItem

class HomeViewModel : ViewModel() {


    private val _items = mutableStateOf<List<LostItem>>(emptyList())


    val items: State<List<LostItem>> = _items

    init {
        loadDummyItems()
    }

    private fun loadDummyItems() {
        _items.value = listOf(
            LostItem(
                id = "1",
                title = "EarBuds",
                location = "College Canteen",
                date = "24 Jan 2026",
                imageRes = R.drawable.ic_lost1,
                type = ItemType.FOUND
            ),
            LostItem(
                id = "2",
                title = "iPhone 13",
                location = "Library",
                date = "23 Jan 2026",
                imageRes = R.drawable.ic_lost1,
                type = ItemType.LOST
            )
        )
    }
}
