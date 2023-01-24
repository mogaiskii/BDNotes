package com.example.awesomeapps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.UUID

class NoteViewModel(private val repository: NoteRepository): ViewModel() {
    var notes: LiveData<List<Note>> = repository.allNotes.asLiveData()

    fun noteById(id: String): LiveData<Note> {
        return repository.getNote(UUID.fromString(id)).asLiveData()
    }

    fun addNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
    }
}

class NoteModelFactory(private val repository: NoteRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T & Any {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(repository) as (T & Any)
        }
        throw IllegalArgumentException("Unknown Class for View Model")
    }
}
