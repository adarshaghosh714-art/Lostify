package com.example.lostify.data

import kotlinx.coroutines.flow.Flow

class LostItemRepository (
    private val dao : LostItemDao
){
    suspend fun insertItem(item : LostItemEntity){
        dao.insertItem(item)
    }
    fun getAllItems() = dao.getAllItems()

    fun getItemById(itemId: Int): Flow<LostItemEntity?> {
        return dao.getItemById(itemId)
    }
}