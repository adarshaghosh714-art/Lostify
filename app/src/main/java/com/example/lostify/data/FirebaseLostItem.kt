package com.example.lostify.data

data class FirebaseLostItem(
    val id: String = "",
    val type: String = "",
    val title: String = "",
    val location: String = "",
    val description: String = "",
    val timestamp: Long = 0L,
    val date: Long = 0L,
    val contactNumber: String = "",
    val email: String = "",
    val imageUri: String? = null
)