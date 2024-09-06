package com.example.schedule.presentation.addition

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.DependencyHolder
import com.example.schedule.domain.Lesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdditionViewModel : ViewModel() {
    private val repository = DependencyHolder.lessonRepository
    private val _titleState = MutableStateFlow<InputState>(InputState.Text())
    private val _venueState = MutableStateFlow<InputState>(InputState.Text())
    private val _teacherNameState = MutableStateFlow<InputState>(InputState.Text())
    private val _timeState = MutableStateFlow<TimeRangeState>(TimeRangeState.Initial)

    val titleState = _titleState.asStateFlow()
    val venueState = _venueState.asStateFlow()
    val teacherNameState = _teacherNameState.asStateFlow()
    val timeState = _timeState.asStateFlow()

    fun onTitleInput(editable: Editable?) {
        _titleState.updateText(editable)
    }

    fun onVenueInput(editable: Editable?) {
        _venueState.updateText(editable)
    }

    fun onTeacherNameInput(editable: Editable?) {
        _teacherNameState.updateText(editable)
    }

    fun onStartDatePicked(time: Long?) {

    }

    fun onStartTimePicked(hours: Int, minutes: Int) {

    }

    fun onEndDatePicked(time: Long?) {

    }

    fun onEndTimePicked(hours: Int, minutes: Int) {

    }

    fun addLesson() {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    private fun MutableStateFlow<InputState>.updateText(editable: Editable?) {
        update {
            val text = editable?.toString().orEmpty()
            InputState.Text(text)
        }
    }
}

sealed interface TimeRangeState {
    data object Initial : TimeRangeState

    data class Range(val start: Long?, val end: Long?) : TimeRangeState

    data object Error : TimeRangeState
}

sealed interface InputState {
    @JvmInline
    value class Text(val value: String = "") : InputState

    data object Error : InputState
}