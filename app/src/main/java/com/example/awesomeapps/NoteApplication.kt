package com.example.awesomeapps

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.util.*

class NoteApplication: Application() {
    private val database by lazy { NoteDatabase.getDatabase(this) }
    val repository by lazy { NoteRepository(database.noteDao()) }

    private var lastAuth: Date? = null

    private val NONE_PASSWORD = "none"
    private val PASSWORD_PREF = "password"

    private fun setPassword(activity: Activity, code: String) {
        val editor = applicationContext.getSharedPreferences(PASSWORD_PREF, Context.MODE_PRIVATE).edit()
        editor.putString(PASSWORD_PREF, code)
        editor.apply()
    }
    private fun getPassword(activity: Activity): String? {
        val pref = applicationContext.getSharedPreferences(PASSWORD_PREF, Context.MODE_PRIVATE)
        return pref.getString(PASSWORD_PREF, NONE_PASSWORD)
    }

    public fun isAuthorized(activity: Activity): Boolean {
        val password = getPassword(activity)
        if (password == NONE_PASSWORD) return true
        if (lastAuth == null) return false
        val now = Date()
        if ((now.time - lastAuth!!.time) > 1000*60*5) return false
        return true
    }

    public fun isPasswordSet(activity: Activity): Boolean {
        return getPassword(activity) != NONE_PASSWORD
    }

    fun passPin(activity: Activity, code: String): Boolean {
        val password = getPassword(activity)
        if (password == NONE_PASSWORD || code == password) {
            lastAuth = Date()
            return true
        }
        return false
    }

    fun savePin(activity: Activity, code: String): Boolean {
        setPassword(activity, code)
        lastAuth = Date()
        return true
    }

    fun removePin(activity: ListActivity) {  // parameter is just a guard kinda
        setPassword(activity, NONE_PASSWORD)
    }
}