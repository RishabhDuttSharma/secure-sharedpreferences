package com.learner.secureprefs.security.impl.keyprovider

import java.security.Key
import java.security.KeyStore

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
abstract class KeyStoreKeyProvider<T> : KeyProvider<T> where T : Key {

    internal val androidKeyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    override fun containsKey(alias: String): Boolean = androidKeyStore.containsAlias(alias)
}