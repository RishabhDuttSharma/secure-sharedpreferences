package com.learner.secureprefs.security.converter

import com.learner.secureprefs.security.ModelByteArrayConverter

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
object StringConverter : ModelByteArrayConverter<String> {

    override fun toByteArray(input: String) = input.toByteArray()

    override fun fromByteArray(byteArray: ByteArray) = String(byteArray)
}