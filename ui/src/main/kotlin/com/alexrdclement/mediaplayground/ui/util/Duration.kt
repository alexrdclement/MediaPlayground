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


private const val DaySuffix = "d"
private const val HourSuffix = "h"
private const val MinuteSuffix = "m"
private const val SecondSuffix = "s"
private const val MilliSuffix = "ms"
private const val MicroSuffix = "µs"
private const val NanoSuffix = "ns"

private fun Long.formatDays(): String = this.formatWithSuffix(DaySuffix)
private fun Long.formatHours(): String = this.formatWithSuffix(HourSuffix)
private fun Long.formatMinutes(): String = this.formatWithSuffix(MinuteSuffix)
private fun Long.formatSeconds(): String = this.formatWithSuffix(SecondSuffix)
private fun Long.formatMilliseconds(): String = this.formatWithSuffix(MilliSuffix)
private fun Long.formatMicroseconds(): String = this.formatWithSuffix(MicroSuffix)
private fun Long.formatNanoseconds(): String = this.formatWithSuffix(NanoSuffix)

private fun Long.formatWithSuffix(suffix: String) = "$this$suffix"
