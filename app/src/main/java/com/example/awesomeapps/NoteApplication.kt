package com.example.awesomeapps

import android.app.Application

class NoteApplication: Application() {
    private val database by lazy { NoteDatabase.getDatabase(this) }
    val repository by lazy { NoteRepository(database.noteDao()) }
}