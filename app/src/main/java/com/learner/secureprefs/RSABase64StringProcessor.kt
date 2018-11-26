package com.learner.secureprefs

import com.learner.secureprefs.converter.Base64Converter
import com.learner.secureprefs.converter.StringConverter
import java.security.KeyPair

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
class RSABase64StringProcessor(keyPair: KeyPair) : RSAProcessorWrapper<String, String>(keyPair, StringConverter(), Base64Converter())