package com.example.schedule.presentation

import com.example.schedule.domain.LessonType

fun LessonType.getTitle() = when (this) {
    LessonType.LECTURE -> "Лекция"
    LessonType.SEMINAR -> "Семинар"
    LessonType.LABORATORY -> "Лабораторная"
}

inline fun <reified T> Any?.takeAs(): T? {
    if (this !is T) {
        return null
    }

    return this
}