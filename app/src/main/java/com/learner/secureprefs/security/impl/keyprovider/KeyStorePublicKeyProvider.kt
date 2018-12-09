package com.learner.secureprefs.security.impl.keyprovider

import android.content.Context
import java.security.KeyPair
import java.security.PublicKey

/**
 * Provides [PublicKey] generated and stored inside AndroidKeyStore
 *
 * Developer: Rishabh Dutt Sharma
 * Dated: 12/1/2018
 */
class KeyStorePublicKeyProvider(context: Context) : KeyStoreKeyPairProvider<PublicKey>(context) {

    override fun resolve(keyPair: KeyPair): PublicKey = keyPair.public

    override fun getExistingKey(alias: String): PublicKey = androidKeyStore.getCertificate(alias).publicKey

}