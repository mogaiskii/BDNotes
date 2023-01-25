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

    private fun setPassword(activity: Activity, code: String) {
        val editor = applicationContext.getSharedPreferences("password", Context.MODE_PRIVATE).edit()
        editor.putString("password", code)
        editor.apply()
    }
    private fun getPassword(activity: Activity): String? {
        val pref = applicationContext.getSharedPreferences("password", Context.MODE_PRIVATE)
        return pref.getString("password", "none")
    }

    public fun isAuthorized(activity: Activity): Boolean {
        val password = getPassword(activity)
        if (password == "none") return true
        if (lastAuth == null) return false
        val now = Date()
        if ((now.time - lastAuth!!.time) > 1000*60*5) return false
        return true
    }

    fun passPin(activity: Activity, code: String): Boolean {
        Log.d("CODE", code)
        val password = getPassword(activity)
        if (password == "none" || code == password) {
            lastAuth = Date()
            return true
        }
        return false
    }

    fun savePin(activity: Activity, code: String): Boolean {
        Log.d("CODE", code)
        setPassword(activity, code)
        return true
    }
}