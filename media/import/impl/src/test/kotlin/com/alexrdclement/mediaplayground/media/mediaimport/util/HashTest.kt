package com.alexrdclement.mediaplayground.media.mediaimport.util

import kotlin.test.Test
import kotlin.test.assertEquals

class HashTest {

    @Test
    fun sha256_returnsKnownHash_forEmptyInput() {
        // SHA-256 of empty input is a well-known constant
        assertEquals(
            "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
            byteArrayOf().sha256(),
        )
    }

    @Test
    fun sha256_returnsSameHash_forSameInput() {
        val bytes = "hello".encodeToByteArray()
        assertEquals(bytes.sha256(), bytes.sha256())
    }

    @Test
    fun sha256_returnsDifferentHash_forDifferentInput() {
        val a = "hello".encodeToByteArray().sha256()
        val b = "world".encodeToByteArray().sha256()
        assert(a != b)
    }
}
