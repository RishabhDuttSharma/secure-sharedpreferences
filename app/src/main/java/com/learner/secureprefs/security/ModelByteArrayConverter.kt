package com.learner.secureprefs.security

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/26/2018
 */
interface ModelByteArrayConverter<T> {

    fun toByteArray(input: T): ByteArray

    fun fromByteArray(byteArray: ByteArray): T
}