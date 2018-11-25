package com.learner.secureprefs

import android.content.Context
import java.security.KeyStore

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
class RSAKeyStoreHelper(context: Context, alias: String) {

    private val keyStoreEntry: KeyStore.PrivateKeyEntry = KeyStore.getInstance("AndroidKeyStore").run {
        // initialize keystore
        load(null)

        // generate a new KeyPair, if exists no entry for alias
        if (!containsAlias(alias)) generateKeyPair(context, alias)

        // retrieve and return the Entry
        getEntry(alias, null) as KeyStore.PrivateKeyEntry
    }

    val publicProcessor = RSABase64StringProcessor(keyStoreEntry.certificate.publicKey)

    val privateProcessor = RSABase64StringProcessor(keyStoreEntry.privateKey)

    private fun generateKeyPair(context: Context, alias: String) {
        TODO("Code to Generate a new KeyPair")
    }
}