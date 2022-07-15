package com.example.gossip.appui.sendmessagerow

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.animateScrollBy
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
            scope.launch { listScrollStateHolder.animateScrollToItem(messageDetailsList.size-1) }
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

    val scrollState = rememberScrollState()
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

    SyncLazyColumnScroll(scope, listScrollStateHolder)
}


@Composable
fun SyncLazyColumnScroll(
    scope: CoroutineScope,
    listScrollStateHolder: LazyListState
) {
    val isKeyboardOpen by rememberIsKeyboardOpen()
    var hasKeyboardOpenedOnce by remember { mutableStateOf(false) }
    val lazyListLayoutInfo = listScrollStateHolder.layoutInfo
    val onScreenVisibleItemList = lazyListLayoutInfo.visibleItemsInfo
    var prevScreenSize by remember { mutableStateOf(0)}
    var lastVisibleItemIndex by remember { mutableStateOf(0)}
    var lastVisibleItemOffset by remember { mutableStateOf(0)}

    if(onScreenVisibleItemList.isNotEmpty()){

        if (lazyListLayoutInfo.viewportEndOffset > prevScreenSize)
            prevScreenSize = lazyListLayoutInfo.viewportEndOffset

        //1. find last visible items offset from bottom in keyboard open state and -> implemented from line 131, 132
        //2. find the same items offset after keyboard invisible -> implemented from line 136 to 142
        //The difference between these two (2 - 1) is the net list scroll offset

    }

    if(isKeyboardOpen){
        val scrollOffset = prevScreenSize-listScrollStateHolder.layoutInfo.viewportEndOffset
        LaunchedEffect(key1 = isKeyboardOpen){
            scope.launch { listScrollStateHolder.animateScrollBy(scrollOffset.toFloat())}
        }
        lastVisibleItemIndex = onScreenVisibleItemList[onScreenVisibleItemList.size-1].index
        lastVisibleItemOffset = lazyListLayoutInfo.viewportEndOffset - onScreenVisibleItemList[onScreenVisibleItemList.size-1].offset
        hasKeyboardOpenedOnce = true
    } else {
        if(hasKeyboardOpenedOnce) {
            var prevItemOffset = 0
            for (i in onScreenVisibleItemList){ if(i.index == lastVisibleItemIndex) { prevItemOffset = i.offset; break } }
            val currentScrollOffset = lazyListLayoutInfo.viewportEndOffset - prevItemOffset
            LaunchedEffect(key1 = isKeyboardOpen){
                scope.launch { listScrollStateHolder.scrollBy((lastVisibleItemOffset - currentScrollOffset).toFloat()) }
            }
        }
        hasKeyboardOpenedOnce = false
    }

}

fun View.getKeyboardHeight(): Int {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect)
    val screenHeight = rootView.height
    return if (rect.bottom > 0) screenHeight - rect.bottom else 0
}

@Composable
fun rememberIsKeyboardOpen(): State<Boolean> {
    val view = LocalView.current

    return produceState(initialValue = view.getKeyboardHeight() > view.rootView.height * .15) {
        val viewTreeObserver = view.viewTreeObserver
        val listener = ViewTreeObserver.OnGlobalLayoutListener { value = view.getKeyboardHeight() > view.rootView.height * .15}
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