package com.learner.secureprefs.security.impl

import android.util.Base64
import java.nio.ByteBuffer

/**
 * Performs serialization and deserialization of Pair<String, String>
 *
 * Developer: Rishabh Dutt Sharma
 * Dated: 12/6/2018
 */
object KeyValueWrapper {

    /**
     * Serializes Pair<String, String> to Base64String representation
     *
     * @param input instance of Pair<String, String>
     *
     * @return Base64 String
     */
    fun wrap(input: Pair<String, String>): String {
        val arrKey = input.first.toByteArray()
        val arrValue = input.second.toByteArray()
        return Base64.encodeToString(ByteBuffer.allocate(1 + arrKey.size + arrValue.size)
                .put(arrKey.size.toByte()).put(arrKey).put(arrValue).array(), Base64.NO_WRAP)
    }

    /**
     * De-Serializes a Base64 string to Pair<String, String>
     *
     * @param Base64 String
     *
     * @return instance of Pair<String, String>
     */
    fun unwrap(base64Input: String): Pair<String, String> = ByteBuffer.wrap(Base64.decode(base64Input, Base64.NO_WRAP)).run {
        Pair(String(ByteArray(get().toInt()).apply { get(this) }), String(ByteArray(remaining()).apply { get(this) }))
    }
}