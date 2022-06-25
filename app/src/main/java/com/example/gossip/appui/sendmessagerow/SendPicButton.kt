package com.example.gossip.appui.sendmessagerow

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gossip.R

@Composable
fun SendPicButton() {

    Button(onClick = {
        /*TODO*/
    }, modifier = Modifier.padding(end = 6.dp).border(width = 0.5.dp, color = MaterialTheme.colors.onPrimary,
    shape = RoundedCornerShape(corner = CornerSize(6.dp))),
    elevation = null) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_insert_photo),
            contentDescription = "Send Pic Button",
            tint = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(top = 7.5.dp, bottom = 7.5.dp)
        )
    }
}

@Preview(name = "NightModeOn", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "NightModeOff", showBackground = true)
@Composable
private fun Preview() {
    SendPicButton()
}