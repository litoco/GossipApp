package com.example.gossip.appui.sendmessagerow

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gossip.R
import com.example.gossip.appui.chatscreen.MessageDetails
import com.example.gossip.appui.chatscreen.SentMessageDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun SendMessageTextButton(
    modifier: Modifier,
    messageDetailsList: MutableList<MessageDetails>,
    messageScrollToPosition: LazyListState,
    scope: CoroutineScope
) {

    var text by remember{ mutableStateOf("") }
    val scrollState = rememberScrollState()

    val trailingIconView = @Composable {
        IconButton(onClick = {
            val username = "You"
            val sentReceiveTime = Calendar.getInstance().timeInMillis
            val currentMessageDetails = SentMessageDetails(username = username, sentReceiveTimeInMilliSec = sentReceiveTime, messageString = text)
            messageDetailsList.add(currentMessageDetails)
            scope.launch {
                messageScrollToPosition.animateScrollToItem(messageDetailsList.size-1)
            }
            text = ""
        }) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_send), contentDescription = "Send Button",
                tint = MaterialTheme.colors.onPrimary)
        }
    }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colors.secondary,
        backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.4f)
    )

    CompositionLocalProvider(values = arrayOf(LocalTextSelectionColors provides customTextSelectionColors)) {
        TextField(modifier = modifier
            .verticalScroll(state = scrollState, enabled = true)
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colors.onPrimary,
                shape = RoundedCornerShape(corner = CornerSize(6.dp))
            ),
            shape = RoundedCornerShape(corner = CornerSize(6.dp)), value = text, onValueChange = { text = it},
            placeholder = { Text(text = "Type something...")},
            maxLines = 4,
            textStyle = TextStyle(fontSize = 16.sp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colors.onPrimary, focusedLabelColor = Color.Transparent),
            trailingIcon = if (text.isNotBlank()) trailingIconView else null)
    }
}

@Preview(name = "NightModeOn", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "NightModeOff", showBackground = true)
@Composable
private fun Preview() {
    SendMessageTextButton(
        Modifier,
        messageDetailsList = mutableListOf(),
        messageScrollToPosition = rememberLazyListState(),
        CoroutineScope(Dispatchers.Main)
    )
}