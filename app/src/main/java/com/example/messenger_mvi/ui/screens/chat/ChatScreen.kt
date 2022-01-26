package com.example.messenger_mvi.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.messenger_mvi.R
import com.example.messenger_mvi.business.model.chat.ChatAction
import com.example.messenger_mvi.ui.models.MessageUI
import com.example.messenger_mvi.ui.theme.ColorPrimaryLight
import com.example.messenger_mvi.ui.theme.MessengerMVITheme
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.rememberImeNestedScrollConnection
import com.google.accompanist.insets.rememberInsetsPaddingValues
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = viewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val messageState = remember { mutableStateOf("") }

    val state = viewModel.state.collectAsState()
    val action = viewModel.action

//    val insets = LocalWindowInsets.current
//    val imeBottom = with(LocalDensity.current) { insets.ime.bottom.toDp() }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBar(scope, scaffoldState) },
        bottomBar = {
            Surface(elevation = 1.dp, color = ColorPrimaryLight) {
                Footer(
                    isSending = state.value.isSending,
                    messageState = messageState.apply { value = state.value.message })
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { contentPadding ->

        with(state.value) {
            MessageList(
                data = data,
                isLoading = isLoading,
                listState = listState,
                contentPadding = contentPadding
            )
        }

        LaunchedEffect(key1 = Unit) {
            action.collectLatest {
                when (it) {
                    ChatAction.Empty -> {}
                    is ChatAction.Error -> showError(scaffoldState, scope, it.errorMessage, it.errorLabel)
//                    is ChatAction.Scroll -> scrollList(listState, scope, it.position)
                }
            }
        }
    }
}

@Composable
fun AppBar(scope: CoroutineScope, scaffoldState: ScaffoldState) {
    TopAppBar(
        contentPadding = rememberInsetsPaddingValues(
            LocalWindowInsets.current.statusBars,
            applyBottom = false,
        )
    ) {
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

@OptIn(ExperimentalAnimatedInsets::class)
@Composable
fun MessageList(
    data: List<MessageUI> = emptyList(),
    isLoading: Boolean = false,
    listState: LazyListState,
    contentPadding: PaddingValues,
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
                    LazyColumn(
                        contentPadding = contentPadding,
                        reverseLayout = true,
                        modifier = Modifier
                            .nestedScroll(connection = rememberImeNestedScrollConnection()),
                        state = listState,
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
//        Footer(isSending, messageState)
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
fun Footer(isSending: Boolean, messageState: MutableState<String>, viewModel: ChatViewModel = viewModel()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp)
            .navigationBarsWithImePadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageState.value,
            onValueChange = { messageState.value = it },
            textStyle = TextStyle(fontSize = 16.sp),
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = stringResource(id = R.string.enter_message)) },
            // TODO: decide how to increase maxLines
            maxLines = 1,
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions { viewModel.sendMessage(messageState.value) },
        )
        Box {
            if (isSending) {
                CircularProgressIndicator(modifier = Modifier.padding(start = 8.dp, top = 4.dp))
            } else {
                IconButton(onClick = { viewModel.sendMessage(messageState.value) }) {
                    Icon(Icons.Filled.Send, contentDescription = "", modifier = Modifier.padding(start = 8.dp))
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