package com.learner.secureprefs.security.impl.keyprovider

import android.content.Context
import android.text.TextUtils
import android.util.Base64
import com.learner.secureprefs.security.KeyProvider
import com.learner.secureprefs.security.impl.rsa.RSAProcessor
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
class PrefsSecretKeyProvider(context: Context) : KeyProvider<SecretKey> {

    private val providerAlias = "prefs-provider"

    private val encoder by lazy { RSAProcessor(Cipher.ENCRYPT_MODE, KeyStorePublicKeyProvider(context).getKey(providerAlias)) }
    private val decoder by lazy { RSAProcessor(Cipher.DECRYPT_MODE, KeyStorePrivateKeyProvider(context).getKey(providerAlias)) }

    private val sharedPreferences = context.getSharedPreferences(providerAlias, Context.MODE_PRIVATE)

    override fun containsKey(alias: String): Boolean = sharedPreferences.contains(alias) && !TextUtils.isEmpty(sharedPreferences.getString(alias, null))

    override fun generateKey(alias: String): SecretKey =
            SecretKeySpec(ByteArray(16).apply { SecureRandom().nextBytes(this) }.also {
                sharedPreferences.edit().putString(alias, Base64.encodeToString(encoder.process(it), Base64.NO_WRAP)).apply()
            }, "AES")

    override fun getExistingKey(alias: String): SecretKey =
            SecretKeySpec(decoder.process(Base64.decode(sharedPreferences.getString(alias, null), Base64.NO_WRAP)), "AES")
}