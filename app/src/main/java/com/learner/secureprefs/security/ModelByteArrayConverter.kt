package com.learner.secureprefs.security

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/26/2018
 */
interface ModelByteArrayConverter<X> {

    fun toByteArray(input: X): ByteArray

    fun fromByteArray(byteArray: ByteArray): X
}