package com.learner.secureprefs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SecureSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = SecureSharedPreferences(this)
    }

    fun onSaveClicked(view: View) {
        prefs.edit().putString(et_key.text.toString(), et_value.text.toString()).commit()
    }

    fun onRetrieveClicked(view: View) {
        et_value.setText(prefs.getString(et_key.text.toString(), "not found"))
    }
}
