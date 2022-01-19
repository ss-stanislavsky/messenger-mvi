package com.example.messenger_mvi.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.messenger_mvi.business.repository.MessageRepository
import com.example.messenger_mvi.database.MessengerDatabase
import com.example.messenger_mvi.ui.screens.chat.ChatScreen
import com.example.messenger_mvi.ui.screens.chat.ChatViewModel
import com.example.messenger_mvi.ui.theme.MessengerMVITheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val chatViewModel: ChatViewModel by viewModels()
    @Inject protected lateinit var database: MessengerDatabase
    @Inject protected lateinit var repository: MessageRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessengerMVITheme {
                ChatScreen(::finish)
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
