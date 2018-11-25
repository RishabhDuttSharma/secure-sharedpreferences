package com.learner.secureprefs

import java.security.Key

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/25/2018
 */
class RSABase64StringProcessor(securityKey: Key) : RSAProcessor<String, String>(securityKey, StringConverter(), Base64Converter())