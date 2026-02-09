package com.example.lostify.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "lost_items")
data class LostItem(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
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