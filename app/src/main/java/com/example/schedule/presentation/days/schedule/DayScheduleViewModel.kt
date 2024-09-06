package com.example.schedule.presentation.days.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.DependencyHolder
import com.example.schedule.domain.Lesson
import com.example.schedule.domain.LessonType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch
import java.time.Instant

class DayScheduleViewModel : ViewModel() {
    private val repository = DependencyHolder.lessonRepository
    val groupedLessons: Flow<List<Pair<String, List<Lesson>>>> =
        repository.lessons
            .map { lessons ->
                lessons
                    .groupBy { lesson -> TimeFormatter.formatAsDaysWithMoths(lesson.startAt) }
                    .toList()
            }

    fun createLesson(
        title: String,
        startAt: Instant,
        endAt: Instant,
        type: LessonType,
        venue: String,
        teacherName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createLesson(
                title = title,
                startAt = startAt,
                endAt = endAt,
                type = type,
                venue = venue,
                teacherName = teacherName
            )
        }
    }
}