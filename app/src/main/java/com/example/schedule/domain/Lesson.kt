package com.example.schedule.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class Lesson(
    val id: Int,
    val title: String,
    val startAt: Instant,
    val endAt: Instant,
    val type: LessonType,
    val venue: String,
    val teacherName: String
) : Parcelable

enum class LessonType {
    LECTURE, SEMINAR, LABORATORY
}