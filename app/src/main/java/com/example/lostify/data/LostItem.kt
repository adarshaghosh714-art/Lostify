package com.example.lostify.data



data class LostItem(
    val id:String,
    val title: String,
    val location: String,
    val type: ItemType,
    val date: String,
    val imageRes: Int
)
enum class ItemType{
    LOST,
    FOUND
}