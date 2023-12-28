package com.uasmobcom.catatanku.presentation.state

import com.uasmobcom.catatanku.domain.Notes

data class NoteListScreenState(var list: List<Notes> = emptyList())