package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.seconds

@Serializable
sealed class TimeUnit : Comparable<TimeUnit> {
    @Serializable
    data class Samples(val samples: Long, val sampleRate: Int) : TimeUnit() {
        operator fun unaryMinus(): Samples = Samples(
            samples = -samples,
            sampleRate = sampleRate,
        )
        operator fun plus(other: Samples): Samples = Samples(
            samples = samples + other.samples,
            sampleRate = sampleRate,
        )
        operator fun minus(other: Samples) = this + (-other)
    }
    @Serializable
    data class Frames(val frames: Long, val frameRate: Double) : TimeUnit() {
        operator fun unaryMinus(): Frames = Frames(
            frames = -frames,
            frameRate = frameRate,
        )
        operator fun plus(other: Frames): Frames = Frames(
            frames = frames + other.frames,
            frameRate = frameRate,
        )
        operator fun minus(other: Frames) = this + (-other)
    }

    override fun compareTo(other: TimeUnit): Int =
        toKotlinDuration().compareTo(other.toKotlinDuration())

}

fun TimeUnit.toKotlinDuration() = when (this) {
    is TimeUnit.Samples -> (samples.toDouble() / sampleRate).seconds
    is TimeUnit.Frames -> (frames / frameRate).seconds
}

operator fun TimeUnit.plus(other: TimeUnit): TimeUnit = when (this) {
    is TimeUnit.Samples if other is TimeUnit.Samples -> this + other
    is TimeUnit.Frames if other is TimeUnit.Frames -> this + other
    else -> error("Cannot add ${this::class.simpleName} to ${other::class.simpleName}")
}


