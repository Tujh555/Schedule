package com.example.schedule.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.schedule.domain.Lesson
import com.example.schedule.domain.LessonType
import java.time.Instant

@Entity
data class LessonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "start_at")
    val startAt: Instant,
    @ColumnInfo(name = "end_at")
    val endAt: Instant,
    @ColumnInfo(name = "type")
    val type: LessonType,
    @ColumnInfo(name = "venue")
    val venue: String,
    @ColumnInfo(name = "teacher_name")
    val teacherName: String
)

fun LessonEntity.toDomain() = Lesson(
    id = id,
    title = title,
    startAt = startAt,
    endAt = endAt,
    type = type,
    venue = venue,
    teacherName = teacherName
)

fun Lesson.toDb() = LessonEntity(
    id = 0,
    title = title,
    startAt = startAt,
    endAt = endAt,
    type = type,
    venue = venue,
    teacherName = teacherName
)