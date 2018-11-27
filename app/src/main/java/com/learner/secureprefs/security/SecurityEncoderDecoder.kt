package com.learner.secureprefs.security

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/27/2018
 */
interface SecurityEncoderDecoder<S, T> {

    fun encode(input: S): T

    fun decode(input: T): S
}