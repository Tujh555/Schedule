package com.example.schedule.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface LessonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lessonEntity: LessonEntity)

    @Query("DELETE FROM LessonEntity WHERE id = :id")
    suspend fun deleteLesson(id: Int)

    @Query("SELECT * FROM LessonEntity")
    fun getAllLessons(): Flow<List<LessonEntity>>

    @Query("SELECT * FROM LessonEntity WHERE start_at >= :start AND start_at <= :end")
    fun getLessonsBetween(start: Instant, end: Instant): List<LessonEntity>
}