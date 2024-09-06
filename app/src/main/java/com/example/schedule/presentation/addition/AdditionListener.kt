package com.example.schedule.presentation.addition

import com.example.schedule.domain.LessonType
import java.time.Instant

interface AdditionListener {
    fun onAddFinished(
        title: String,
        startAt: Instant,
        endAt: Instant,
        type: LessonType,
        venue: String,
        teacherName: String
    )
}