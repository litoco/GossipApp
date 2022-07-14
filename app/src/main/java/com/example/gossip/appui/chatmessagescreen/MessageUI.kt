package com.example.gossip.appui.chatmessagescreen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*


@Composable
fun MessageHeader(username:String) {
    Text(text = username, fontWeight = FontWeight.Bold, fontSize = 18.sp)
}

@Composable
fun MessageBody(messageText:String) {
    Text(text = messageText, fontSize = 16.sp)
}

@Composable
fun MessageFooter(sentReceivedTimeInMilliSec:Long, sentMessageStatusIcon:Int?) {
    val sentMessageDate = Date(sentReceivedTimeInMilliSec)
    val calendarInstance = Calendar.getInstance()
    calendarInstance.time = sentMessageDate
    var time = calendarInstance.get(Calendar.HOUR).toString()+":"
    time += calendarInstance.get(Calendar.MINUTE).toString()
    time += " "+ if(calendarInstance.get(Calendar.AM_PM) == 0) "AM " else "PM "
    Row (modifier = Modifier.fillMaxWidth(1f),
        horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
        Text(text = time, fontSize = 12.sp)
        if (sentMessageStatusIcon != null) {
            Image(modifier = Modifier
                .height(12.dp)
                .width(12.dp), imageVector = ImageVector.vectorResource(id = sentMessageStatusIcon),
                contentDescription = "MessageSentStatus",
            colorFilter = ColorFilter.tint(Color(0xFFA5A4A4)))
        }
    }
}

@Composable
fun MessageUIAssembler(messageDetails: MessageDetails) {
    Card (
        modifier = Modifier.padding(
            start = (16 + if(messageDetails is SentMessageDetails){16}else{0}).dp,
            end = (16 + if(messageDetails is SentMessageDetails){0}else{16}).dp,
            top = 8.dp, bottom = 8.dp
        ),
        shape = RoundedCornerShape(
            topStart = if (messageDetails is SentMessageDetails) {8.dp} else {0.dp},
            topEnd = if (messageDetails is SentMessageDetails) {0.dp} else {8.dp},
            bottomStart = 8.dp, bottomEnd = 8.dp),
        elevation = 8.dp
    ) {
        Column (modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            MessageHeader(username = messageDetails.username)
            MessageBody(messageText = messageDetails.messageString)
            MessageFooter(sentReceivedTimeInMilliSec = messageDetails.sentReceiveTimeInMilliSec, sentMessageStatusIcon =
                if (messageDetails is SentMessageDetails){messageDetails.sentMessageStatusIcon} else {null})
        }
    }
}


@Preview(name = "NightModeOn", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "NightModeOff", showBackground = true)
@Composable
private fun Preview() {
    val messageDetails = SentMessageDetails("Username", 20, "Hello")
    MessageUIAssembler(messageDetails = messageDetails)
}
