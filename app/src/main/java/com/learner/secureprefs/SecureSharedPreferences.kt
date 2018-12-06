package com.learner.secureprefs

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Base64
import com.learner.secureprefs.security.impl.KeyValueWrapper
import com.learner.secureprefs.security.impl.StringBase64Processor
import com.learner.secureprefs.security.impl.aes.AESDecoder
import com.learner.secureprefs.security.impl.aes.AESEncoder
import com.learner.secureprefs.security.impl.keyprovider.KeyStoreSecretKeyProvider
import com.learner.secureprefs.security.impl.keyprovider.PrefsSecretKeyProvider
import com.learner.secureprefs.security.impl.md.KeyHashGenerator

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/27/2018
 */
class SecureSharedPreferences constructor(context: Context, name: String = "secure-prefs", mode: Int = Context.MODE_PRIVATE) : SharedPreferences {

    private val preferences = context.getSharedPreferences(name, mode)

    private val valueProcessor: StringBase64Processor

    init {
        with((if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) PrefsSecretKeyProvider(context)
        else KeyStoreSecretKeyProvider).getKey("secure-prefs-key")) {
            valueProcessor = StringBase64Processor(AESEncoder(this), AESDecoder(this))
        }
    }

    private fun getEncoded(processor: StringBase64Processor, value: String?) =
            if (value == null) null else processor.encode(value)

    private fun getDecoded(processor: StringBase64Processor, value: String?) =
            if (value == null) null else processor.decode(value)

    private fun getEncodedKey(value: String?) = if (value == null) null else
        Base64.encodeToString(KeyHashGenerator.process(value.toByteArray()), Base64.NO_WRAP)

    private fun getEncodedKeyValue(key: String?, value: String?) = getEncoded(valueProcessor,
            KeyValueWrapper.serialize(Pair(key ?: "", value ?: ""))
    )

    private fun getDecodedKeyValue(value: String?) = getDecoded(valueProcessor, value)?.run {
        KeyValueWrapper.deserialize(this)
    }

    private fun <T> getValue(key: String?, defValue: T?): String? {
        val prefsVal = preferences.getString(getEncodedKey(key), null)
        return if (prefsVal != null) getDecodedKeyValue(prefsVal)?.second else defValue?.toString()
    }

    override fun contains(key: String?) = preferences.contains(getEncodedKey(key))

    override fun getBoolean(key: String?, defValue: Boolean) =
            getValue(key, defValue)?.toBoolean() ?: defValue

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) =
            preferences.unregisterOnSharedPreferenceChangeListener(listener)

    override fun getInt(key: String?, defValue: Int) =
            getValue(key, defValue)?.toIntOrNull() ?: defValue

    override fun getAll(): MutableMap<String, *> = hashMapOf<String, String>().apply {
        preferences.all.entries.forEach {
            getDecodedKeyValue(it.value.toString())?.run { put(first, second) }
        }
    }

    override fun edit() = SecureEditor(preferences.edit())

    override fun getLong(key: String?, defValue: Long) =
            getValue(key, defValue)?.toLongOrNull() ?: defValue

    override fun getFloat(key: String?, defValue: Float) =
            getValue(key, defValue)?.toFloatOrNull() ?: defValue

    override fun getStringSet(key: String?, defValues: MutableSet<String>?) = preferences.getStringSet(getEncodedKey(key), null)?.run {
        hashSetOf<String>().apply { this@run.forEach { add(getDecodedKeyValue(it)?.second ?: "") } }
    } ?: defValues

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) =
            preferences.registerOnSharedPreferenceChangeListener(listener)

    override fun getString(key: String?, defValue: String?) = getValue(key, defValue) ?: defValue

    inner class SecureEditor(private val editor: SharedPreferences.Editor) : SharedPreferences.Editor {

        override fun clear(): SharedPreferences.Editor = editor.clear()

        private fun <T> putValue(key: String?, value: T?): SharedPreferences.Editor = apply {
            editor.putString(getEncodedKey(key), getEncodedKeyValue(key, value?.toString()))
        }

        override fun putLong(key: String?, value: Long) = putValue(key, value)

        override fun putInt(key: String?, value: Int) = putValue(key, value)

        override fun remove(key: String?): SharedPreferences.Editor = editor.remove(getEncodedKey(key))

        override fun putBoolean(key: String?, value: Boolean) = putValue(key, value)

        override fun putStringSet(key: String?, values: MutableSet<String>?): SharedPreferences.Editor = editor.putStringSet(getEncodedKey(key), hashSetOf<String>().apply {
            values?.forEach { add(getEncodedKeyValue(key, it) ?: "") }
        })

        override fun commit() = editor.commit()

        override fun putFloat(key: String?, value: Float) = putValue(key, value)

        override fun apply() = editor.apply()

        override fun putString(key: String?, value: String?) = putValue(key, value)
    }
}