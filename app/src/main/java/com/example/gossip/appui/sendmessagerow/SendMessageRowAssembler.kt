package com.example.gossip.appui.sendmessagerow

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gossip.appui.chatscreen.MessageDetails
import kotlinx.coroutines.CoroutineScope

@Composable
fun AssembleSendMessageRow(
    messageDetailsList: MutableList<MessageDetails>,
    messageScrollToPosition: LazyListState,
    scope: CoroutineScope
) {
    Row (modifier = Modifier.height(IntrinsicSize.Min).padding(6.dp), verticalAlignment = Alignment.Bottom) {
        SendPicButton()
        SendMessageTextButton(
            modifier = Modifier.weight(1f),
            messageDetailsList = messageDetailsList,
            listScrollStateHolder = messageScrollToPosition,
            scope = scope)
    }
}