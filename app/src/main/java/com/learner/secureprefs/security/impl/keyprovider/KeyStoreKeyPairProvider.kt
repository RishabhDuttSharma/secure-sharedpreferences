package com.learner.secureprefs.security.impl.keyprovider

import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.Key
import java.security.KeyPair
import java.security.KeyPairGenerator
import javax.security.auth.x500.X500Principal

/**
 * Collective implementation for Public/Private Keys generates and stored inside AndroidKeyStore
 *
 * Developer: Rishabh Dutt Sharma
 * Dated: 12/1/2018
 */
abstract class KeyStoreKeyPairProvider<T>(private val context: Context) : KeyStoreKeyProvider<T>() where T : Key {

    override fun generateKey(alias: String): T = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore").run {

        val x500Principal = X500Principal("CN=$alias")

        initialize(if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSubject(x500Principal)
                .build()
        else KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setCertificateSubject(x500Principal)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setDigests(KeyProperties.DIGEST_SHA256)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .build())

        resolve(generateKeyPair())
    }

    /**
     * Resolves the required Key from KeyPair
     */
    abstract fun resolve(keyPair: KeyPair): T
}