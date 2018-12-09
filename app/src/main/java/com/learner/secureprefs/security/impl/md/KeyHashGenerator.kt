package com.learner.secureprefs.security.impl.md

import com.learner.secureprefs.security.SecurityProcessor
import java.security.MessageDigest

/**
 *
 * One-Way Fixed-Length Hash generator (specially for Key Look-up purposes)
 *
 * Provider: MessageDigest
 * Algorithm: SHA-256
 *
 * Developer: Rishabh Dutt Sharma
 * Dated: 12/5/2018
 */
object KeyHashGenerator : SecurityProcessor {

    private val messageDigest by lazy { MessageDigest.getInstance("SHA-256") }

    override fun process(input: ByteArray): ByteArray = messageDigest.run {
        update(input)
        digest()
    }.also { messageDigest.reset() }
}