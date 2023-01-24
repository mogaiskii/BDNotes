package com.example.awesomeapps

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: Flow<List<Note>> = noteDao.allNotes()

    fun getNote(id: UUID): Flow<Note> {
        return noteDao.getById(id)
    }

    @WorkerThread
    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    @WorkerThread
    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    @WorkerThread
    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}