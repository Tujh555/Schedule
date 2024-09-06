package com.example.schedule.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.Instant

class LessonDatabaseConverter {
    @TypeConverter
    fun fromInstant(time: Instant): Long = time.toEpochMilli()

    @TypeConverter
    fun toInstant(time: Long): Instant = Instant.ofEpochMilli(time)
}