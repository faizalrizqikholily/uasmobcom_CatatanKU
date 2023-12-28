package com.uasmobcom.catatanku.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uasmobcom.catatanku.domain.Notes
import com.uasmobcom.catatanku.domain.Repository
import com.uasmobcom.catatanku.presentation.state.NoteListScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: Repository): ViewModel() {

    private val _state = MutableStateFlow(NoteListScreenState())
    val state = _state.asStateFlow()

    private val _notes = mutableStateListOf<Notes>()
    val notes: List<Notes> get() = _notes

    private val _selectedNotes = mutableStateListOf<Notes>()
    val selectedNotes: List<Notes> get() = _selectedNotes

    val list = state.value.list.toMutableStateList()

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repo: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NoteViewModel(repo) as T
        }
    }

    fun toggleNoteSelection(note: Notes, checked: Boolean) {
        if (checked) {
            _selectedNotes.add(note)
        } else {
            _selectedNotes.remove(note)
        }
    }

    fun deleteSelectedNotes() {
        _selectedNotes.forEach {
            delete(it)
        }
        _notes.removeAll(_selectedNotes)
        _selectedNotes.clear()
    }

    private fun getAllNotes(): Flow<List<Notes>> {
        return repository.getAllNotes()
    }

    fun notesList() = viewModelScope.launch(Dispatchers.IO) {
        getAllNotes().collect {
            _state.value = NoteListScreenState(list = it)
        }
    }

    fun getNoteById(id: Long?): Flow<Notes?> {
        return repository.getNoteById(id)
    }

    fun deleteAllNotes() = viewModelScope.launch(Dispatchers.IO) {
        repository.getDao().deleteAllNotes()
    }

    fun insert(note: Notes) = viewModelScope.launch(Dispatchers.IO) {
        repository.getDao().insert(note)
    }

    fun delete(note: Notes) = viewModelScope.launch(Dispatchers.IO) {
        repository.getDao().delete(note)
    }

    fun update(note: Notes) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }

    fun customDecode(encodedText: String): String {
        val regex = "%([0-9A-Fa-f]{2})".toRegex()
        return regex.replace(encodedText) { matchResult ->
            val hexValue = matchResult.groupValues[1]
            val decodedChar = hexValue.toInt(16).toChar()
            decodedChar.toString()
        }
    }
}