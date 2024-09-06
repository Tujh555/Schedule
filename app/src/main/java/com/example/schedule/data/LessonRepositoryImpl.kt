package com.example.schedule.data

import com.example.schedule.data.database.LessonDao
import com.example.schedule.data.database.LessonEntity
import com.example.schedule.data.database.toDomain
import com.example.schedule.domain.Lesson
import com.example.schedule.domain.LessonRepository
import com.example.schedule.domain.LessonType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class LessonRepositoryImpl(private val lessonDao: LessonDao): LessonRepository {
    override val lessons: Flow<List<Lesson>> = lessonDao
        .getAllLessons()
        .map { lessons -> lessons.map(LessonEntity::toDomain) }

    override suspend fun createLesson(
        title: String,
        startAt: Instant,
        endAt: Instant,
        type: LessonType,
        venue: String,
        teacherName: String
    ) {
        val lessonEntity = LessonEntity(
            title = title,
            startAt = startAt,
            endAt = endAt,
            type = type,
            venue = venue,
            teacherName = teacherName,
            id = 0
        )

        lessonDao.insertLesson(lessonEntity)
    }

    override suspend fun deleteLesson(id: Int) {
        lessonDao.deleteLesson(id)
    }

    override suspend fun getLessonsBetween(startAt: Instant, endAt: Instant): List<Lesson> =
        lessonDao
            .getLessonsBetween(startAt, endAt)
            .map(LessonEntity::toDomain)
}