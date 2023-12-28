package com.uasmobcom.catatanku.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.uasmobcom.catatanku.domain.Notes
import java.time.LocalDate
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScreen(
    insert: (item: Notes) -> Unit,
    scheduleNotification: (noteId: Long, title: String, note: String) -> Unit,
    navController: NavController
) {

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}
    val titleTextFieldValue = remember { mutableStateOf(TextFieldValue()) }
    val noteTextFieldValue = remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current

    val currentDate = LocalDate.now()
    val note = Notes(
        title = titleTextFieldValue.value.text,
        note = noteTextFieldValue.value.text,
        date = currentDate.toString()
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(25.dp)) {

        Text(text = "Judul")
        OutlinedTextField(
            value = titleTextFieldValue.value,
            onValueChange = { titleTextFieldValue.value = it },
            label = { Text("Judul....") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Catatan")
        OutlinedTextField(
            value = noteTextFieldValue.value,
            onValueChange = { noteTextFieldValue.value = it },
            label = { Text("Masukan Catatan....") },
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                insert(note)
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    scheduleNotification(Random.nextLong(), note.title, note.note)
                }
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambahkan Catatan")
        }
    }

    LaunchedEffect(launcher) {
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        ) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}