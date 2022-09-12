package com.example.ttssample

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
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
import com.example.ttssample.ui.theme.TTSSampleTheme
import java.util.*

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this)

        setContent {
            TTSSampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppScreen(tts!!)
                }
            }
        }
    }

    override fun onInit(p0: Int) {
        if (p0 == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","Language not supported!")
            }
        }
    }

    override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}

@Composable
fun AppScreen(tts: TextToSpeech) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    AppContent(
        text = text,
        onTextChange = { text = it},
        onClearText = { text = TextFieldValue("")},
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
            placeholder = { Text(stringResource(R.string.placeholder_user_input))}
        )
    }
}

fun speakOut(text: String, tts: TextToSpeech){
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
}
