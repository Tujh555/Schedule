package com.example.schedule.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [LessonEntity::class], version = 1, exportSchema = false)
@TypeConverters(LessonDatabaseConverter::class)
abstract class LessonDatabase : RoomDatabase() {
    abstract val lessonDao: LessonDao

    companion object {
        fun create(context: Context): LessonDatabase = Room
            .databaseBuilder(context, LessonDatabase::class.java, "LessonDatabase")
            .build()
    }
}