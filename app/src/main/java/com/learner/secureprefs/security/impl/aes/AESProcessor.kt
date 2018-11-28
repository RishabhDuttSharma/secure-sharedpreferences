package com.learner.secureprefs.security.impl.aes

import com.learner.secureprefs.security.SecurityProcessor
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
open class AESProcessor(operationMode: Int, securityKey: Key, iv: ByteArray? = null) : SecurityProcessor {

    val cipher = Cipher.getInstance("AES/GCM/NoPadding").apply {
        if (iv != null) init(operationMode, securityKey, GCMParameterSpec(128, iv))
        else init(operationMode, securityKey)
    }

    override fun process(input: ByteArray): ByteArray = cipher.doFinal(input)
}