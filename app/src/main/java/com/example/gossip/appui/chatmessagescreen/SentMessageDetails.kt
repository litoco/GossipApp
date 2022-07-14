package com.example.gossip.appui.chatmessagescreen

import com.example.gossip.R

class SentMessageDetails (username: String, sentReceiveTimeInMilliSec: Long, messageString: String, val sentMessageStatusIcon: Int = R.drawable.ic_baseline_sending_pending):
    MessageDetails (username, sentReceiveTimeInMilliSec, messageString) {

}