package com.learner.secureprefs.security.impl

import com.learner.secureprefs.security.converter.Base64Converter
import com.learner.secureprefs.security.converter.StringConverter
import java.security.KeyPair

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
class RSABase64StringEncoderDecoder(keyPair: KeyPair) : RSAEncoderDecoder<String, String>(keyPair, StringConverter(), Base64Converter())