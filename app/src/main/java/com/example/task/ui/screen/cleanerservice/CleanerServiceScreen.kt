package com.example.task.ui.screen.cleanerservice

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.task.R
import com.example.task.data.model.Specification
import com.example.task.data.singleton.CurrentOrder
import com.example.task.data.singleton.CurrentOrder.addToCartItemsList
import com.example.task.data.singleton.CurrentOrder.quantity
import com.example.task.ui.component.AppModalBottomSheet
import com.example.task.ui.view.state.CleanerServiceUIEvent
import com.example.task.ui.view.viewmodel.CleanerServiceViewModel

@Composable
fun CleanerServiceScreen(
    onNavigateToBack: () -> Unit,
    onNavigateToBookingService: () -> Unit,
    onNavigateToCheckoutScreen: () -> Unit,
    itemsCount: () -> Int?,
) {
    val viewModel = hiltViewModel<CleanerServiceViewModel>()
    val viewState by viewModel.consumableState().collectAsState()
    Log.d("asdf", "onEvent: addToCartItemsList :: $addToCartItemsList")
    val showBottomSheet by remember { derivedStateOf { viewState.showBottomSheet } }
    CleanerServiceScreenContent(
        onNavigateToBack = onNavigateToBack,
        onNavigateToBookingService = onNavigateToBookingService,
        addToCartList = { listOf() },
        totalItems = itemsCount,
        showBottomSheet = { showBottomSheet },
        onAddButtonClick = { viewModel.onEvent(CleanerServiceUIEvent.AddButtonClick) },
        onDismissBottomSheet = { viewModel.onEvent(CleanerServiceUIEvent.DismissBottomSheet) },
        onNavigateToCheckoutScreen = onNavigateToCheckoutScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleanerServiceScreenContent(
    onNavigateToBack: () -> Unit = {},
    onNavigateToBookingService: () -> Unit = {},
    addToCartList: () -> List<Specification>? = { listOf() },
    totalItems: () -> Int? = { 0 },
    showBottomSheet: () -> Boolean = { false },
    onAddButtonClick: () -> Unit = {},
    onDismissBottomSheet: () -> Unit = {},
    onNavigateToCheckoutScreen: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column {
            TopAppBarSection()
            Spacer(modifier = Modifier.height(16.dp))
            ServiceDetailsSection(
                onNavigateToBookingService = onNavigateToBookingService,
                addToCartList = addToCartList,
                totalItems = totalItems,
                onAddButtonClick = onAddButtonClick
            )
        }
        if (quantity != 0) {
            FloatingActionButton(
                containerColor = Color(0xFF00BCD4),
                onClick = { onNavigateToCheckoutScreen.invoke() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 22.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Image(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            modifier = Modifier
                                .size(38.dp)
                                .padding(2.dp)
                        )
                        Badge(
                            containerColor = Color.Red,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(Color.Red, CircleShape),
                        ) {
                            Text(
                                text = addToCartItemsList.size.toString(),
                                color = Color.White,
                                fontSize = 12.sp,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "View Cart",
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.wrapContentWidth()
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
            if (showBottomSheet()) {
                AppModalBottomSheet(onDismissRequest = onDismissBottomSheet) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                    ) {
                        Text(
                            text = "Would you like to Repeat Last Customize ",
                            color = Color.Black,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Start,
                            maxLines = Int.MAX_VALUE,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "Customization : ${addToCartItemsList.last().name[0]}",
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start
                        )

                        Row(modifier = Modifier.padding(16.dp)) {
                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(Color(0xFF00BCD4)),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentWidth()
                            ) {
                                Text(text = "Customize", color = Color.White)
                            }

                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF00BCD4
                                    )
                                ),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier
                                    .wrapContentWidth()
                            ) {
                                Text(text = "Repeat", color = Color.White)
                            }
                        }

                    }
                }
            }

        }
    }

}

@Composable
fun TopAppBarSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background), // Replace with your drawable resource
            contentDescription = "Cleaner logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        // Back button and actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle back */ }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Row {
                IconButton(onClick = { /* Handle like */ }) {
                    Icon(Icons.Default.ThumbUp, contentDescription = "Like", tint = Color.White)
                }
                IconButton(onClick = { /* Handle settings */ }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceDetailsSection(
    onNavigateToBookingService: () -> Unit,
    addToCartList: () -> List<Specification>?,
    totalItems: () -> Int?,
    onAddButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Cleaner",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Rating and duration
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .background(Color(0xFF00BCD4), shape = RoundedCornerShape(17.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "0.0",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "|")
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "30 - 45 mins",
                color = Color.Gray,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "|")
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "₹",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Home cleaning description
        Text(
            text = "All items are exclusive of all taxes",
            fontSize = 12.sp,
            color = Color.Green
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Home cleanning",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Price
            Text(
                text = "Make your own Package",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            /*Button(
                onClick = onNavigateToBookingService,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .border(1.dp, Color(0xff00bcd4), RoundedCornerShape(50))
                    .wrapContentWidth()
            ) {
                Text(text = "Customize ${totalItems()}", color = Color.Black)
            }*/

            // Customize button
            if (CurrentOrder.quantity == 0) {
                Button(
                    onClick = onNavigateToBookingService,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier
                        .border(1.dp, Color(0xff00bcd4), RoundedCornerShape(50))
                        .wrapContentWidth()
                ) {
                    Text(text = "Customize", color = Color.Black)
                }
            } else {
                Row(
                    modifier = Modifier
                        .border(1.dp, Color(0xff00bcd4), RoundedCornerShape(50))
                        .height(40.dp)
                        .background(Color.White, RoundedCornerShape(50)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "-",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                            }
                            .wrapContentWidth()
                    )

                    androidx.compose.material.Divider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp),
                        color = Color.Gray
                    )

                    Text(
                        text = CurrentOrder.quantity.toString(),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .wrapContentWidth()
                    )

                    androidx.compose.material.Divider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp),
                        color = Color.Gray
                    )

                    Text(
                        text = "+",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                onAddButtonClick.invoke()
                            }
                            .wrapContentWidth()
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNavigateToBookingService,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .border(1.dp, Color(0xff00bcd4), RoundedCornerShape(50))
                .wrapContentWidth()
        ) {
            Text(text = "Customize", color = Color.Black)
        }
        // Price and Customize button
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "₹ 999.00",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(bottom = 70.dp, start = 8.dp)
                    .wrapContentWidth()
            )
            Image(
                painter = painterResource(id = R.drawable.plain_image),
                contentDescription = "",
                modifier = Modifier.padding(end = 15.dp)
            )
        }
        Divider(modifier = Modifier.height(1.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCleanerServiceScreen() {
    CleanerServiceScreenContent()
}
