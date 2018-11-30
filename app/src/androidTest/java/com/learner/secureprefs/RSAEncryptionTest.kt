package com.learner.secureprefs

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Base64
import android.util.Log
import com.learner.secureprefs.security.impl.Constant
import com.learner.secureprefs.security.impl.aes.AESDecoder
import com.learner.secureprefs.security.impl.aes.AESEncoder
import com.learner.secureprefs.security.impl.keyprovider.KeyStorePrivateKeyProvider
import com.learner.secureprefs.security.impl.keyprovider.KeyStorePublicKeyProvider
import com.learner.secureprefs.security.impl.keyprovider.KeyStoreSecretKeyProvider
import com.learner.secureprefs.security.impl.rsa.RSAProcessor
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
    fun checkRSAKeyPairIntegrity() {

        val alias = "test-prefs"

        val encoder = RSAProcessor(Cipher.ENCRYPT_MODE, KeyStorePublicKeyProvider(InstrumentationRegistry.getContext()).getKey(alias))
        val decoder = RSAProcessor(Cipher.DECRYPT_MODE, KeyStorePrivateKeyProvider(InstrumentationRegistry.getContext()).getKey(alias))

        val rawText = "sample-text".toByteArray()

        Assert.assertArrayEquals(decoder.process(encoder.process(rawText)), rawText)
    }

    @Test
    fun checkAESIntegrity() {

        val secretKey = KeyStoreSecretKeyProvider().getKey(Constant.KEY_ALIAS_AES)

        val rawText = "sample-text".toByteArray()

        val encodedText = AESEncoder(secretKey).process(rawText)

        Log.e("AES", "Encoded Text Length: ${Base64.encodeToString(encodedText, Base64.NO_WRAP)}")

        val decryter = AESDecoder(secretKey)
        val decodedText = decryter.process(encodedText)

        Assert.assertEquals(String(rawText), String(decodedText))
    }
}