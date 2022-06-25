package com.example.gossip.appui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.gossip.appui.chatscreen.AssembleChatMessages
import com.example.gossip.appui.navigationdrawer.AssembledDrawer
import com.example.gossip.appui.sendmessagerow.AssembleSendMessageRow
import com.example.gossip.appui.topappbar.AssembleTopBar

@Composable
private fun GetChatScreen() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val messagesDetailsList = remember {
        DemoData.getMessages().toMutableStateList()
    }

    val messageScrolledToPosition = rememberLazyListState(initialFirstVisibleItemIndex = messagesDetailsList.size - 1)

    Scaffold (
        scaffoldState = scaffoldState,
        drawerContent = {
            AssembledDrawer(
                headerData = DemoData.getDrawerHeaderData(),
                bodyData = DemoData.getDrawerBodyData(),
                scope = scope, scaffoldState = scaffoldState,
                navController = navController
            )
        },
        topBar = {
            AssembleTopBar(
                scaffoldState = scaffoldState,
                topBarData = DemoData.getTopAppBarData(),
                scope = scope
            )
        }
    ) {
        Column {
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxWidth(1f)
                .background(MaterialTheme.colors.background),
                contentAlignment = Alignment.TopCenter) {
                AssembleChatMessages(messageDetailsList = messagesDetailsList, messageScrollToPosition = messageScrolledToPosition)
            }
            AssembleSendMessageRow(messageDetailsList = messagesDetailsList, messageScrollToPosition = messageScrolledToPosition, scope = scope)
        }
    }
}

@Composable
fun GetCurrentScreen() {
    GetChatScreen()
}