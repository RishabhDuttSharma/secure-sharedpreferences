package com.learner.secureprefs.converter

import com.learner.secureprefs.ModelByteArrayConverter

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
class StringConverter : ModelByteArrayConverter<String> {

    override fun toByteArray(input: String) = input.toByteArray()

    override fun fromByteArray(byteArray: ByteArray) = String(byteArray)
}