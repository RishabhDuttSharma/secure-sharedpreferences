package com.learner.secureprefs.security.impl.aes

import com.learner.secureprefs.security.SecurityProcessor
import java.nio.ByteBuffer
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

/**
 *
 * Decodes a given Input ByteArray (serialized output from AESEncoder) to actual value
 *
 * Algorithm: AES (Advanced Encryption Standards)
 * Block Mode: CBC (Code Block Cipher)
 * Padding: PKCS7Padding (Cryptographic Message Syntax Standard)
 *
 * @param securityKey instance of [javax.crypto.SecretKey]
 *
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
open class AESDecoder(private val securityKey: Key) : SecurityProcessor {

    private val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")

    override fun process(input: ByteArray): ByteArray = cipher.run {

        val byteBuffer = ByteBuffer.wrap(input)

        val ivLength = byteBuffer.get() // retrieves the length of IV
        val iv = ByteArray(ivLength.toInt()).apply { byteBuffer.get(this) } // retrieves complete IV
        val cipherText = ByteArray(byteBuffer.remaining()).apply { byteBuffer.get(this) } // retrieves complete Ciphered Value

        init(Cipher.DECRYPT_MODE, securityKey, IvParameterSpec(iv))
        doFinal(cipherText)
    }
}