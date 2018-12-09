package com.learner.secureprefs.security.impl.aes

import com.learner.secureprefs.security.SecurityProcessor
import java.nio.ByteBuffer
import java.security.Key
import javax.crypto.Cipher

/**
 * Encodes a given Input ByteArray and generates a serialized output consisting
 * of IV and Ciphered output
 *
 * Algorithm: AES (Advanced Encryption Standards)
 * Block Mode: CBC (Code Block Cipher)
 * Padding: PKCS7Padding (Public Key Cryptography Standards)
 *
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
open class AESEncoder(private val securityKey: Key) : SecurityProcessor {

    private val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")

    override fun process(input: ByteArray): ByteArray = cipher.run {
        init(Cipher.ENCRYPT_MODE, securityKey)

        val cipherText = doFinal(input)
        return ByteBuffer.allocate(1 + iv.size + cipherText.size)
                .put(iv.size.toByte())
                .put(iv).put(cipherText).array()
    }
}