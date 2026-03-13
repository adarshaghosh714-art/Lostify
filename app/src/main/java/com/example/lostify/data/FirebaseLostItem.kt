package com.example.lostify.data

data class FirebaseLostItem(
    val id: String = "",
    val type: String = "",
    val title: String = "",
    val location: String = "",
    val description: String = "",
    val timestamp: Long = 0,
    val date: Long = 0,
    val imageUrl: String? = null,
    val contactNumber: String = "",
    val email: String = ""
)