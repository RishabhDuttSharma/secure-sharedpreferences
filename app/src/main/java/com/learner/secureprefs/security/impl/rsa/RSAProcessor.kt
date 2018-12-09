package com.learner.secureprefs.security.impl.rsa

import com.learner.secureprefs.security.SecurityProcessor
import java.security.Key
import javax.crypto.Cipher

/**
 *
 * Performs Encoding/Decoding on given input corresponding to specified operationMode and securityKey
 *
 * Algorithm: RSA (Rivest-Shamir-Adleman)
 * Block Mode: ECB (Electronic CodeBook)
 * Padding: PKCS1Padding (RSA Cryptography Standard)
 *
 *
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
open class RSAProcessor(operationMode: Int, securityKey: Key) : SecurityProcessor {

    private val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding").apply {
        init(operationMode, securityKey)
    }

    override fun process(input: ByteArray): ByteArray = cipher.doFinal(input)
}