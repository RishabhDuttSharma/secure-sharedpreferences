package com.learner.secureprefs

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/26/2018
 */
interface SecurityProcessor {

    fun process(input: ByteArray): ByteArray
}