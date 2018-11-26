package com.learner.secureprefs

import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import javax.security.auth.x500.X500Principal

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
class RSAKeyStoreHelper(context: Context, alias: String) {

    private val keyPair = KeyStore.getInstance("AndroidKeyStore").run {
        // initialize keystore
        load(null)

        // generate a new KeyPair, if exists no entry for alias
        if (!containsAlias(alias)) generateKeyPair(context, alias)

        // retrieve and return the Keys as Pair<A, B>
        Pair(getKey(alias, null) as PrivateKey, getCertificate(alias).publicKey as PublicKey)
    }

    val publicProcessor = RSABase64StringProcessor(keyPair.second)

    val privateProcessor = RSABase64StringProcessor(keyPair.first)

    private fun generateKeyPair(context: Context, alias: String) = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore").run {


        val x500Principal = X500Principal("CN=$alias")

        initialize(if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            KeyPairGeneratorSpec.Builder(context)
                    .setAlias(alias)
                    .setSubject(x500Principal)
                    .build()
        else KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setCertificateSubject(x500Principal)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setDigests(KeyProperties.DIGEST_SHA256)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .build())

        generateKeyPair()
    }
}