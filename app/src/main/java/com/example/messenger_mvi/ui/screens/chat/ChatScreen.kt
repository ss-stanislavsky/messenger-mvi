package com.example.messenger_mvi.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messenger_mvi.R
import com.example.messenger_mvi.models.MessageUI
import com.example.messenger_mvi.ui.theme.ColorPrimaryLight
import com.example.messenger_mvi.ui.theme.MessengerMVITheme

@Composable
fun ChatScreen(
    finish: (() -> Unit)?,
//    chatViewModel: ChatViewModel = viewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBar() },
    ) {
        Column(
            modifier = Modifier
                .background(ColorPrimaryLight)
                .fillMaxSize(),
        ) {
            Box(modifier = Modifier.weight(1f)) {
                MessageList()
            }
            Footer(false)
        }
    }
}

@Composable
fun AppBar() {
    TopAppBar {
        IconButton(onClick = { }) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(id = R.string.search))
        }
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
        )
        IconButton(onClick = { }) {
            Icon(imageVector = Icons.Filled.Share, contentDescription = stringResource(id = R.string.share))
        }
    }
}

@Composable
fun MessageList(data: List<MessageUI> = emptyList()) {
    Box(Modifier.fillMaxSize()) {
        if (data.isNotEmpty()) {
            LazyColumn(
                content = {
                    itemsIndexed(
                        listOf("First", "Second", "Third", "Fourth", "Fifth", "Sixth", "", "", "", "")
                    ) { index, item ->
                        if (index % 3 == 0) {
                            MyChatItem()
                        } else {
                            AlienChatItem()
                        }
                    }
                })
        } else {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun Footer(sending: Boolean) {
    val message = remember { mutableStateOf("") }
    Row(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = message.value,
            onValueChange = { message.value = it },
            textStyle = TextStyle(fontSize = 16.sp),
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = stringResource(id = R.string.enter_message)) },
            maxLines = 5,
            shape = RoundedCornerShape(16.dp)
        )
        Box {
            if (sending) {
                CircularProgressIndicator(modifier = Modifier.padding(start = 4.dp, top = 4.dp))
            } else {
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.Send, contentDescription = "")
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DefaultPreview() {
    MessengerMVITheme {
        ChatScreen(null)
    }
}