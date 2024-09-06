package com.example.schedule.presentation.days.schedule.pager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.LessonsListItemBinding
import com.example.schedule.domain.Lesson

class LessonListAdapter : ListAdapter<Pair<String, List<Lesson>>, LessonsListViewHolder>(LessonListDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LessonsListViewHolder(
            LessonsListItemBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LessonsListViewHolder, position: Int) {
        holder.bind(getItem(position).second)
    }
}

object LessonListDiffCallback : DiffUtil.ItemCallback<Pair<String, List<Lesson>>>() {
    override fun areItemsTheSame(
        oldItem: Pair<String, List<Lesson>>,
        newItem: Pair<String, List<Lesson>>
    ): Boolean = oldItem.first == newItem.first && oldItem.second === newItem.second

    override fun areContentsTheSame(
        oldItem: Pair<String, List<Lesson>>,
        newItem: Pair<String, List<Lesson>>
    ): Boolean = oldItem.first == newItem.first && equals(oldItem.second, newItem.second)

    private fun equals(oldItem: List<Lesson>, newItem: List<Lesson>): Boolean {
        if (oldItem.size != newItem.size) return false

        for (i in oldItem.indices) {
            val oldItemElement = oldItem[i]
            val newItemElement = newItem[i]

            if (oldItemElement != newItemElement) {
                return false
            }
        }

        return true
    }
}

class LessonsListViewHolder(
    private val binding: LessonsListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(list: List<Lesson>) {
        val adapter = LessonAdapter()
        binding.root.adapter = adapter
        adapter.submitList(list)
    }
}

