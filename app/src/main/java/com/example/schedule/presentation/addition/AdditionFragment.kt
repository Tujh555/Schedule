package com.example.schedule.presentation.addition

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.schedule.R
import com.example.schedule.databinding.FragmentAdditionBinding
import com.example.schedule.domain.LessonType
import com.example.schedule.presentation.getTitle
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class AdditionFragment : DialogFragment(R.layout.fragment_addition) {
    private val binding by viewBinding(FragmentAdditionBinding::bind)
    private val viewModel by viewModels<AdditionViewModel>()
    private val lessonTypes = LessonType.entries.map(LessonType::getTitle)
    private val lessonTypesAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_expandable_list_item_1,
            lessonTypes
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setWidthPercent(95)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.tvType.setAdapter(lessonTypesAdapter)

        binding.tvTitle.addTextChangedListener { text -> viewModel.onTitleInput(text) }
        binding.tvVenue.addTextChangedListener { text -> viewModel.onVenueInput(text) }
        binding.tvTeacherName.addTextChangedListener { viewModel.onTeacherNameInput(it) }

        binding.btnSave.setOnClickListener {
            viewModel.addLesson()
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        initButtonChooseTime()
    }

    private fun initButtonChooseTime() {
        binding.btnChooseTime.setOnClickListener {
            showDatePicker("Выберите дату начала") {
                viewModel.onStartDatePicked(it)
                showStartTimePicker()
            }
        }
    }

    private fun showStartTimePicker() {
        showTimePicker("Выберите время начала") { hour, minute ->
            viewModel.onStartTimePicked(hour, minute)
            showEndDatePicker()
        }
    }

    private fun showEndDatePicker() {
        showDatePicker("Выберите дату конца") {
            viewModel.onEndDatePicked(it)
            showEndTimePicker()
        }
    }

    private fun showEndTimePicker() {
        showTimePicker(
            title = "Выберите время конца",
            onAccept = viewModel::onEndTimePicked
        )
    }

    private fun showDatePicker(title: String, onAccept: (Long?) -> Unit) {
        val picker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(title)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        picker.addOnPositiveButtonClickListener(onAccept)

        picker.show(parentFragmentManager, "")
    }

    private fun showTimePicker(title: String, onAccept: (Int, Int) -> Unit) {
        val picker = MaterialTimePicker
            .Builder()
            .setTitleText(title)
            .build()

        picker.addOnPositiveButtonClickListener {
            onAccept(picker.hour, picker.minute)
        }

        picker.show(parentFragmentManager, "")
    }

    private fun setWidthPercent(percentage: Int) {
        val percent = percentage.toFloat() / 100
        val dm = resources.displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}