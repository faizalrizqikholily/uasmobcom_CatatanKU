package com.uasmobcom.catatanku

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.uasmobcom.catatanku.data.NoteDatabase
import com.uasmobcom.catatanku.domain.Notes
import com.uasmobcom.catatanku.domain.NotificationScheduler
import com.uasmobcom.catatanku.presentation.AppNavigation
import com.uasmobcom.catatanku.presentation.viewmodel.NoteViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class NoteListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val database = Room.inMemoryDatabaseBuilder(appContext, NoteDatabase::class.java).build()
    private lateinit var viewModel: NoteViewModel
    private val testRepository = TestRepository(database.dao())
    private val notificationWorker = NotificationScheduler(appContext)

    private val list = listOf(
        Notes(
            id = 1234L,
            title = "Test",
            note = "Test Note",
            date = LocalDate.now().toString(),
            isChecked = false
        ),
        Notes(
            id = 1534L,
            title = "Test",
            note = "Test Note",
            date = LocalDate.now().toString(),
            isChecked = false
        ),
        Notes(
            id = 1634L,
            title = "Test",
            note = "Test Note",
            date = LocalDate.now().toString(),
            isChecked = false
        )
    )

    @Before
    fun setup() {
        viewModel = NoteViewModel(testRepository)
        list.forEach { viewModel.insert(it) }
        viewModel.notesList()
    }

    @Before
    fun setUp() {
        composeTestRule.activity.setContent {
            AppNavigation(
                viewModel = viewModel,
                scheduleNotification = notificationWorker::scheduleNotification
            )
        }
    }

    @Test
    fun test1() {
        Page.on<NoteListScreen>(composeTestRule)
            .verify(composeTestRule)
            .isDisplayed(composeTestRule, "Floating Action Button")
    }

    @Test
    fun test_list_card_titles() {
        Page.on<NoteListScreen>(composeTestRule)
            .verify(composeTestRule)
            .click(composeTestRule, "List Card 0")
            .click(composeTestRule, "Back Button")
            .verify(composeTestRule)
    }
}