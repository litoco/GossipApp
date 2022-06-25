package com.example.gossip.appui.navigationdrawer

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope

@Composable
fun AssembledDrawer(
    headerData: DrawerHeaderData,
    bodyData: DrawerBodyData,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavHostController
) {
    NavigationHeader(headerData = headerData)
    NavigationBody(bodyData = bodyData, scope, scaffoldState, navController)
}