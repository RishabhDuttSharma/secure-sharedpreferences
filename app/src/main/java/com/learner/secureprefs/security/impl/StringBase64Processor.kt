package com.learner.secureprefs.security.impl

import com.learner.secureprefs.security.SecurityProcessor
import com.learner.secureprefs.security.converter.Base64Converter
import com.learner.secureprefs.security.converter.StringConverter

/**
 * Wraps the functionality to Encode/Decode a String Input and generate a String output
 * using a combination of implementations of SecurityProcessor
 *
 * @param encoder instance of SecurityProcessor to perform Encoding
 * @param decoder instance of SecurityProcessor to perform Decoding corresponding to [encoder]
 *
 *
 * Developer: Rishabh Dutt Sharma
 * Dated: 12/2/2018
 */
class StringBase64Processor(private val encoder: SecurityProcessor, private val decoder: SecurityProcessor) {

    /**
     * Encodes a String value using [encoder] and generates a Base64 String representation
     *
     * @param input String representation of value to be secured
     *
     * @return Base64 String representation of encoded-input
     */
    fun encode(input: String) = Base64Converter.fromByteArray(
            encoder.process(StringConverter.toByteArray(input))
    )

    /**
     * Decodes the Base64 String representation using [decoder] and returns String representation of actual value
     *
     * @param input Base64 String representation of Encoded value
     *
     * @return String representation of Actual (i.e., Decoded) value
     *
     */
    fun decode(input: String) = StringConverter.fromByteArray(
            decoder.process(Base64Converter.toByteArray(input))
    )
}