package com.learner.secureprefs.security.impl

import com.learner.secureprefs.security.ModelByteArrayConverter
import com.learner.secureprefs.security.RSAProcessor
import com.learner.secureprefs.security.SecurityEncoderDecoder
import java.security.KeyPair
import javax.crypto.Cipher

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
open class RSAEncoderDecoder<S, T>(keyPair: KeyPair, private val sourceConverter: ModelByteArrayConverter<S>, private val targetConverter: ModelByteArrayConverter<T>) : SecurityEncoderDecoder<S, T> {

    private val encoder = RSAProcessor(Cipher.ENCRYPT_MODE, keyPair.public)

    private val decoder = RSAProcessor(Cipher.DECRYPT_MODE, keyPair.private)

    override fun encode(input: S): T = process(encoder, sourceConverter, targetConverter, input)

    override fun decode(input: T): S = process(decoder, targetConverter, sourceConverter, input)

    private fun <S, T> process(processor: RSAProcessor, sourceConverter: ModelByteArrayConverter<S>, targetConverter: ModelByteArrayConverter<T>, input: S) =
            targetConverter.fromByteArray(processor.process(sourceConverter.toByteArray(input)))

}