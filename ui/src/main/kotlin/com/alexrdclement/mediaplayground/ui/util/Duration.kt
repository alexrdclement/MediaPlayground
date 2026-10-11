package com.alexrdclement.mediaplayground.ui.util

import kotlin.time.Duration
import kotlin.time.DurationUnit

fun Duration.formatShort(minDurationUnit: DurationUnit = DurationUnit.SECONDS): String {
    val daysPart = this.inWholeDays
    val hoursPart = this.inWholeHours % 24
    val minutesPart = this.inWholeMinutes % 60
    val secondsPart = this.inWholeSeconds % 60

    return when {
        daysPart > 0 -> formatDaysPart(
            daysPart = daysPart,
            hoursPart = hoursPart,
            minDurationUnit = minDurationUnit,
        )
        hoursPart > 0 -> formatHoursPart(
            hoursPart = hoursPart,
            minutesPart = minutesPart,
            minDurationUnit = minDurationUnit,
        )
        minutesPart > 0 -> formatMinutesPart(
            minutesPart = minutesPart,
            secondsPart = secondsPart,
            minDurationUnit = minDurationUnit,
        )
        secondsPart > 0 -> formatSecondsPart(
            secondsPart = secondsPart,
            minDurationUnit = minDurationUnit,
        )
        else -> formatZero(minDurationUnit)
    }
}

private fun formatDaysPart(
    daysPart: Long,
    hoursPart: Long,
    minDurationUnit: DurationUnit,
): String = if (minDurationUnit == DurationUnit.DAYS) {
    daysPart.withUnit(DurationUnit.DAYS)
} else {
    "${daysPart.withUnit(DurationUnit.DAYS)} ${hoursPart.withUnit(DurationUnit.HOURS)}"
}

private fun formatHoursPart(
    hoursPart: Long,
    minutesPart: Long,
    minDurationUnit: DurationUnit,
): String = if (minDurationUnit == DurationUnit.HOURS) {
    hoursPart.withUnit(DurationUnit.HOURS)
} else {
    "${hoursPart.withUnit(DurationUnit.HOURS)} ${minutesPart.withUnit(DurationUnit.MINUTES)}"
}

private fun formatMinutesPart(
    minutesPart: Long,
    secondsPart: Long,
    minDurationUnit: DurationUnit,
): String = if (minDurationUnit == DurationUnit.MINUTES) {
    minutesPart.withUnit(DurationUnit.MINUTES)
} else {
    "$minutesPart:${secondsPart.toString().padStart(length = 2, padChar = '0')}"
}

private fun Duration.formatSecondsPart(
    secondsPart: Long,
    minDurationUnit: DurationUnit,
): String = if (minDurationUnit == DurationUnit.SECONDS) {
    secondsPart.withUnit(DurationUnit.SECONDS)
} else {
    when (minDurationUnit) {
        DurationUnit.MILLISECONDS -> inWholeMilliseconds.withUnit(DurationUnit.MILLISECONDS)
        DurationUnit.MICROSECONDS -> inWholeMicroseconds.withUnit(DurationUnit.MICROSECONDS)
        DurationUnit.NANOSECONDS -> inWholeNanoseconds.withUnit(DurationUnit.NANOSECONDS)
        else -> error("Unexpected minDurationUnit: $minDurationUnit")
    }
}

private fun formatZero(minDurationUnit: DurationUnit): String = 0L.withUnit(minDurationUnit)

private fun Long.withUnit(unit: DurationUnit): String {
    val suffix = when (unit) {
        DurationUnit.DAYS -> "d"
        DurationUnit.HOURS -> "h"
        DurationUnit.MINUTES -> "m"
        DurationUnit.SECONDS -> "s"
        DurationUnit.MILLISECONDS -> "ms"
        DurationUnit.MICROSECONDS -> "µs"
        DurationUnit.NANOSECONDS -> "ns"
    }
    return "$this$suffix"
}
