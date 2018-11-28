package com.learner.secureprefs

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.learner.secureprefs.security.impl.Constant
import com.learner.secureprefs.security.impl.aes.AESKeyStoreHelper
import com.learner.secureprefs.security.impl.aes.AESProcessor
import com.learner.secureprefs.security.impl.rsa.RSABase64StringEncoderDecoder
import com.learner.secureprefs.security.impl.rsa.RSAKeyStoreHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import javax.crypto.Cipher

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

    @Test
    fun checkAESIntegrity() {

        val secretKey = AESKeyStoreHelper.getSecretKey(InstrumentationRegistry.getContext(), Constant.KEY_ALIAS_AES)

        val encrypter = AESProcessor(Cipher.ENCRYPT_MODE, secretKey)


        val rawText = "sample-text".toByteArray()

        val encodedText = encrypter.process(rawText)

        val decryter = AESProcessor(Cipher.DECRYPT_MODE, secretKey, encrypter.cipher.iv)
        val decodedText = decryter.process(encodedText)

        Assert.assertEquals(String(rawText), String(decodedText))
    }
}