package com.learner.secureprefs

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ByteArrayTest {

    @Test
    fun checkByteArray() {

        val key = "user_id"

        val bArr = key.toByteArray()
        println(bArr.size)

        assertEquals(key.length, bArr.size)
    }
}
