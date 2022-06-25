package com.example.gossip.appui.topappbar

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.gossip.appui.DemoData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AssembleTopBar(scaffoldState:ScaffoldState, topBarData: TopBarData, scope: CoroutineScope) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                Icon(imageVector = ImageVector.vectorResource(id = topBarData.navigationDrawerIcon),
                    contentDescription = "Navigation Drawer", tint = MaterialTheme.colors.onPrimary)
            }
            Text(modifier = Modifier.weight(1f), text = topBarData.title,
                fontWeight = FontWeight.Bold, fontSize = 18.sp, style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onPrimary)
            IconButton(onClick = {
                //TODO: add click behaviour for skip icon
            }) {
                Icon(imageVector = ImageVector.vectorResource(id = topBarData.refreshIcon),
                    contentDescription = "Skip Icon", tint = MaterialTheme.colors.onPrimary)
            }
        }
    }
}

@Preview(name = "NightModeOn", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "NightModeOff", showBackground = true)
@Composable
private fun Preview() {
    AssembleTopBar(scaffoldState = rememberScaffoldState(),
        topBarData = DemoData.getTopAppBarData(),
        scope = rememberCoroutineScope())
}