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
 * Implementation of SharedPreferences to persist Key-Value(s) in secure-form and retrieve them, when required.
 *
 * Uses AndroidKeyStore to generate and store Keys for Encoding and Decoding values.
 * SecretKey is retrieved, generated if does not exist, via SecretKey implementations of KeyProviderSpi
 *
 * Manages generation of SecretKey for Android versions below API 23 and API 23+
 *
 * Keys are encoded using One-Way Fixed-Length Hash Algorithms
 * Values are encoded using a combination of Key-Value Serialization and AES-Transformation
 *
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/27/2018
 */
class SecureSharedPreferences constructor(context: Context, name: String = "secure-prefs", mode: Int = Context.MODE_PRIVATE) : SharedPreferences {

    private val preferences = context.getSharedPreferences(name, mode)

    /**
     * Creates and Initialized an Instance of Wrapper enclosing AES - Encoder/Decoder for securing Key-Value.
     *
     * For Android versions API 23 (i.e., Marshmallow) and later, Secret-Key is generated and stored inside AndroidKeyStore.
     *
     * For Android versions below API 23 (i.e., Marshmallow) Secret-Key is persisted in SharedPreferences and
     * secured with RSA-KeyPair which is generated and stored inside AndroidKeyStore.
     */
    private val valueProcessor = with((if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        PrefsSecretKeyProvider(context) else KeyStoreSecretKeyProvider).getKey("secure-prefs-key")) {
        StringBase64Processor(AESEncoder(this), AESDecoder(this))
    }

    /**
     * Performs encoding of given value corresponding to the type of ProcessorWrapper
     *
     * @param processor instance of Wrapper for Encoding/Decoding certain values
     * @param value String representation of Value to be Secured
     *
     * @return secured-form (i.e., encoded) of [value]
     */
    private fun getEncoded(processor: StringBase64Processor, value: String?) =
            if (value == null) null else processor.encode(value)


    /**
     * Performs decryption of given value corresponding to the type of ProcessorWrapper
     *
     * @param processor instance of Wrapper for Encoding/Decoding certain values
     * @param value String representation of Value to be Revealed
     *
     * @return secured-form (i.e., encoded) of [value]
     */
    private fun getDecoded(processor: StringBase64Processor, value: String?) =
            if (value == null) null else processor.decode(value)


    /**
     * Generates a certain One-Way Hash for KEY which can used for KEY_LOOK_UP
     *
     * @param value String representation of KEY in natural-language
     *
     * @return String representation of Hashed value calculated for [value]
     */
    private fun getEncodedKey(value: String?) = if (value == null) null else
        Base64.encodeToString(KeyHashGenerator.process(value.toByteArray()), Base64.NO_WRAP)

    /**
     * Serializes and Encodes the Key-Value pair
     *
     * @param key String representation of [key] in natural-language
     * @param value String representation of [value] in natural-language
     *
     * @return String representation of Encoded value for Serialized Pair([key],[value])
     */
    private fun getEncodedKeyValue(key: String?, value: String?) = getEncoded(valueProcessor,
            KeyValueWrapper.wrap(Pair(key ?: "", value ?: ""))
    )

    /**
     * Decodes and De-serializes a given [value]
     *
     * @param value String representation of Serialized and Encoded form for a String representation of natural-language value
     *
     * @return String representation of actual natural-language value
     */
    private fun getDecodedKeyValue(value: String?) = getDecoded(valueProcessor, value)?.run {
        KeyValueWrapper.unwrap(this)
    }

    /**
     * Provides actual-value, from SharedPreferences, corresponding to a natural-language [key]
     *
     * @param key String representation of [key] in natural-language
     * @param defValue Value to be returned if no [key] entry exists
     *
     * @return actual-value, if [key] exists, String representation of [defValue] otherwise
     */
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

    /**
     * Implementation of SharedPreferences.Editor to persist Key-Values in a more Secure manner.
     *
     * Converts all representations of data supported by Editor to String for uniform persistence and
     * retrieval of Encode Key-Values
     */
    inner class SecureEditor internal constructor(private val editor: SharedPreferences.Editor) : SharedPreferences.Editor {


        /**
         * Puts Secure (i.e., Encrypted) version of Key-Value pair to instance of SharedPreference.Editor
         * held by SecureEditor.
         *
         * Note: apply {...} ensures to return current instance of SecureEditor instead of actual Editor itself
         *
         * @param key String representation of KEY in natural language
         * @param value String representation of VALUE in natural language
         *
         * @return instance current SecureEditor (to prevent exposure of actual Editor, which may lead to inconsistencies)
         */
        private fun <T> putValue(key: String?, value: T?): SharedPreferences.Editor = apply {
            editor.putString(getEncodedKey(key), getEncodedKeyValue(key, value?.toString()))
        }

        override fun clear(): SharedPreferences.Editor = apply { editor.clear() }

        override fun putLong(key: String?, value: Long) = putValue(key, value)

        override fun putInt(key: String?, value: Int) = putValue(key, value)

        override fun remove(key: String?): SharedPreferences.Editor = apply { editor.remove(getEncodedKey(key)) }

        override fun putBoolean(key: String?, value: Boolean) = putValue(key, value)

        override fun putStringSet(key: String?, values: MutableSet<String>?): SharedPreferences.Editor = apply {
            editor.putStringSet(getEncodedKey(key), hashSetOf<String>().apply {
                values?.forEach { add(getEncodedKeyValue(key, it) ?: "") }
            })
        }

        override fun commit() = editor.commit()

        override fun putFloat(key: String?, value: Float) = putValue(key, value)

        override fun apply() = editor.apply()

        override fun putString(key: String?, value: String?) = putValue(key, value)
    }
}