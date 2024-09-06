package com.example.schedule.domain

import java.time.Instant

data class Lesson(
    val id: Int,
    val title: String,
    val startAt: Instant,
    val endAt: Instant,
    val type: LessonType,
    val venue: String,
    val teacherName: String
)

enum class LessonType {
    LECTURE, SEMINAR, LABORATORY
}