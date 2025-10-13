package com.alexrdclement.mediaplayground.data.audio.util

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.selects.select

suspend fun <T> Collection<Deferred<T>>.awaitFirst(
    predicate: (T) -> Boolean,
): T? {
    val remaining = this.toMutableList()
    while (remaining.isNotEmpty()) {
        val result = select {
            for (deferred in remaining) {
                deferred.onAwait { value ->
                    if (predicate(value)) value else null
                }
            }
        }
        if (result != null) return result
        remaining.removeAll { it.isCompleted }
    }
    return null
}
