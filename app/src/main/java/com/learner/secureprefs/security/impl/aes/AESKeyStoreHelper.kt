package com.learner.secureprefs.security.impl.aes

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import com.learner.secureprefs.security.impl.Constant
import com.learner.secureprefs.security.impl.rsa.RSABase64StringEncoderDecoder
import com.learner.secureprefs.security.impl.rsa.RSAKeyStoreHelper
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
object AESKeyStoreHelper {

    fun getSecretKey(context: Context, alias: String): SecretKey = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        getSecretKeyFromPrefs(context, alias) else getSecretKeyFromKeyStore(alias)

    private fun getSecretKeyFromPrefs(context: Context, alias: String): SecretKey {

        val encoderDecoder = RSABase64StringEncoderDecoder(RSAKeyStoreHelper.getKeyPair(context, Constant.KEY_ALIAS_RSA))

        val sharedPrefsName = encoderDecoder.encode(alias)
        val sharedPrefsKey = "key_$sharedPrefsName"
        val sharedPreferences = context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE)

        val encodedKey = sharedPreferences.getString(sharedPrefsKey, null)

        return SecretKeySpec(if (encodedKey != null) encoderDecoder.decode(encodedKey).toByteArray()
        else getRandomByteArray().also {
            sharedPreferences.edit().putString(sharedPrefsKey, encoderDecoder.encode(String(it))).apply()
        }, "AES")
    }

    private fun getRandomByteArray() = ByteArray(16).apply { SecureRandom().nextBytes(this) }


    @RequiresApi(Build.VERSION_CODES.M)
    fun getSecretKeyFromKeyStore(alias: String): SecretKey = KeyStore.getInstance("AndroidKeyStore").run {
        // initialize keystore
        load(null)

        // retrieve Keys, create and return KeyPair, if exists
        if (containsAlias(alias))
            getKey(alias, null) as SecretKey
        // generate a new KeyPair, if exists no entry for alias
        else generateSecretKey(alias)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateSecretKey(alias: String) = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").run {

        init(KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build())

        generateKey()
    }
}