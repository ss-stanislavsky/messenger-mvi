package com.example.messenger_mvi.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.messenger_mvi.R
import com.example.messenger_mvi.business.model.chat.ChatState
import com.example.messenger_mvi.ui.models.MessageUI
import com.example.messenger_mvi.ui.theme.ColorPrimaryLight
import com.example.messenger_mvi.ui.theme.MessengerMVITheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = viewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val state = viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBar(scope, scaffoldState) },
    ) {
//        Column(
//            modifier = Modifier
//                .background(ColorPrimaryLight)
//                .fillMaxSize(),
//        ) {

        when (val currentState = state.value) {
            is ChatState.Data -> {
                MessageList(currentState.data, currentState.message, listState = listState)
                scrollList(listState,scope, state.value.data.size)
//                Footer(sending = false)
            }
            is ChatState.Error -> {
                MessageList(currentState.data, currentState.message, listState = listState)
                showError(scaffoldState, scope, currentState.errorMessage, stringResource(id = android.R.string.ok))
//                Footer(sending = false)
            }
            is ChatState.Loading -> {
                MessageList(currentState.data, currentState.message, isLoading = true, listState = listState)
//                Footer(sending = false)
            }
            is ChatState.Sending -> {
                MessageList(data = currentState.data, currentState.message, isSending = true, listState = listState)
            }
//                Footer(sending = true)
        }

//        MessageList(listState = listState, data = state.value.data, message = state.value.message)

//            Box(modifier = Modifier.weight(1f)) {
//                MessageList()
//        when (val currentState = state.value) {
//            is ChatState.Data -> MessageList(currentState.data)
//            is ChatState.Loading -> MessageList()
    }
//            }
//            Footer(false)
//        }
//    }
}

@Composable
fun AppBar(scope: CoroutineScope, scaffoldState: ScaffoldState) {
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
        IconButton(onClick = { showError(scaffoldState, scope, "Error", "Ok") }) {
            Icon(imageVector = Icons.Filled.Share, contentDescription = stringResource(id = R.string.share))
        }
    }
}

@Composable
fun MessageList(
    data: List<MessageUI> = emptyList(),
    message: String = "",
    isLoading: Boolean = false,
    isSending: Boolean = false,
    listState: LazyListState,
) {
    Column(
        modifier = Modifier
            .background(ColorPrimaryLight)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            if (!isLoading) {
                if (data.isNotEmpty()) {
                    LazyColumn(state = listState,
                        content = {
                            data.forEach {
                                if (it.isMyOwn) {
                                    item { MyChatItem(it) }
                                } else {
                                    item { AlienChatItem(it) }
                                }
                            }
                        })
                } else {
                    NoContent()
                }
            } else {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        Footer(isSending, message)
    }
}

@Composable
fun NoContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            imageVector = Icons.Outlined.Create,
            contentDescription = "",
            modifier = Modifier.size(32.dp),
        )
        Text(text = stringResource(id = R.string.start_chating), fontSize = 18.sp, modifier = Modifier.padding(24.dp))
    }
}

@Composable
fun Footer(sending: Boolean, message: String, viewModel: ChatViewModel = viewModel()) {
    val msg = remember { mutableStateOf(message) }
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = msg.value,
            onValueChange = { msg.value = it },
            textStyle = TextStyle(fontSize = 16.sp),
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = stringResource(id = R.string.enter_message)) },
            maxLines = 5,
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions { viewModel.sendMessage(msg.value) },
        )
        Box {
            if (sending) {
                CircularProgressIndicator(modifier = Modifier.padding(start = 4.dp, top = 4.dp))
            } else {
                IconButton(onClick = { viewModel.sendMessage(msg.value) }) {
                    Icon(Icons.Filled.Send, contentDescription = "")
                }
            }
        }
    }
}

fun showError(scaffoldState: ScaffoldState, scope: CoroutineScope, errorMessage: String, label: String) {
    scope.launch { scaffoldState.snackbarHostState.showSnackbar(errorMessage, label) }
}

fun scrollList(listState: LazyListState, scope: CoroutineScope, position: Int) {
    scope.launch { listState.animateScrollToItem(position) }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DefaultPreview() {
    MessengerMVITheme {
        ChatScreen()
    }
}