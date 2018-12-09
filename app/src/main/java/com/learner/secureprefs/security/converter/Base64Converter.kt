package com.learner.secureprefs.security.converter

import android.util.Base64
import com.learner.secureprefs.security.ModelByteArrayConverter

/**
 * Converts a Base64String to ByteArray and vice-versa
 *
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
object Base64Converter : ModelByteArrayConverter<String> {

    override fun toByteArray(input: String): ByteArray = Base64.decode(input, Base64.NO_WRAP)

    override fun fromByteArray(byteArray: ByteArray): String = Base64.encodeToString(byteArray, Base64.NO_WRAP)
}