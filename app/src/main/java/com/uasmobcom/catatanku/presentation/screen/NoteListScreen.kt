package com.uasmobcom.catatanku.presentation.screen

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.uasmobcom.catatanku.domain.Notes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteListScreen(
    selectedNotes: List<Notes>,
    toggleNoteSelection: (item: Notes, checked: Boolean) -> Unit,
    navController: NavController,
    list: List<Notes>?
) {

    Box(
        modifier = Modifier
            .semantics { testTag = "Note List Screen" }
            .fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            val reversedList = list?.reversed()
            reversedList?.let { list ->
                itemsIndexed(list) { index, item ->

                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .semantics { testTag = "List Card $index" }
                            .clickable {
                                val encodedTitle = Uri.encode(item.title)
                                var encodedNote = Uri.encode(item.note)
                                if (encodedNote.isBlank()) {
                                    encodedNote = "Empty"
                                }

                                navController.navigate(
                                    "details-screen/${item.id}/${encodedTitle}/${encodedNote}"
                                )
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = item.title,
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .semantics { testTag = "Title $index" },
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                                    Checkbox(
                                        modifier = Modifier
                                            .padding(end = 5.dp)
                                            .semantics { testTag = "Checkbox $index" },
                                        checked = selectedNotes.contains(item),
                                        onCheckedChange = { checked ->
                                            toggleNoteSelection(item, checked)
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.padding(bottom = 15.dp))
                            Text(
                                text = item.note,
                                modifier = Modifier
                                    .sizeIn(maxHeight = 300.dp)
                                    .semantics { testTag = "Note $index" },
                                fontSize = 18.sp,
                                lineHeight = 25.sp,
                                style = MaterialTheme.typography.bodySmall,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.padding(top = 15.dp))

                            Text(
                                text = item.date,
                                modifier = Modifier.testTag("Date $index")
                            )
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomEnd)) {
            FloatingActionButton(
                onClick = { navController.navigate("create-note-screen") },
                modifier = Modifier
                    .semantics { testTag = "Floating Action Button" }
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Go to Create Note Screen"
                )
            }
        }
    }
}

