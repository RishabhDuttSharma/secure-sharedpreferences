package com.learner.secureprefs

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
class StringConverter : RSAProcessor.ModelByteArrayConverter<String> {

    override fun toByteArray(input: String) = input.toByteArray()

    override fun fromByteArray(byteArray: ByteArray) = String(byteArray)
}