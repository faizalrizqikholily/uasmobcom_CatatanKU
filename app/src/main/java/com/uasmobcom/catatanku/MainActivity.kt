package com.uasmobcom.catatanku

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.uasmobcom.catatanku.data.NoteDatabase
import com.uasmobcom.catatanku.data.RepositoryImpl
import com.uasmobcom.catatanku.domain.NotificationScheduler
import com.uasmobcom.catatanku.presentation.AppNavigation
import com.uasmobcom.catatanku.presentation.viewmodel.NoteViewModel
import com.uasmobcom.catatanku.ui.theme.RoomTheme

/*
 *  Todo:
 *   - Actions Menu:
 *      - Sort list by date or title
 *   - Icon buttons for each card to delete the note -- provide warning dialog
 *   - Set reminder for task button in details page?
 *
 */

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = NoteDatabase.getDatabase(this)
        val repository = RepositoryImpl(database.dao())
        val factory = NoteViewModel.Factory(repository)
        val viewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]
        viewModel.notesList()
        val notificationWorker = NotificationScheduler(this)

        setContent {
            RoomTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var deleteSelectedAlertExpanded by remember { mutableStateOf(false) }
                    var deleteAllAlertExpanded by remember { mutableStateOf(false) }
                    var version by remember { mutableStateOf(false)}


                    Scaffold(
                        topBar = {
                            // Move this to its own file
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "CatatanKU!",
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 25.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                colors = TopAppBarDefaults.smallTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.background
                                ),
                                actions = {
                                    var expandedOptionsMenu by remember {
                                        mutableStateOf(false)
                                    }
                                    IconButton(onClick = {
                                        expandedOptionsMenu = !expandedOptionsMenu
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = expandedOptionsMenu,
                                        onDismissRequest = { expandedOptionsMenu = false },
                                        modifier = Modifier
                                            .semantics { testTag = "Options Menu Drop Down" },
                                        offset = DpOffset(0.dp, 12.dp),
                                        content = {
                                            DropdownMenuItem(
                                                text = { Text(text = "Hapus Semua") },
                                                modifier = Modifier
                                                    .semantics { testTag = "Delete All button" },
                                                onClick = {
                                                    deleteAllAlertExpanded = !deleteAllAlertExpanded
                                                },
                                                colors = MenuDefaults.itemColors(
                                                    textColor = MaterialTheme.colorScheme.tertiary
                                                )
                                            )
                                            DropdownMenuItem(
                                                text = { Text(text = "Hapus yang dipilih") },
                                                modifier = Modifier
                                                    .semantics { testTag = "Delete Selected button" },
                                                onClick = {
                                                    if (viewModel.selectedNotes.isNotEmpty()) {
                                                        deleteSelectedAlertExpanded = !deleteSelectedAlertExpanded
                                                    }
                                                },
                                                colors = MenuDefaults.itemColors(
                                                    textColor = MaterialTheme.colorScheme.tertiary
                                                )
                                            )
                                            DropdownMenuItem(
                                                text = { Text(text = "Version") },
                                                modifier = Modifier
                                                    .semantics { testTag = "Oke" },
                                                onClick = {
                                                    version = !version
                                                },
                                                colors = MenuDefaults.itemColors(
                                                    textColor = MaterialTheme.colorScheme.tertiary
                                                )
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    ) {

                        Box(
                            modifier = Modifier.padding(it)
                        ){
                            AppNavigation(
                                viewModel = viewModel,
                                notificationWorker::scheduleNotification
                            )

                        }

                        if (deleteSelectedAlertExpanded) {
                            AlertDialog(
                                onDismissRequest = { deleteSelectedAlertExpanded = !deleteSelectedAlertExpanded },
                                title = {
                                    Text("Hapus yang dipilih")
                                },
                                text = {
                                    Text("Apa kamu yakin untuk menghapus catatan yand dipilih?")
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            viewModel.deleteSelectedNotes()
                                            deleteSelectedAlertExpanded = false
                                        }
                                    ) {
                                        Text("Hapus")
                                    }
                                },
                                dismissButton = {
                                    Button(
                                        onClick = {
                                            deleteSelectedAlertExpanded = false
                                        }
                                    ) {
                                        Text("Batal")
                                    }
                                }
                            )
                        }

                        if (deleteAllAlertExpanded) {
                            AlertDialog(
                                onDismissRequest = { deleteAllAlertExpanded = !deleteAllAlertExpanded },
                                title = {
                                    Text("Hapus Semua Catatan")
                                },
                                text = {
                                    Text("Apa kamu yakin untuk menghapus semua catatan?")
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            viewModel.deleteAllNotes()
                                            deleteAllAlertExpanded = false
                                        }
                                    ) {
                                        Text("Hapus semua")
                                    }
                                },
                                dismissButton = {
                                    Button(
                                        onClick = {
                                            deleteAllAlertExpanded = false
                                        }
                                    ) {
                                        Text("Batal")
                                    }
                                }
                            )
                        }
                        if (version) {
                            AlertDialog(
                                onDismissRequest = { version = !version },
                                title = {
                                    Text("Hello Semuanya!!")
                                },
                                text = {
                                    Text("Nama saya Faizal Rizqi Kholily - NIM 1313621029")
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            version = false
                                        }
                                    ) {
                                        Text("Oke")
                                    }
                                },

                            )
                        }

                    }
                }
            }
        }
    }
}

