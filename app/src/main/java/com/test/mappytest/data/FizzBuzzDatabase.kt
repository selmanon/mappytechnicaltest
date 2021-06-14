package com.test.mappytest.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.mappytest.data.entitites.RequestEntity

@Database(entities = [RequestEntity::class], version = 1)
abstract class FizzBuzzDatabase : RoomDatabase() {
    abstract fun requestDao(): RequestDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: FizzBuzzDatabase? = null

        fun getInstance(context: Context): FizzBuzzDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    )
                        .also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): FizzBuzzDatabase {
            return Room
                .databaseBuilder(context, FizzBuzzDatabase::class.java, "FizzBuzz-Database")
                .build()
        }
    }
}