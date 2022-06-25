package com.example.gossip.appui.chatscreen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

@Composable
fun AssembleChatMessages(messageDetailsList: MutableList<MessageDetails>, messageScrollToPosition:LazyListState) {

    LazyColumn (state = messageScrollToPosition) {
        items(messageDetailsList) {
            messageDetails -> MessageUIAssembler(messageDetails = messageDetails)
        }
    }
}