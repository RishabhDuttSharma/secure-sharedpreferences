package com.learner.secureprefs

import android.util.Base64

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
class Base64Converter : RSAProcessor.ModelByteArrayConverter<String> {

    override fun toByteArray(input: String): ByteArray = Base64.decode(input, Base64.NO_WRAP)

    override fun fromByteArray(byteArray: ByteArray): String = Base64.encodeToString(byteArray, Base64.NO_WRAP)
}