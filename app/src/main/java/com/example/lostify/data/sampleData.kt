package com.example.lostify.data



import com.example.lostify.R

val sampleItems = listOf(
    LostItem(
        id = 1,
        title = "Earbuds",
        location = "College Canteen",
        date = "24 Jan 2026",
        imageRes = R.drawable.ic_lost1,
        type = ItemType.LOST
    ),
    LostItem(
        id = 2,
        title = "Wallet",
        location = "Library",
        date = "23 Jan 2026",
        imageRes = R.drawable.ic_logo,
        type = ItemType.LOST
    )
)
