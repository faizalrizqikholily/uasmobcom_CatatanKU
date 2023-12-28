package com.uasmobcom.catatanku.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uasmobcom.catatanku.domain.Notes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    note: Notes?,
    details: String?,
    title: String?,
    update: (note: Notes) -> Unit,
    customDecode: (text: String) -> String,
    navController: NavController
) {

    val decodedTitle = title?.let { customDecode(it) }
    val decodedNote = details?.let { customDecode(it) }

    var newTitleText by remember {
        mutableStateOf(decodedTitle ?: "")
    }

    var newDetailsText by remember {
        mutableStateOf(decodedNote ?: "")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 25.dp,
                    top = 10.dp,
                    bottom = 15.dp
                )
                .align(Alignment.Start)
        ) {
            Text(text = "Ubah Judul", fontWeight = FontWeight.Bold)
        }

        TextField(
            value = newTitleText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 25.dp,
                    bottom = 25.dp,
                    end = 25.dp
                ),
            onValueChange = { newTitleText = it }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 25.dp,
                    top = 10.dp,
                    bottom = 15.dp
                )
                .align(Alignment.Start)
        ) {
            Text(text = "Ubah Catatan:", fontWeight = FontWeight.Bold)
        }

        TextField(
            value = newDetailsText,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(
                    start = 25.dp,
                    bottom = 25.dp,
                    end = 25.dp
                ),
            onValueChange = { newDetailsText = it }
        )


        Button(onClick = {
            note?.let {
                val updatedNote = note.copy(title = newTitleText, note = newDetailsText)
                update(updatedNote)
                navController.popBackStack()
            }
        }) {
            Text(text = "Perbarui")
        }

        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.testTag("Back Button")
        ) {
            Text(text = "Kembali")
        }
    }
}
