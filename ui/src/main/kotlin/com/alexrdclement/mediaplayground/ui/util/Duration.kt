package com.alexrdclement.mediaplayground.ui.util

import kotlin.time.Duration
import kotlin.time.DurationUnit

fun Duration.formatShort(
    minDurationUnit: DurationUnit = DurationUnit.SECONDS,
): String {
    val daysPart = this.inWholeDays
    val hoursPart = this.inWholeHours % 24
    val minutesPart = this.inWholeMinutes % 60
    val secondsPart = this.inWholeSeconds % 60

    return when {
        daysPart > 0 -> if (minDurationUnit == DurationUnit.DAYS) {
            daysPart.formatDays()
        } else {
            "${daysPart.formatDays()} ${hoursPart.formatHours()}"
        }
        hoursPart > 0 -> if (minDurationUnit == DurationUnit.HOURS) {
            hoursPart.formatHours()
        } else {
            "${hoursPart.formatHours()} ${minutesPart.formatMinutes()}"
        }
        minutesPart > 0 -> if (minDurationUnit == DurationUnit.MINUTES) {
            minutesPart.formatMinutes()
        } else {
            "${minutesPart}:${secondsPart.toString().padStart(2, '0')}"
        }
        secondsPart > 0 -> if (minDurationUnit == DurationUnit.SECONDS) {
            secondsPart.formatSeconds()
        } else {
            when (minDurationUnit) {
                DurationUnit.MILLISECONDS -> inWholeMilliseconds.formatMilliseconds()
                DurationUnit.MICROSECONDS -> inWholeMicroseconds.formatMicroseconds()
                DurationUnit.NANOSECONDS -> inWholeNanoseconds.formatNanoseconds()
                else -> throw IllegalStateException("Unexpected minDurationUnit: $minDurationUnit")
            }
        }
        else -> with(0L) {
            when (minDurationUnit) {
                DurationUnit.DAYS -> formatDays()
                DurationUnit.HOURS -> formatHours()
                DurationUnit.MINUTES -> formatMinutes()
                DurationUnit.SECONDS -> formatSeconds()
                DurationUnit.MILLISECONDS -> formatMilliseconds()
                DurationUnit.MICROSECONDS -> formatMicroseconds()
                DurationUnit.NANOSECONDS -> formatNanoseconds()
            }
        }
    }
}


private const val daySuffix = "d"
private const val hourSuffix = "h"
private const val minuteSuffix = "m"
private const val secondSuffix = "s"
private const val milliSuffix = "ms"
private const val microSuffix = "µs"
private const val nanoSuffix = "ns"

private fun Long.formatDays(): String = this.formatWithSuffix(daySuffix)
private fun Long.formatHours(): String = this.formatWithSuffix(hourSuffix)
private fun Long.formatMinutes(): String = this.formatWithSuffix(minuteSuffix)
private fun Long.formatSeconds(): String = this.formatWithSuffix(secondSuffix)
private fun Long.formatMilliseconds(): String = this.formatWithSuffix(milliSuffix)
private fun Long.formatMicroseconds(): String = this.formatWithSuffix(microSuffix)
private fun Long.formatNanoseconds(): String = this.formatWithSuffix(nanoSuffix)

private fun Long.formatWithSuffix(suffix: String) = "$this$suffix"
