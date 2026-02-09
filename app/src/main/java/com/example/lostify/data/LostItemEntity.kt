package com.example.lostify.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lost_items")
data class LostItemEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val type: ItemType,

    val title: String,
    val location: String,

    val description: String? = null,


    val timestamp: Long? = null,
    val date: Long,


    val imageRes: Int? = null,


    val contactNumber: String? = null,
    val email: String? = null
)
