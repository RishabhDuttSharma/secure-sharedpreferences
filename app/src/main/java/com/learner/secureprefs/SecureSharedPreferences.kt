package com.learner.secureprefs

import android.content.Context
import android.content.SharedPreferences

/**
 * Developer: Rishabh Dutt Sharma
 * Dated: 11/27/2018
 */
class SecureSharedPreferences(private val sharedPreferences: SharedPreferences) : SharedPreferences {

    constructor(context: Context, name: String = "secure-prefs", mode: Int = Context.MODE_PRIVATE) : this(context.getSharedPreferences(name, mode))

    override fun contains(key: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInt(key: String?, defValue: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAll(): MutableMap<String, *> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun edit(): SharedPreferences.Editor {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLong(key: String?, defValue: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getString(key: String?, defValue: String?): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}