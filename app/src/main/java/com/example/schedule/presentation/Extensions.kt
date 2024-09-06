package com.example.schedule.presentation

import com.example.schedule.domain.LessonType

fun LessonType.getTitle() = when (this) {
    LessonType.LECTURE -> "Лекция"
    LessonType.SEMINAR -> "Семинар"
    LessonType.LABORATORY -> "Лабораторная"
}