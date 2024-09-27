package com.example.task.ui.screen.checkout

import android.util.Log
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task.data.model.Specification
import com.example.task.data.singleton.CurrentOrder
import com.example.task.ui.component.AppScaffold

@Composable
fun CheckoutScreen(
    onNavigateBack: () -> Unit,

    ) {
    CheckoutScreenContent(
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun CheckoutScreenContent(
    onNavigateBack: () -> Unit = {}
) {

    AppScaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        title = "Cart",
        showToolbar = true,
        onNavigationIconClick = onNavigateBack

    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Log.d("asdf", "CheckoutScreenContent: ${CurrentOrder.checkoutList}")
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                itemsIndexed(CurrentOrder.checkoutList) { index, item ->
                    CheckoutCartItem(specificationList = item, itemIndex = index)
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                }
            }
        }

    }

}

@Composable
fun CheckoutCartItem(specificationList: List<Specification>, itemIndex: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Make Your Own Package",
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = Color(0xFF00BCD4)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${CurrentOrder.totalAddToCartItemsList.getOrNull(itemIndex)?.name?.getOrNull(0)} : ",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Log.d("qwert", "CheckoutCartItem: specificationList :: $specificationList")
            specificationList.forEach { specification ->
                specification.list.forEach { item ->
                    val description = item.name.getOrNull(0) ?: "null"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$description (${item.quantity}),",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
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
                        fontSize = 18.sp,
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
                        text = CurrentOrder.quantityList[itemIndex].toString(),
                        fontSize = 18.sp,
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
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {

                            }
                            .wrapContentWidth()
                    )
                }
                Text(
                    text = "₹ 12.00",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutScreenContentPreview() {
    CheckoutScreenContent()
}