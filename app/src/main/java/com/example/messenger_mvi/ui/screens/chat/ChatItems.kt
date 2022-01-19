package com.example.messenger_mvi.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messenger_mvi.ui.theme.ColorPrimaryDark

@Composable
fun AlienChatItem() {
    Row {
        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp)
                .size(48.dp)
                .clip(CircleShape)
                .border(1.dp, ColorPrimaryDark, CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SF",
                fontSize = 20.sp,
                color = ColorPrimaryDark,
                textAlign = TextAlign.Center
            )
        }
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp, 16.dp)
                .padding(end = 32.dp)
                .border(1.dp, ColorPrimaryDark, RoundedCornerShape(16.dp))
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                Text(
                    text = "Card View Card View Card View Card View Card View Card View Card View Card View Card ",
                    fontSize = 16.sp,
                    color = ColorPrimaryDark
                )
            }
        }
    }
}

@Composable
fun MyChatItem() {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(start = 48.dp, top = 8.dp, end = 8.dp)
                    .border(1.dp, Color.White, RoundedCornerShape(16.dp))
            ) {
                Box(
                    modifier = Modifier
                        .background(ColorPrimaryDark)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Card View Card View Card View Card View Card View Card View Card View Card View Card ",
                        fontSize = 16.sp,
                        color = Color.White,
                        textAlign = TextAlign.Start,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 8.dp, end = 8.dp)
                .size(48.dp)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
                .background(ColorPrimaryDark),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SF",
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun Preview() {
    LazyColumn(content = {
        item {
            MyChatItem()
            AlienChatItem()
        }
    })
}