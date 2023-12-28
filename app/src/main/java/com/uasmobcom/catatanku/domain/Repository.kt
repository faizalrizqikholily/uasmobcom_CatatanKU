package com.uasmobcom.catatanku.domain

import com.uasmobcom.catatanku.data.Dao
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getDao(): Dao
    fun getAllNotes(): Flow<List<Notes>>
    fun getNoteById(noteId: Long?): Flow<Notes?>
    fun deleteAllNotes()
    suspend fun update(note: Notes)
    suspend fun insert(note: Notes)
    suspend fun delete(note: Notes)
}