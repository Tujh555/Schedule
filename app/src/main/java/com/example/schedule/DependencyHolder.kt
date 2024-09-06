package com.example.schedule

import android.content.Context
import com.example.schedule.data.LessonRepositoryImpl
import com.example.schedule.data.database.LessonDatabase
import com.example.schedule.domain.LessonRepository

object DependencyHolder {
    private var _lessonRepository: LessonRepositoryImpl? = null
    val lessonRepository: LessonRepository
        get() = requireNotNull(_lessonRepository) {
            "call init first"
        }

    fun init(context: Context) {
        if (_lessonRepository != null) {
            return
        }

        synchronized(this) {
            if (_lessonRepository != null) {
                return
            }

            val database = LessonDatabase.create(context)
            _lessonRepository = LessonRepositoryImpl(database.lessonDao)
        }
    }
}