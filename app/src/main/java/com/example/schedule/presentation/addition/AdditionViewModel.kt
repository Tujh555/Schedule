package com.example.schedule.presentation.addition

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.DependencyHolder
import com.example.schedule.domain.Lesson
import com.example.schedule.domain.LessonType
import com.example.schedule.presentation.takeAs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

class AdditionViewModel : ViewModel() {
    private val repository = DependencyHolder.lessonRepository
    private val _titleState = MutableStateFlow<InputState>(InputState.Text())
    private val _venueState = MutableStateFlow<InputState>(InputState.Text())
    private val _teacherNameState = MutableStateFlow<InputState>(InputState.Text())
    private val _timeState = MutableStateFlow<TimeState>(TimeState.Range(null, null))
    private val _lessonTypeState = MutableStateFlow<Result<LessonType>?>(null)

    private val isFieldsValid: Boolean
        get() {
            val title = titleState.value
            val venue = venueState.value
            val teacher = teacherNameState.value
            val time = timeState.value
            val type = lessonTypeState.value

            val inputsValid = listOf(title, venue, teacher).all { it is InputState.Text }
            val timeValid = time is TimeState.Range
            val typeValid = type?.isSuccess == true

            return inputsValid && timeValid && typeValid
        }

    val titleState = _titleState.asStateFlow()
    val venueState = _venueState.asStateFlow()
    val teacherNameState = _teacherNameState.asStateFlow()
    val timeState = _timeState.asStateFlow()
    val lessonTypeState = _lessonTypeState.asStateFlow()

    fun onLessonTypeSelected(type: LessonType) {
        _lessonTypeState.update { Result.success(type) }
    }

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
        _timeState.update { state ->
            when (state) {
                TimeState.EmptyRangeError,
                TimeState.IncorrectBoundsError ->
                    TimeState.Range(
                        start = time?.let(Instant::ofEpochMilli),
                        end = null
                    )

                is TimeState.Range ->
                    state.copy(start = time?.let(Instant::ofEpochMilli))
            }
        }
    }

    fun onStartTimePicked(hours: Int, minutes: Int) {
        val time = hours.hourAsMillis() + minutes.minuteAsMillis()
        _timeState.update { state ->
            when (state) {
                TimeState.EmptyRangeError,
                TimeState.IncorrectBoundsError ->
                    TimeState.Range(
                        start = Instant.now().plusMillis(time),
                        end = null
                    )

                is TimeState.Range ->
                    state.copy(start = (state.start ?: Instant.now()).plusMillis(time))
            }
        }
    }

    fun onEndDatePicked(time: Long?) {
        _timeState.update { state ->
            when (state) {
                TimeState.EmptyRangeError,
                TimeState.IncorrectBoundsError ->
                    TimeState.Range(
                        start = null,
                        end = time?.let(Instant::ofEpochMilli)
                    )

                is TimeState.Range ->
                    state.copy(end = time?.let(Instant::ofEpochMilli))
            }
        }
    }

    fun onEndTimePicked(hours: Int, minutes: Int) {
        val time = hours.hourAsMillis() + minutes.minuteAsMillis()
        _timeState.update { state ->
            when (state) {
                TimeState.EmptyRangeError,
                TimeState.IncorrectBoundsError ->
                    TimeState.Range(
                        start = null,
                        end = Instant.now().plusMillis(time)
                    )

                is TimeState.Range ->
                    state.copy(end = (state.end ?: Instant.now()).plusMillis(time))
            }
        }
    }

    fun addLesson(): Boolean {
        validateFields()

        if (isFieldsValid.not()) {
            return false
        }
        val time = timeState.value.takeAs<TimeState.Range>() ?: return false

        val startAt = time.start ?: Instant.now()
        val endAt = time.end ?: Instant.now()
        val lesson = Lesson(
            id = 0,
            title = titleState.value.takeAs<InputState.Text>()?.value ?: return false,
            startAt = startAt,
            endAt = endAt,
            type = lessonTypeState.value?.getOrNull() ?: return false,
            venue = venueState.value.takeAs<InputState.Text>()?.value ?: return false,
            teacherName = teacherNameState.value.takeAs<InputState.Text>()?.value ?: return false
        )

        viewModelScope.launch(Dispatchers.IO) {
            repository.createLesson(lesson)
        }

        return true
    }

    private fun validateFields() {
        listOf(_titleState, _venueState, _teacherNameState).forEach { inputState ->
            val currentState = inputState.value
            if (currentState is InputState.Text && currentState.value.isEmpty()) {
                inputState.update { InputState.Error }
            }
        }

        val timeState = _timeState.value

        if (timeState is TimeState.Range) {
            val (start, end) = timeState

            if (start == null || end == null) {
                _timeState.update { TimeState.EmptyRangeError }
            }

            if ((end?.toEpochMilli() ?: 0L) < (start?.toEpochMilli() ?: 0L)) {
                _timeState.update { TimeState.IncorrectBoundsError }
            }
        }

        val lessonType = _lessonTypeState.value

        if (lessonType?.getOrNull() == null) {
            _lessonTypeState.update { Result.failure(IllegalStateException()) }
        }
    }

    private fun MutableStateFlow<InputState>.updateText(editable: Editable?) {
        update {
            val text = editable?.toString().orEmpty()
            InputState.Text(text)
        }
    }

    private fun Int.hourAsMillis() = toLong() * 3600000

    private fun Int.minuteAsMillis() = toLong() * 60000
}

sealed interface TimeState {
    data object EmptyRangeError : TimeState

    data object IncorrectBoundsError : TimeState

    data class Range(val start: Instant?, val end: Instant?) : TimeState
}

sealed interface InputState {
    @JvmInline
    value class Text(val value: String = "") : InputState

    data object Error : InputState
}