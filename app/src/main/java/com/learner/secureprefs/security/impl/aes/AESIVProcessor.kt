package com.learner.secureprefs.security.impl.aes

import com.learner.secureprefs.security.SecurityProcessor
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
class AESIVProcessor(private val opMode: Int, private val securityKey: Key, private val Iv: ByteArray) : SecurityProcessor {

    private val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding").apply {
        init(opMode, securityKey, IvParameterSpec(ByteArray(16)))
    }

    override fun process(input: ByteArray): ByteArray = cipher.doFinal(input)
}