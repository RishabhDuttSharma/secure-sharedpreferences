package com.learner.secureprefs.security.impl.aes

import com.learner.secureprefs.security.SecurityProcessor
import java.nio.ByteBuffer
import java.security.Key
import javax.crypto.Cipher

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
open class AESEncoder(securityKey: Key) : SecurityProcessor {

    private val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding").apply {
        init(Cipher.ENCRYPT_MODE, securityKey)
    }

    override fun process(input: ByteArray): ByteArray {

        val cipherText = cipher.doFinal(input)
        val iv = cipher.iv

        return ByteBuffer.allocate(1 + iv.size + cipherText.size)
                .put(iv.size.toByte())
                .put(iv).put(cipherText).array()
    }
}