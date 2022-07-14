package com.example.gossip.appui.sendmessagerow

import android.content.res.Configuration
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gossip.R
import com.example.gossip.appui.chatmessagescreen.MessageDetails
import com.example.gossip.appui.chatmessagescreen.SentMessageDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun SendMessageTextButton(
    modifier: Modifier,
    messageDetailsList: MutableList<MessageDetails>,
    listScrollStateHolder: LazyListState,
    scope: CoroutineScope
) {

    var text by remember{ mutableStateOf("") }
    val scrollState = rememberScrollState()

    val wasKeyboardVisible = remember{ mutableStateOf(false)}
    val lastVisibleItemIndex = remember{ mutableStateOf(0)}
    val listItemAbsoluteSizeList = remember { (1..messageDetailsList.size).map { 0 }.toMutableList()}

    rememberIsKeyboardOpen(scope, listScrollStateHolder, wasKeyboardVisible, lastVisibleItemIndex, listItemAbsoluteSizeList)

    val trailingIconView = @Composable {
        IconButton(onClick = {
            val username = "You"
            val sentReceiveTime = Calendar.getInstance().timeInMillis
            val currentMessageDetails =
                SentMessageDetails(
                    username = username,
                    sentReceiveTimeInMilliSec = sentReceiveTime,
                    messageString = text
                )
            messageDetailsList.add(currentMessageDetails)
            scope.launch {
                listScrollStateHolder.animateScrollToItem(messageDetailsList.size-1)
                listItemAbsoluteSizeList.add(listScrollStateHolder.layoutInfo.visibleItemsInfo[listScrollStateHolder.layoutInfo.visibleItemsInfo.size-1].size)
            }
            text = ""
        }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_send),
                contentDescription = "Send Button",
                tint = MaterialTheme.colors.onPrimary
            )
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
            trailingIcon = if (text.isNotBlank()) trailingIconView else null
        )

    }
}

fun View.isKeyboardOpen(
    scope: CoroutineScope,
    listScrollStateHolder: LazyListState,
    isKeyboardVisibleBefore: MutableState<Boolean>,
    lastVisibleItemIndex: MutableState<Int>,
    listItemAbsoluteSizeList: MutableList<Int>
): Boolean {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect)
    val screenHeight = rootView.height
    val absoluteKeyboardHeight = if (rect.bottom > 0) screenHeight - rect.bottom else 0
    val visibleItemList = listScrollStateHolder.layoutInfo.visibleItemsInfo
    if(visibleItemList.isNotEmpty()){
        if(absoluteKeyboardHeight > 100){
            if (!isKeyboardVisibleBefore.value){
                scope.launch { listScrollStateHolder.scrollBy(absoluteKeyboardHeight.toFloat()) }
                isKeyboardVisibleBefore.value = true
            }
            lastVisibleItemIndex.value = visibleItemList[visibleItemList.size-1].index
            if (lastVisibleItemIndex.value < visibleItemList.size)
                listItemAbsoluteSizeList[visibleItemList[visibleItemList.size-1].index] = visibleItemList[visibleItemList.size-1].size
        } else {
            if (isKeyboardVisibleBefore.value) {
                var scrollOffset = 0
                var i = listScrollStateHolder.layoutInfo.totalItemsCount - 1
                while (i > lastVisibleItemIndex.value){
                    scrollOffset += listItemAbsoluteSizeList[i]
                    i -= 1
                }
                scope.launch {listScrollStateHolder.scrollBy(-scrollOffset.toFloat())}
                isKeyboardVisibleBefore.value = false
            }
        }
    }

    return absoluteKeyboardHeight > screenHeight * 0.15
}

@Composable
fun rememberIsKeyboardOpen(
    scope: CoroutineScope,
    listScrollStateHolder: LazyListState,
    wasKeyboardVisible: MutableState<Boolean>,
    lastVisibleItemIndex: MutableState<Int>,
    listItemAbsoluteSizeList: MutableList<Int>
): State<Boolean> {
    val view = LocalView.current

    return produceState(initialValue = view.isKeyboardOpen(scope, listScrollStateHolder, wasKeyboardVisible,
        lastVisibleItemIndex, listItemAbsoluteSizeList)) {
        val viewTreeObserver = view.viewTreeObserver
        val listener = ViewTreeObserver.OnGlobalLayoutListener { value = view.isKeyboardOpen(scope, listScrollStateHolder,
            wasKeyboardVisible, lastVisibleItemIndex, listItemAbsoluteSizeList) }
        viewTreeObserver.addOnGlobalLayoutListener(listener)

        awaitDispose { viewTreeObserver.removeOnGlobalLayoutListener(listener)  }
    }
}

@Preview(name = "NightModeOn", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "NightModeOff", showBackground = true)
@Composable
private fun Preview() {
    SendMessageTextButton(
        Modifier,
        messageDetailsList = mutableListOf(),
        listScrollStateHolder = rememberLazyListState(),
        CoroutineScope(Dispatchers.Main)
    )
}