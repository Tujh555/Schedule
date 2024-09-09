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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.schedule.R
import com.example.schedule.databinding.FragmentAdditionBinding
import com.example.schedule.domain.LessonType
import com.example.schedule.presentation.getTitle
import com.example.schedule.presentation.takeAs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWidthPercent(95)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setupLessonType()
        setupTitleTv()
        setupVenueTv()
        setupTeacherNameTv()
        initButtonChooseTime()

        binding.btnSave.setOnClickListener {
            val canAddLesson = viewModel.addLesson()
            if (canAddLesson) {
                dismiss()
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun setupLessonType() {
        binding.tvType.setAdapter(lessonTypesAdapter)

        binding.tvType.setOnItemClickListener { adapterView, _, i, _ ->
            adapterView
                .getItemAtPosition(i)
                .takeAs<String>()
                ?.let { selectedItem ->
                    val index = lessonTypes.indexOf(selectedItem)

                    if (index != -1) {
                        val selectedType = LessonType.entries[index]
                        viewModel.onLessonTypeSelected(selectedType)
                    }
                }
        }

        viewModel.lessonTypeState
            .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
            .onEach { typeResult ->
                binding.tvType.error = if (typeResult?.isFailure == true) {
                    "Выберите тип предмета"
                } else {
                    null
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun setupTeacherNameTv() {
        binding.tvTeacherName.addTextChangedListener(
            afterTextChanged = viewModel::onTeacherNameInput
        )
        viewModel.teacherNameState.observeInputStateFor(
            editText = binding.tvTeacherName,
            error = "Введите имя преподавателя"
        )
    }

    private fun setupVenueTv() {
        binding.tvVenue.addTextChangedListener(afterTextChanged = viewModel::onVenueInput)
        viewModel.venueState.observeInputStateFor(
            editText = binding.tvVenue,
            error = "Введите место"
        )
    }

    private fun setupTitleTv() {
        binding.tvTitle.addTextChangedListener(afterTextChanged = viewModel::onTitleInput)
        viewModel.titleState.observeInputStateFor(
            editText = binding.tvTitle,
            error = "Введите название"
        )
    }

    private fun Flow<InputState>.observeInputStateFor(
        editText: TextInputEditText,
        error: String
    ) {
        flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
            .onEach { inputState ->
                editText.error = when (inputState) {
                    InputState.Error -> error
                    is InputState.Text -> null
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun setupTvTime() {
        viewModel.timeState
            .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
            .onEach { timeState ->
                binding.tvTime.error = when (timeState) {
                    TimeState.EmptyRangeError -> "Выберите время"

                    TimeState.IncorrectBoundsError -> "Некорректное время"

                    is TimeState.Range -> null
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun initButtonChooseTime() {
        setupTvTime()
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
        val rect = resources.displayMetrics.run {
            Rect(0, 0, widthPixels, heightPixels)
        }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}