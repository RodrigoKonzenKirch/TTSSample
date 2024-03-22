package com.example.ttssample

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun AppScreen(tts: TextToSpeech) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    AppContent(
        text = text,
        onTextChange = { text = it},
        onClearText = { text = TextFieldValue("") },
        onButtonClick = { speakOut(text.text, tts)}
    )
}

@Composable
fun AppContent(
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    onClearText: () -> Unit,
    onButtonClick: () -> Unit
){
    Column {
        Button(
            enabled = text.text.isNotBlank(),
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        ) {
            Text(stringResource(R.string.button_read))
        }

        TextField(
            value = text,
            trailingIcon = {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "Clear Text",
                    modifier = Modifier.clickable (onClick = onClearText)
                )
            },
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            placeholder = { Text(stringResource(R.string.placeholder_user_input)) }
        )
    }
}

fun speakOut(text: String, tts: TextToSpeech){
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
}