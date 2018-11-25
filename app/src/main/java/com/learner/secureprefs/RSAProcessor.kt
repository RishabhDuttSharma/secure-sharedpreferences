package com.learner.secureprefs

import java.security.Key
import javax.crypto.Cipher

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
abstract class RSAProcessor<S, T>(private val securityKey: Key, private val sourceConverter: ModelByteArrayConverter<S>, private val targetConverter: ModelByteArrayConverter<T>) {

    private val cipher = Cipher.getInstance("RSA")

    private fun process(operationMode: Int, input: ByteArray) = cipher.init(operationMode, securityKey).run {
        cipher.doFinal(input)
    }

    private fun <S, T> process(operationMode: Int, sourceConverter: ModelByteArrayConverter<S>, targetConverter: ModelByteArrayConverter<T>, input: S) =
            targetConverter.fromByteArray(process(operationMode, sourceConverter.toByteArray(input)))

    fun encode(input: S): T = process(Cipher.ENCRYPT_MODE, sourceConverter, targetConverter, input)

    fun decode(input: T): S = process(Cipher.DECRYPT_MODE, targetConverter, sourceConverter, input)

    interface ModelByteArrayConverter<X> {

        fun toByteArray(input: X): ByteArray

        fun fromByteArray(byteArray: ByteArray): X
    }
}