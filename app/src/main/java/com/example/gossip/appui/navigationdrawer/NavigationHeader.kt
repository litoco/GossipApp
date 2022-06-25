package com.example.gossip.appui.navigationdrawer

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.gossip.R


@Composable
fun UserPhoto(profilePicUrl: String) {
    val painter = rememberAsyncImagePainter(model = profilePicUrl)

    SubcomposeAsyncImage(model = profilePicUrl, contentDescription = "ProfilePic", modifier = Modifier.padding(2.dp).height(80.dp).width(80.dp)
        .border(width = 2.dp, shape = CircleShape, color = MaterialTheme.colors.onPrimary)) {
        if (painter.state is AsyncImagePainter.State.Loading || painter.state is AsyncImagePainter.State.Error){
            Image(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_account_circle), contentDescription = "DefaultProfilePic",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary))
        } else {
            Image(painter = painter, contentDescription = "ProfilePic")
        }
    }
}

@Composable
fun NavigationHeader(headerData: DrawerHeaderData) {
    Column(modifier = Modifier
        .height(192.dp)
        .fillMaxWidth()
        .background(MaterialTheme.colors.primary)
        .padding(16.dp), verticalArrangement = Arrangement.Bottom) {
        UserPhoto(headerData.profilePicUrl)
        Text(text = headerData.username, fontSize = 16.sp, fontWeight = Bold, color = MaterialTheme.colors.onPrimary, modifier = Modifier.padding(start = 8.dp))
        Text(text = headerData.userEmailOrPhone, fontSize = 16.sp, color = MaterialTheme.colors.onPrimary, modifier = Modifier.padding(start = 8.dp))
    }
}

@Preview(name = "NightModeOn", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "NightModeOff", showBackground = true)
@Composable
private fun Preview() {
    NavigationHeader(DrawerHeaderData())
}