package com.learner.secureprefs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RSABase64StringProcessor(RSAKeyStoreHelper.getKeyPair(this, "prefs"))
    }
}
