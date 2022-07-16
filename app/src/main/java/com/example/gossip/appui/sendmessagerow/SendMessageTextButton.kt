package com.example.gossip.appui.sendmessagerow

import android.content.res.Configuration
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListItemInfo
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
    var prevScreenSize by remember { mutableStateOf(0)}
    var lastVisibleItemIndex by remember { mutableStateOf(0)}
    var lastVisibleItemOffset by remember { mutableStateOf(0)}
    var hasScrolledOnce by remember { mutableStateOf(false)}


    //Add the above if else code inside one "LaunchedEffect" block
    LaunchedEffect(key1 = isKeyboardOpen, key2 = listScrollStateHolder.isScrollInProgress){
        if (listScrollStateHolder.layoutInfo.viewportEndOffset > prevScreenSize)
            prevScreenSize = listScrollStateHolder.layoutInfo.viewportEndOffset
        if (listScrollStateHolder.layoutInfo.visibleItemsInfo.isNotEmpty()) {
            if (isKeyboardOpen) {
                if (!listScrollStateHolder.isScrollInProgress && !hasScrolledOnce) {
                    val size = listScrollStateHolder.layoutInfo.viewportEndOffset
                    scope.launch { listScrollStateHolder.animateScrollBy((prevScreenSize - size).toFloat()) }
                    hasScrolledOnce = true
                }
                lastVisibleItemIndex = listScrollStateHolder.layoutInfo.visibleItemsInfo[listScrollStateHolder.layoutInfo.visibleItemsInfo.size - 1].index
                lastVisibleItemOffset = listScrollStateHolder.layoutInfo.viewportEndOffset - listScrollStateHolder.layoutInfo.visibleItemsInfo[listScrollStateHolder.layoutInfo.visibleItemsInfo.size - 1].offset
            } else {
                if (!listScrollStateHolder.isScrollInProgress) {
                    var itemDetails:LazyListItemInfo? = null
                    for (item in listScrollStateHolder.layoutInfo.visibleItemsInfo){
                        if (item.index == lastVisibleItemIndex){
                            itemDetails = item
                            break
                        }
                    }
                    if (itemDetails != null){
                        val lastItemCurrentOffset = (listScrollStateHolder.layoutInfo.viewportEndOffset - itemDetails.offset) - lastVisibleItemOffset
                        if (hasScrolledOnce){
                            scope.launch { listScrollStateHolder.animateScrollBy(-lastItemCurrentOffset.toFloat()) }
                        }
                    }
                    hasScrolledOnce = false
                }
            }
        }
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