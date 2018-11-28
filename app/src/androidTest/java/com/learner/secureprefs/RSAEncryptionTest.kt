package com.learner.secureprefs

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.learner.secureprefs.security.impl.rsa.RSABase64StringEncoderDecoder
import com.learner.secureprefs.security.impl.rsa.RSAKeyStoreHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/26/2018
 */
@RunWith(AndroidJUnit4::class)
class RSAEncryptionTest {

    @Test
    fun checkRSAKeyPairGeneration() {
        Assert.assertNotNull(RSAKeyStoreHelper.getKeyPair(InstrumentationRegistry.getContext(), "test-prefs"))
    }

    @Test
    fun checkRSAKeyPairIntegrity() {
        val keyPair = RSAKeyStoreHelper.getKeyPair(InstrumentationRegistry.getContext(), "test-prefs")
        val processor = RSABase64StringEncoderDecoder(keyPair)

        val rawText = "sample-text"

        val encodedText = processor.encode(rawText)
        val decodedText = processor.decode(encodedText)

        Assert.assertEquals(rawText, decodedText)
    }
}