package com.learner.secureprefs.security.impl

import android.util.Base64
import java.nio.ByteBuffer

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 12/6/2018
 */
object KeyValueWrapper {

    fun wrap(pair: Pair<String, String>): String {
        val arrKey = pair.first.toByteArray()
        val arrValue = pair.second.toByteArray()
        return Base64.encodeToString(ByteBuffer.allocate(1 + arrKey.size + arrValue.size)
                .put(arrKey.size.toByte()).put(arrKey).put(arrValue).array(), Base64.NO_WRAP)
    }

    fun unwrap(base64Input: String): Pair<String, String> = ByteBuffer.wrap(Base64.decode(base64Input, Base64.NO_WRAP)).run {
        Pair(String(ByteArray(get().toInt()).apply { get(this) }), String(ByteArray(remaining()).apply { get(this) }))
    }
}