package com.learner.secureprefs

import java.security.Key
import javax.crypto.Cipher

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
open class RSAProcessor(operationMode: Int, securityKey: Key) : SecurityProcessor {

    private val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding").apply {
        init(operationMode, securityKey)
    }

    override fun process(input: ByteArray): ByteArray = cipher.doFinal(input)
}