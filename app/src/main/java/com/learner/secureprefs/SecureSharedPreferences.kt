package com.learner.secureprefs

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.learner.secureprefs.security.impl.StringBase64Processor
import com.learner.secureprefs.security.impl.aes.AESDecoder
import com.learner.secureprefs.security.impl.aes.AESEncoder
import com.learner.secureprefs.security.impl.aes.AESIVProcessor
import com.learner.secureprefs.security.impl.keyprovider.KeyStorePrivateKeyProvider
import com.learner.secureprefs.security.impl.keyprovider.KeyStorePublicKeyProvider
import com.learner.secureprefs.security.impl.keyprovider.KeyStoreSecretKeyProvider
import com.learner.secureprefs.security.impl.keyprovider.PrefsSecretKeyProvider
import com.learner.secureprefs.security.impl.rsa.RSAProcessor
import java.security.SecureRandom
import javax.crypto.Cipher

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/27/2018
 */
class SecureSharedPreferences constructor(context: Context, name: String = "secure-prefs", mode: Int = Context.MODE_PRIVATE) : SharedPreferences {

    private val preferences = context.getSharedPreferences(name, mode)

    private val keyProcessor: StringBase64Processor
    private val valueProcessor: StringBase64Processor

    init {

        val secretKey = (if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) PrefsSecretKeyProvider(context)
        else KeyStoreSecretKeyProvider).getKey("secure-prefs-key")
        valueProcessor = StringBase64Processor(AESEncoder(secretKey), AESDecoder(secretKey))

        val rsaProcessor = with("secret-iv-provider") {
            StringBase64Processor(RSAProcessor(Cipher.ENCRYPT_MODE, KeyStorePublicKeyProvider(context).getKey(this)),
                    RSAProcessor(Cipher.DECRYPT_MODE, KeyStorePrivateKeyProvider(context).getKey(this)))
        }

        with(rsaProcessor.encode("key_iv")) {
            val encodedKeyIVValue = preferences.getString(this, null)
            if (encodedKeyIVValue == null) ByteArray(16).apply { SecureRandom().nextBytes(this) }
                    .also { preferences.edit().putString(this, rsaProcessor.encodeToString(it)).apply() }
            else rsaProcessor.decodeToByteArray(encodedKeyIVValue)
        }.also {
            keyProcessor = StringBase64Processor(AESIVProcessor(Cipher.ENCRYPT_MODE, secretKey, it),
                    AESIVProcessor(Cipher.DECRYPT_MODE, secretKey, it))
        }
    }

    private fun getEncoded(processor: StringBase64Processor, value: String?) = if (value == null) null else processor.encode(value)
    private fun getDecoded(processor: StringBase64Processor, value: String?) = if (value == null) null else processor.decode(value)

    private fun getEncodedKey(value: String?) = getEncoded(keyProcessor, value)
    private fun getDecodedKey(value: String?) = getDecoded(keyProcessor, value)

    private fun getEncodedValue(value: String?) = getEncoded(valueProcessor, value)
    private fun getDecodedValue(value: String?) = getDecoded(valueProcessor, value)

    private fun <T> getValue(key: String?, defValue: T?): String? {
        val prefsVal = preferences.getString(getEncodedKey(key), null)
        return if (prefsVal != null) getDecodedValue(prefsVal) else defValue?.toString()
    }

    override fun contains(key: String?) = preferences.contains(getEncodedKey(key))

    override fun getBoolean(key: String?, defValue: Boolean) =
            getValue(key, defValue)?.toBoolean() ?: defValue

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) =
            preferences.unregisterOnSharedPreferenceChangeListener(listener)

    override fun getInt(key: String?, defValue: Int) =
            getValue(key, defValue)?.toIntOrNull() ?: defValue

    override fun getAll(): MutableMap<String, *> = hashMapOf<String, String>().apply {
        preferences.all.entries.forEach { entry ->
            put(getDecodedKey(entry.key) ?: "", getDecodedValue(entry.value.toString()) ?: "")
        }
    }

    override fun edit() = SecureEditor(preferences.edit())

    override fun getLong(key: String?, defValue: Long) =
            getValue(key, defValue)?.toLongOrNull() ?: defValue

    override fun getFloat(key: String?, defValue: Float) =
            getValue(key, defValue)?.toFloatOrNull() ?: defValue

    override fun getStringSet(key: String?, defValues: MutableSet<String>?) = preferences.getStringSet(getEncodedKey(key), null)?.run {
        hashSetOf<String>().apply { this@run.forEach { add(getDecodedValue(it) ?: "") } }
    } ?: defValues

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) =
            preferences.registerOnSharedPreferenceChangeListener(listener)

    override fun getString(key: String?, defValue: String?) = getValue(key, defValue) ?: defValue

    inner class SecureEditor(private val editor: SharedPreferences.Editor) : SharedPreferences.Editor {

        override fun clear(): SharedPreferences.Editor = editor.clear()

        private fun <T> putValue(key: String?, value: T?): SharedPreferences.Editor =
                editor.putString(getEncodedKey(key), getEncodedValue(value?.toString() ?: ""))

        override fun putLong(key: String?, value: Long) = putValue(key, value)

        override fun putInt(key: String?, value: Int) = putValue(key, value)

        override fun remove(key: String?): SharedPreferences.Editor = editor.remove(getEncodedKey(key))

        override fun putBoolean(key: String?, value: Boolean) = putValue(key, value)

        override fun putStringSet(key: String?, values: MutableSet<String>?): SharedPreferences.Editor = editor.putStringSet(getEncodedKey(key), hashSetOf<String>().apply {
            values?.forEach { add(getEncodedValue(it) ?: "") }
        })

        override fun commit() = editor.commit()

        override fun putFloat(key: String?, value: Float) = putValue(key, value)

        override fun apply() = editor.apply()

        override fun putString(key: String?, value: String?) = putValue(key, value)
    }
}