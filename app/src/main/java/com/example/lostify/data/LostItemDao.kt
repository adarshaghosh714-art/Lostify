package com.example.lostify.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LostItemDao {

    @Query("SELECT * FROM lost_items")
    fun getAllItems(): Flow<List<LostItemEntity>>


    @Query("SELECT * FROM lost_items WHERE id = :itemId")
    fun getItemById(itemId: Int): Flow<LostItemEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: LostItemEntity)
}