package com.learner.secureprefs.security

/**
 * Interface to be implemented by Classes that would convert a Model to ByteArray and vice-versa
 *
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/26/2018
 */
interface ModelByteArrayConverter<T> {

    /**
     * Called when model is serialized
     *
     * @param input instance of Model
     *
     * @return serialized model
     */
    fun toByteArray(input: T): ByteArray

    /**
     * Called when model is de-serialized
     *
     * @param byteArray serialized model
     *
     * @return instance of Model
     */
    fun fromByteArray(byteArray: ByteArray): T
}