package com.learner.secureprefs.security.impl

import android.util.Base64
import com.learner.secureprefs.security.converter.Base64Converter
import java.nio.ByteBuffer

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 12/6/2018
 */
class KeyValueDeserializer(private val base64Input: String) {

    override fun toString(): String =
with(Base64.decode(base64Input, Base64.NO_WRAP)){

        val arrKey = key.toByteArray()
        val arrValue = value.toByteArray()
        return Base64Converter.fromByteArray(ByteBuffer.allocate(1 + arrKey.size + arrValue.size)
                .put(arrKey.size.toByte()).put(arrKey).put(arrValue).array())
    }
}