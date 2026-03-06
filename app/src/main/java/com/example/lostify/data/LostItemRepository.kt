package com.example.lostify.data

import com.google.firebase.firestore.FirebaseFirestore

class LostItemRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val itemsCollection = firestore.collection("items")

    // Add item to Firestore
    suspend fun addItem(item: FirebaseLostItem) {
        itemsCollection.add(item)
    }

    // Listen for realtime updates
    fun listenForItems(onItemsChanged: (List<FirebaseLostItem>) -> Unit) {

        itemsCollection.addSnapshotListener { snapshot, error ->

            if (error != null) {
                error.printStackTrace()
                return@addSnapshotListener
            }

            val items = snapshot?.documents?.mapNotNull { document ->

                val item = document.toObject(FirebaseLostItem::class.java)

                item?.copy(
                    id = document.id
                )
            } ?: emptyList()

            onItemsChanged(items)
        }
    }
}