package com.learner.secureprefs.security

/**
 * Interface for classes that would implement Data Security, that would
 * either generate a secured (i.e., encoded) output corresponding to a raw input
 * or extract raw information (i.e., decoded) from a secured input.
 *
 *
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/26/2018
 */
interface SecurityProcessor {

    /**
     * Processes a given input ByteArray and generate a corresponding output ByteArray
     *
     * @param input to be processed
     *
     * @return processed output
     */
    fun process(input: ByteArray): ByteArray
}