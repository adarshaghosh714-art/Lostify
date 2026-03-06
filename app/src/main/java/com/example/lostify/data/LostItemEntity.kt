package com.example.lostify.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lost_items")
data class LostItemEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var type: String = "",

    var title: String = "",
    var location: String = "",

    var description: String = "",

    var timestamp: Long = 0L,
    var date: Long = 0L,

    var imageUri: String = "",

    var contactNumber: String = "",
    var email: String = ""
)