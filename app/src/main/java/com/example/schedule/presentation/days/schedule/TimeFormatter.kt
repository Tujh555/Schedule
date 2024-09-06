package com.example.schedule.presentation.days.schedule

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeFormatter {
    private val daysMothsFormatter = provide("dd.MM")
    private val daytimeFormatter = provide("HH:mm")

    private fun provide(pattern: String): DateTimeFormatter = DateTimeFormatter
        .ofPattern(pattern)
        .withZone(ZoneId.systemDefault())

    fun formatAsDaysWithMoths(time: Instant): String = daysMothsFormatter.format(time)

    fun formatAsDaytime(time: Instant): String = daytimeFormatter.format(time)
}