package com.uasmobcom.catatanku.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.uasmobcom.catatanku.domain.Notes
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Notes>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    fun getAllNoteById(noteId: Long?): Flow<Notes?>

    @Query("DELETE FROM notes")
    fun deleteAllNotes()
    
    @Insert
    suspend fun insert(note: Notes)

    @Delete
    suspend fun delete(note: Notes)

    @Update
    suspend fun update(note: Notes)
}