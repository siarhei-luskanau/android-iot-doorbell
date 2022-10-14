package siarhei.luskanau.iot.doorbell.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
@Suppress("FunctionNaming")
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
@Suppress("FunctionNaming")
fun ErrorItem(text: String) {
    Card(
        elevation = 2.dp,
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = text,
            color = Color.Red,
            style = MaterialTheme.typography.h5
        )
    }
}
