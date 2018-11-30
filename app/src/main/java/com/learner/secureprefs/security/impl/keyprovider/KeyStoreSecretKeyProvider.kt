package com.learner.secureprefs.security.impl.keyprovider

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 12/1/2018
 */
class KeyStoreSecretKeyProvider : KeyStoreKeyProvider<SecretKey>() {

    override fun getExistingKey(alias: String): SecretKey = androidKeyStore.getKey(alias, null) as SecretKey

    @RequiresApi(Build.VERSION_CODES.M)
    override fun generateKey(alias: String): SecretKey = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").run {

        init(KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build())

        generateKey()
    }
}