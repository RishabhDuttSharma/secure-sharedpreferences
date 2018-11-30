package com.learner.secureprefs.security.impl.keyprovider

import android.content.Context
import java.security.KeyPair
import java.security.PrivateKey

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 12/1/2018
 */
class KeyStorePrivateKeyProvider(context: Context) : KeyStoreKeyPairProvider<PrivateKey>(context) {

    override fun resolve(keyPair: KeyPair): PrivateKey = keyPair.private

    override fun getExistingKey(alias: String): PrivateKey = androidKeyStore.getKey(alias, null) as PrivateKey
}