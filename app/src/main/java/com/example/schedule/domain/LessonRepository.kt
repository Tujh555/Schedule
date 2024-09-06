package com.example.schedule.domain

import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface LessonRepository {
    val lessons: Flow<List<Lesson>>

    suspend fun createLesson(lesson: Lesson)

    suspend fun deleteLesson(id: Int)

    suspend fun getLessonsBetween(startAt: Instant, endAt: Instant): List<Lesson>
}