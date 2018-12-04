package com.learner.secureprefs.security.impl

import com.learner.secureprefs.security.SecurityProcessor
import com.learner.secureprefs.security.converter.Base64Converter
import com.learner.secureprefs.security.converter.StringConverter

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 12/2/2018
 */
class StringBase64Processor(private val encoder: SecurityProcessor, private val decoder: SecurityProcessor) {

    fun encode(input: String) = Base64Converter.fromByteArray(encodeToByteArray(input))

    fun encodeToByteArray(input: String): ByteArray = encoder.process(StringConverter.toByteArray(input))

    fun encodeToString(input: ByteArray) = Base64Converter.fromByteArray(encoder.process(input))

    fun decode(input: String) = StringConverter.fromByteArray(decodeToByteArray(input))

    fun decodeToByteArray(input: String): ByteArray = decoder.process(Base64Converter.toByteArray(input))

    fun decodeToString(input: ByteArray) = StringConverter.fromByteArray(decoder.process(input))
}