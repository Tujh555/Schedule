package com.example.schedule.presentation.days.schedule.pager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.LessonItemBinding
import com.example.schedule.domain.Lesson
import com.example.schedule.domain.LessonType
import com.example.schedule.presentation.days.schedule.TimeFormatter
import com.example.schedule.presentation.getTitle

class LessonAdapter : ListAdapter<Lesson, LessonViewHolder>(LessonDIffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LessonViewHolder(
            LessonItemBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

object LessonDIffCallback : DiffUtil.ItemCallback<Lesson>() {
    override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean =
        oldItem == newItem
}

class LessonViewHolder(
    private val binding: LessonItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(lesson: Lesson) {
        with(binding) {
            tvTime.text = buildString {
                append(TimeFormatter.formatAsDaytime(lesson.startAt))
                append("\n-\n")
                append(TimeFormatter.formatAsDaytime(lesson.endAt))
            }

            tvTitle.text = lesson.title
            tvSubtitle.text = buildString {
                append(lesson.type.getTitle())
                append(", ")
                append(lesson.venue)
            }
            tvTeacherName.text = lesson.teacherName
        }
    }
}