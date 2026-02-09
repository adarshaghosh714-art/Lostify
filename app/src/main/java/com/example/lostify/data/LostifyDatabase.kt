package com.example.lostify.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [LostItemEntity::class],
    version = 2,
    exportSchema = false
)
abstract class LostifyDatabase : RoomDatabase() {

    abstract fun lostItemDao(): LostItemDao

    companion object {
        @Volatile
        private var INSTANCE: LostifyDatabase? = null

        fun getDatabase(context: Context): LostifyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LostifyDatabase::class.java,
                    "lostify_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}