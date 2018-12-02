package com.learner.secureprefs.security.impl

import com.learner.secureprefs.security.SecurityProcessor
import com.learner.secureprefs.security.converter.Base64Converter
import com.learner.secureprefs.security.converter.StringConverter

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 12/2/2018
 */
class StringBase64Processor(private val encoder: SecurityProcessor, private val decoder: SecurityProcessor) {

    fun encode(input: String) = Base64Converter.fromByteArray(encoder.process(StringConverter.toByteArray(input)))

    fun decode(input: String) = StringConverter.fromByteArray(decoder.process(Base64Converter.toByteArray(input)))
}