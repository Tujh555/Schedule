package com.example.schedule.presentation.addition

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.schedule.R
import com.example.schedule.databinding.FragmentAdditionBinding
import com.example.schedule.domain.LessonType
import com.example.schedule.presentation.getTitle
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class AdditionFragment : DialogFragment(R.layout.fragment_addition) {
    private val binding by viewBinding(FragmentAdditionBinding::bind)
    private var additionListener: AdditionListener? = null
    private val lessonTypes = LessonType.entries.map(LessonType::getTitle)
    private val lessonTypesAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_expandable_list_item_1,
            lessonTypes
        )
    }
    private var dateStart: Long? = null
    private var dateEnd: Long? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setWidthPercent(95)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let { bundle ->
            dateStart = bundle.getLong(START_KEY)
            dateEnd = bundle.getLong(END_KEY)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        additionListener = parentFragment as? AdditionListener
        binding.tvType.setAdapter(lessonTypesAdapter)

        updateTimeText()

        binding.btnSave.setOnClickListener {
            // TODO накрутить валидацию
            additionListener?.let {
                it.onAddFinished(
                    title = binding.tvTitle.text?.toString().orEmpty(),
                    startAt = Instant.ofEpochMilli(dateStart ?: 0),
                    endAt = Instant.ofEpochMilli(dateEnd ?: 0),
                    type = LessonType.LECTURE,
                    venue = binding.tvVenue.text?.toString().orEmpty(),
                    teacherName = binding.tvTeacherName.text?.toString().orEmpty()
                )
            }
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnChooseTime.setOnClickListener {
            val startDatePicker =
                MaterialDatePicker.Builder
                    .datePicker()
                    .setTitleText("Выберите дату начала")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            startDatePicker.addOnPositiveButtonClickListener {
                var startDate = startDatePicker.selection

                val startTimePicker = MaterialTimePicker.Builder()
                    .setTitleText("Выберите время начала")
                    .build()

                startTimePicker.addOnPositiveButtonClickListener {
                    val startTimeMillis = startTimePicker.hour * 3_600_000 + startTimePicker.minute * 60000

                    startDate = (startDate ?: 0) + startTimeMillis

                    val endDatePicker =
                        MaterialDatePicker.Builder
                            .datePicker()
                            .setTitleText("Выберите дату конца")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .build()

                    endDatePicker.addOnPositiveButtonClickListener {
                        var endDate = endDatePicker.selection

                        MaterialTimePicker.Builder()
                            .setTitleText("Выберите время конца")
                            .build()
                            .apply {
                                addOnPositiveButtonClickListener {
                                    val endTimeMillis =
                                        startTimePicker.hour * 3_600_000 + startTimePicker.minute * 60000

                                    endDate = (endDate ?: 0) + endTimeMillis

                                    dateStart = startDate
                                    dateEnd = endDate
                                    updateTimeText()
                                }
                            }
                            .show(parentFragmentManager, "end time")
                    }

                    endDatePicker.show(parentFragmentManager, "end date")
                }

                startTimePicker.show(parentFragmentManager, "start time")
            }

            startDatePicker.show(parentFragmentManager, "start date")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        dateStart?.let { start ->
            dateEnd?.let { end ->
                outState.putLong(START_KEY, start)
                outState.putLong(END_KEY, end)
            }
        }
    }

    private fun updateTimeText() {
        if (dateStart == null || dateEnd == null) {
            binding.tvTime.setText("Выберите время")
            return
        }

        val newText = buildString {
            val start = Instant.ofEpochMilli(dateStart ?: 0)
            val end = Instant.ofEpochMilli(dateEnd ?: 0)

            val formatter = DateTimeFormatter
                .ofPattern("MM/dd/yyyy 'at' hh:mm a")
                .withZone(ZoneId.systemDefault())

            append("С ")
            append(formatter.format(start))
            append(" ПО")
            append(formatter.format(end))
        }
        binding.tvTime.setText(newText)
    }

    private fun setWidthPercent(percentage: Int) {
        val percent = percentage.toFloat() / 100
        val dm = resources.displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        private const val END_KEY = "end"
        private const val START_KEY = "start"
    }
}