package com.learner.secureprefs.security

import java.security.Key

/**
 * Service Provider Interface to be implemented by all the classes that would provide an instance of Key
 *
 * Developer: Rishabh Dutt Sharma
 * Dated: 12/1/2018
 */
interface KeyProviderSpi<T> where T : Key {

    /**
     * Retrieves the Key corresponding to [alias], if exists, generates a new Key otherwise.
     *
     * @param alias name for the Key
     *
     * @return instance of Key
     */
    fun getKey(alias: String) = if (containsKey(alias)) getExistingKey(alias) else generateKey(alias)

    /**
     * Check whether the [alias] exists
     *
     * @param alias name for the Key
     *
     * @return true, if the [alias] exists, false otherwise
     */
    fun containsKey(alias: String): Boolean

    /**
     * Generates a new Key for given [alias], and returns it
     *
     * @param alias name for the Key
     *
     * @return instance of Key
     */
    fun generateKey(alias: String): T

    /**
     * Should be called with [containsKey]
     *
     * @return instance of existing Key
     */
    fun getExistingKey(alias: String): T
}