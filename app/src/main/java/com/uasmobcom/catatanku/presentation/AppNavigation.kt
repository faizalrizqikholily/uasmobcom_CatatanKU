package com.uasmobcom.catatanku.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uasmobcom.catatanku.presentation.screen.CreateNoteScreen
import com.uasmobcom.catatanku.presentation.screen.DetailsScreen
import com.uasmobcom.catatanku.presentation.screen.NoteListScreen
import com.uasmobcom.catatanku.presentation.viewmodel.NoteViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    viewModel: NoteViewModel,
    scheduleNotification: (noteId: Long, title: String, note: String) -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "notes-screen"
    ) {

        composable("notes-screen") {

            val list = viewModel.state.collectAsState().value.list
            val selectedNotes = viewModel.selectedNotes

            NoteListScreen(
                selectedNotes = selectedNotes,
                toggleNoteSelection = viewModel::toggleNoteSelection,
                navController = navController,
                list = list
            )
        }

        composable("create-note-screen") {
            CreateNoteScreen(
                insert = viewModel::insert,
                scheduleNotification = scheduleNotification,
                navController = navController
            )
        }

        composable("details-screen/{noteId}/{title}/{details}",
            arguments = listOf(
                navArgument("noteId") { type = NavType.LongType },
                navArgument("details") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType}
            )
        ) { backStackEntry ->

            val noteId = backStackEntry.arguments?.getLong("noteId")
            val title = backStackEntry.arguments?.getString("title")
            val details = backStackEntry.arguments?.getString("details")
            val note by viewModel.getNoteById(noteId).collectAsState(initial = null)

            DetailsScreen(
                note = note,
                title = title,
                details = details,
                customDecode = viewModel::customDecode,
                update = viewModel::update,
                navController = navController
            )
        }
    }
}