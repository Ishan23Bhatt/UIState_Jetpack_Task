package com.example.task.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.task.R

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    showToolbar: Boolean = false,
    toolbarBackgroundColor: Color = Color.White,
    title: String = "",
    titleColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    @DrawableRes navigationIconResId: Int = R.drawable.ic_back,
    onNavigationIconClick: () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = backgroundColor,
        topBar = {
            if (showToolbar) {
                TopAppBar(
                    modifier = Modifier
                        .background(toolbarBackgroundColor),
                    title = {
                        IconButton(
                            onClick = onNavigationIconClick
                        ) {
                            Icon(
                                painter = painterResource(id = navigationIconResId),
                                contentDescription = "Back",
                                modifier = Modifier.size(25.dp),
                                tint = Color.Black
                            )
                        }
                        Text(text = title, color = titleColor)
                    },
                    backgroundColor = toolbarBackgroundColor,
                    elevation = 0.dp
                )
            }
        },
        bottomBar = bottomBar
    ) { contentPadding ->
        content(contentPadding)
    }
}