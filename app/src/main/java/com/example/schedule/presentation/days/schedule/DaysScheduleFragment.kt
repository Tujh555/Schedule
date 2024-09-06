package com.example.schedule.presentation.days.schedule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.schedule.R
import com.example.schedule.databinding.FragmentDaysScheduleBinding
import com.example.schedule.presentation.addition.AdditionFragment
import com.example.schedule.presentation.days.schedule.pager.AlphaPageTransformer
import com.example.schedule.presentation.days.schedule.pager.LessonListAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DaysScheduleFragment : Fragment(R.layout.fragment_days_schedule) {
    private val binding by viewBinding(FragmentDaysScheduleBinding::bind)
    private val viewModel by viewModels<DayScheduleViewModel>()
    private val lessonListAdapter = LessonListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachTabMediator()
        observeLessonList()
        initAddButton()
    }

    private fun initAddButton() {
        binding.fabAdd.setOnClickListener {
            AdditionFragment().show(childFragmentManager, null)
        }
    }

    private fun attachTabMediator() {
        val pager = binding.pager

        pager.adapter = lessonListAdapter
        pager.setPageTransformer(AlphaPageTransformer())
        val mediator = TabLayoutMediator(binding.tabLayout, pager) { tab, positon ->
            val (currentTabText, _) = lessonListAdapter.currentList[positon]

            tab.text = currentTabText
        }

        mediator.attach()
    }

    private fun observeLessonList() {
        viewModel
            .groupedLessons
            .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach(lessonListAdapter::submitList)
            .launchIn(lifecycleScope)
    }
}