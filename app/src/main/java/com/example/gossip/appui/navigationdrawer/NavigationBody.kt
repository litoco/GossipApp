package com.example.gossip.appui.navigationdrawer

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gossip.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationBody(
    bodyData: DrawerBodyData,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavHostController
) {
    Column(modifier = Modifier.padding(16.dp)) {
        for (menu in bodyData.menuList){
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                , verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = menu.iconResId), contentDescription = menu.iconName, colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary))
                Text(text = menu.iconName, modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),fontSize = 16.sp, color = MaterialTheme.colors.onPrimary)
            }
        }
    }
}

@Preview(name = "NightModeOn", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "NightModeOff", showBackground = true)
@Composable
private fun Preview() {
    val menList: MutableList<MenuRow> = mutableListOf()
    menList.add(MenuRow(R.drawable.ic_baseline_info, "About"))
    menList.add(MenuRow(R.drawable.ic_baseline_logout, "Logout"))
    NavigationBody(DrawerBodyData(mutableListOf()), rememberCoroutineScope(), rememberScaffoldState(), rememberNavController())
}