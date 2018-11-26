package com.learner.secureprefs

import java.security.KeyPair
import javax.crypto.Cipher

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
abstract class RSAProcessorWrapper<S, T>(keyPair: KeyPair, private val sourceConverter: ModelByteArrayConverter<S>, private val targetConverter: ModelByteArrayConverter<T>) {

    private val encoder = RSAProcessor(Cipher.ENCRYPT_MODE, keyPair.public)

    private val decoder = RSAProcessor(Cipher.DECRYPT_MODE, keyPair.private)

    private fun <S, T> process(processor: RSAProcessor, sourceConverter: ModelByteArrayConverter<S>, targetConverter: ModelByteArrayConverter<T>, input: S) =
            targetConverter.fromByteArray(processor.process(sourceConverter.toByteArray(input)))

    fun encode(input: S): T = process(encoder, sourceConverter, targetConverter, input)

    fun decode(input: T): S = process(decoder, targetConverter, sourceConverter, input)

}