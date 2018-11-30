package com.learner.secureprefs.security.impl.aes

import com.learner.secureprefs.security.SecurityProcessor
import java.nio.ByteBuffer
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
open class AESDecoder(private val securityKey: Key) : SecurityProcessor {

    private val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")

    override fun process(input: ByteArray): ByteArray = cipher.run {

        val byteBuffer = ByteBuffer.wrap(input)

        val ivLength = byteBuffer.get()
        val iv = ByteArray(ivLength.toInt()).apply { byteBuffer.get(this) }
        val cipherText = ByteArray(byteBuffer.remaining()).apply { byteBuffer.get(this) }

        init(Cipher.DECRYPT_MODE, securityKey, IvParameterSpec(iv))
        doFinal(cipherText)
    }
}