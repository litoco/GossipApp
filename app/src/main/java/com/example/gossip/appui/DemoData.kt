package com.example.gossip.appui

import com.example.gossip.R
import com.example.gossip.appui.chatmessagescreen.MessageDetails
import com.example.gossip.appui.chatmessagescreen.ReceivedMessageDetails
import com.example.gossip.appui.chatmessagescreen.SentMessageDetails
import com.example.gossip.appui.navigationdrawer.DrawerBodyData
import com.example.gossip.appui.navigationdrawer.DrawerHeaderData
import com.example.gossip.appui.navigationdrawer.MenuRow
import com.example.gossip.appui.topappbar.TopBarData
import java.util.*

class DemoData {
    companion object{
        private fun getMenuList(): MutableList<MenuRow> {
            val menList: MutableList<MenuRow> = mutableListOf()
            menList.add(MenuRow(R.drawable.ic_baseline_info, "About"))
            menList.add(MenuRow(R.drawable.ic_baseline_logout, "Logout"))
            return menList
        }

        fun getDrawerHeaderData(): DrawerHeaderData {
            return DrawerHeaderData()
        }

        fun getDrawerBodyData(): DrawerBodyData {
            return DrawerBodyData(menuList = getMenuList())
        }

        fun getTopAppBarData(): TopBarData {
            return TopBarData(navigationDrawerIcon = R.drawable.ic_baseline_menu, title = "Stranger", refreshIcon = R.drawable.ic_baseline_change)
        }

        fun getMessages(): List<MessageDetails> {
            val messages = mutableListOf<MessageDetails>()
            for (item in (listOf(1..10).random())){
                val isSentMessage:Boolean = listOf(true, false).random()
                val messageDetails: MessageDetails =
                    if (isSentMessage) {
                        val username = "You"
                        val timeInMilliSec = Calendar.getInstance().timeInMillis
                        var messageString = ""
                        for (sentence in (1..(2..5).random())){
                            for (word in (1..(2..10).random())){
                                val currentWord = listOf("Lorem", "Ipsum", "Dolor", "Sit", "Amet", "Sed", "Vivamus", "Cras").random()
                                messageString += " $currentWord"
                            }
                            messageString += "."
                        }
                        SentMessageDetails(username = username, sentReceiveTimeInMilliSec = timeInMilliSec, messageString = messageString)
                    }else{
                        val username = "Stranger"
                        val timeInSec = Calendar.getInstance().timeInMillis
                        var messageString = ""
                        for (sentence in (1..(2..5).random())){
                            for (word in (1..(2..10).random())){
                                val currentWord = listOf("Lorem", "Ipsum", "Dolor", "Sit", "Amet", "Sed", "Vivamus", "Cras").random()
                                messageString += " $currentWord"
                            }
                            messageString += "."
                        }
                        ReceivedMessageDetails(username = username, sentReceiveTimeInSec = timeInSec, messageString = messageString)
                    }
                messages.add(messageDetails)
            }
            return messages
        }
    }
}