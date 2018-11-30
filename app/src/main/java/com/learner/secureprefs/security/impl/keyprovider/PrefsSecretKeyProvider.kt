package com.learner.secureprefs.security.impl.keyprovider

import android.content.Context
import com.learner.secureprefs.security.impl.Constant
import com.learner.secureprefs.security.impl.rsa.RSABase64StringEncoderDecoder
import com.learner.secureprefs.security.impl.rsa.RSAKeyStoreHelper
import java.security.SecureRandom
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
class PrefsSecretKeyProvider(context: Context): KeyProvider<SecretKey> {



    override fun containsKey(alias: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun generateKey(alias: String): SecretKey {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getExistingKey(alias: String): SecretKey {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getSecretKey(context: Context, alias: String): SecretKey = getSecretKeyFromPrefs(context, alias)

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
}