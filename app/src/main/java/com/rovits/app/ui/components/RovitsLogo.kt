package com.rovits.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rovits.app.R
import com.rovits.app.ui.theme.RovitsAppTheme

@Composable
fun RovitsLogo(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp
) {
    Image(
        painter = painterResource(id = R.drawable.ic_logo),
        contentDescription = "Rovits Logo",
        modifier = modifier.size(size)
    )
}

@Preview(showBackground = true)
@Composable
fun RovitsLogoPreview() {
    RovitsAppTheme {
        RovitsLogo()
    }
}

