package com.uasmobcom.catatanku

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.uasmobcom.catatanku.data.NoteDatabase
import com.uasmobcom.catatanku.presentation.viewmodel.NoteViewModel
import org.junit.Before
import org.junit.Test

class ViewModelTests {

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val database = Room.inMemoryDatabaseBuilder(appContext, NoteDatabase::class.java).build()
    private lateinit var viewModel: NoteViewModel
    private val testRepository = TestRepository(database.dao())

    @Before
    fun setup() {
        viewModel = NoteViewModel(testRepository)
    }

    @Test
    fun test_custom_decode() {
        val encodedText = "This%20is%20a%20test"
        val decodedText = viewModel.customDecode(encodedText)
        assert(decodedText == "This is a test")

        val encodedLetter = "%48"
        val decodedLetter = viewModel.customDecode(encodedLetter)
        assert(decodedLetter == "H")

        val blankText = ""
        val decodeBlankText = viewModel.customDecode(blankText)
        assert(decodeBlankText == "")
    }
}