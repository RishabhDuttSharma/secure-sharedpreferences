package com.learner.secureprefs.security.impl.keyprovider

import java.security.Key

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 12/1/2018
 */
interface KeyProvider<T> where T : Key {

    fun getKey(alias: String) = if (containsKey(alias)) getExistingKey(alias) else generateKey(alias)

    fun containsKey(alias: String): Boolean

    fun generateKey(alias: String): T

    fun getExistingKey(alias: String): T
}