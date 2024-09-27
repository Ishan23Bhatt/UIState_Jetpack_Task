package com.example.task.ui.screen.bookingservice

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Divider
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.task.R
import com.example.task.data.model.CartItems
import com.example.task.data.model.Item0
import com.example.task.data.model.Specification
import com.example.task.ui.component.AppScaffold
import com.example.task.ui.view.state.BookingServiceUIEvent
import com.example.task.ui.view.viewmodel.BookingServiceViewModel

@Composable
fun BookingServiceScreen(
    onNavigateToBack: () -> Unit,
    onNavigateToCleanerServiceScreen: (Int) -> Unit,
) {
    val viewModel = hiltViewModel<BookingServiceViewModel>()
    val viewState by viewModel.consumableState().collectAsState()

//    val isNavigateToBack by remember { derivedStateOf { viewState.isNavigateToBack } }
    val isNavigateToCleanerServiceScreen by remember { derivedStateOf { viewState.isNavigateToCleanerServiceScreen } }

    val data by remember { derivedStateOf { viewState.data } }
    val apartmentSizeList by remember { derivedStateOf { viewState.apartmentSizeList } }
    val specificationList by remember { derivedStateOf { viewState.specificationList } }
    val filterSpecification by remember { derivedStateOf { viewState.filteredSpecifications } }
    val price by remember { derivedStateOf { viewState.price } }
    val count by remember { derivedStateOf { viewState.count } }

    val scrollState = rememberScrollState()

    BookingServiceScreenContent(
        data = { data },
        scrollState = scrollState,
        onNavigateToBack = { onNavigateToBack() },
        onSelectApartmentSize = { viewModel.onEvent(BookingServiceUIEvent.OnSelectApartmentSize(it)) },
        apartmentSizeList = { apartmentSizeList },
        specificationList = { specificationList },
        filterSpecification = { filterSpecification },
        onSelectCleaningService = { specificationIndex, index ->
            viewModel.onEvent(
                BookingServiceUIEvent.OnSelectCleaningService(specificationIndex, index)
            )
        },
        addToCartPrice = { price },
        onAddButtonForItemClick = { specificationIndex, index ->
            viewModel.onEvent(
                BookingServiceUIEvent.AddButtonForItemClick(specificationIndex, index)
            )
        },
        onCancelButtonForItemClick = { specificationIndex, index ->
            viewModel.onEvent(
                BookingServiceUIEvent.MinusButtonForItemClick(specificationIndex, index)
            )
        },
        onAddButtonClick = { viewModel.onEvent(BookingServiceUIEvent.AddButtonClick) },
        onCancelButtonClick = { viewModel.onEvent(BookingServiceUIEvent.CancelButtonClick) },
        count = { count },
        addToCartClick = { viewModel.onEvent(BookingServiceUIEvent.AddToCartClick) }
    )

    NavigationLaunchEffect(
        isNavigateToCleanerServiceScreen = { isNavigateToCleanerServiceScreen },
        itemsCount = { count ?: 0 },
        navigateToCleanerServiceScreen = onNavigateToCleanerServiceScreen,
        changeBackNavigate = { viewModel.onEvent(BookingServiceUIEvent.ChangeNavigateBack) }
    )

}

@Composable
fun BookingServiceScreenContent(
    scrollState: ScrollState = rememberScrollState(),
    onNavigateToBack: () -> Unit = {},
    data: () -> CartItems? = { null },
    onSelectApartmentSize: (Int) -> Unit = {},
    apartmentSizeList: () -> List<Item0>? = { listOf() },
    specificationList: () -> List<Specification>? = { listOf() },
    filterSpecification: () -> List<Specification>? = { listOf() },
    onSelectCleaningService: (Int, Int) -> Unit = { _, _ -> },
    addToCartPrice: () -> Double? = { 0.00 },
    onAddButtonForItemClick: (Int, Int) -> Unit = { _, _ -> },
    onCancelButtonForItemClick: (Int, Int) -> Unit = { _, _ -> },
    onAddButtonClick: () -> Unit = {},
    onCancelButtonClick: () -> Unit = {},
    count: () -> Int? = { 1 },
    addToCartClick: () -> Unit = {},
) {
    AppScaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        title = data()?.name?.get(0) ?: "",
        onNavigationIconClick = { onNavigateToBack() },
        showToolbar = scrollState.value > 750,
        bottomBar = {
            BottomAppBarSection(
                addToCartPrice = addToCartPrice,
                onAddButtonClick = onAddButtonClick,
                onCancelButtonClick = onCancelButtonClick,
                count = count,
                addToCartClick = addToCartClick,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            TopAppBarSection(onNavigateToBack = { onNavigateToBack() })

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Text(
                    text = data()?.name?.get(0) ?: "",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(5.dp),
                    style = MaterialTheme.typography.headlineLarge
                )

                Divider()

                //Apartment size
                Text(
                    text = data()?.specifications?.get(0)?.name?.get(0) ?: "",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 5.dp, start = 5.dp),
                    fontWeight = FontWeight.Bold,
                )

                //Choose 1
                Text(
                    text = if ((specificationList()?.getOrNull(0)?.type ?: 0) == 1) {
                        "Choose 1"
                    } else {
                        "Choose ${specificationList()?.getOrNull(0)?.range} to ${
                            specificationList()?.getOrNull(
                                0
                            )?.max_range
                        }"
                    },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 5.dp, top = 2.dp),
                )

                //Radio button
                ApartmentSizeOption(
                    apartmentSizeList = apartmentSizeList,
                    onSelectApartmentSize = onSelectApartmentSize,
                )
                /*CleaningServiceItem(
                    cleaningServiceList = apartmentSizeList,
                    onItemClick = onSelectCleaningService,
                    cleaningServiceType = { specificationList()?.getOrNull(0)?.type },
                )*/

                Divider()

                filterSpecification()?.forEachIndexed { index, specification ->
                    Text(
                        text = specification.name.getOrNull(0) ?: "",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 10.dp, start = 5.dp),
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = if (specification.type == 1) {
                            "Choose 1"
                        } else {
                            "Choose ${specification.range} to ${specification.max_range}"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 5.dp, top = 2.dp),
                    )

                    CleaningServiceItem(
                        cleaningServiceList = { specification.list },
                        onItemClick = onSelectCleaningService,
                        cleaningServiceType = { specification.type },
                        userCanAddSpecificationQuantity = { specification.user_can_add_specification_quantity },
                        specificationIndex = index,
                        onAddButtonForItemClick = onAddButtonForItemClick,
                        onCancelButtonForItemClick = onCancelButtonForItemClick
                    )

                    Divider()
                }
            }
        }
    }
}

@Composable
fun BottomAppBarSection(
    addToCartPrice: () -> Double?,
    onAddButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit,
    count: () -> Int? = { 1 },
    addToCartClick: () -> Unit = {},
    onNavigateToBack: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .border(1.dp, Color(0xff00bcd4), RoundedCornerShape(50))
                .height(40.dp)
                .background(Color.White, RoundedCornerShape(50)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "-",
                color = Color(0xff00bcd4),
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { onCancelButtonClick.invoke() }
            )

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp), color = Color(0xff00bcd4)
            )

            Text(
                text = count().toString(),
                fontSize = 20.sp,
                color = Color(0xff00bcd4),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp), color = Color(0xff00bcd4)
            )

            Text(
                text = "+",
                fontSize = 20.sp,
                color = Color(0xff00bcd4),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { onAddButtonClick.invoke() }
            )
        }
        Button(
            onClick = { addToCartClick.invoke() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff00bcd4)),
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Text(text = "Add to Cart - ₹ ${"%.2f".format((addToCartPrice()?.times(count() ?: 1)) ?: 0.00)}")
        }
    }
}

@Composable
fun ApartmentSizeOption(
    apartmentSizeList: () -> List<Item0>?,
    onSelectApartmentSize: (Int) -> Unit,
) {
    apartmentSizeList()?.forEachIndexed { index, item0 ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppRadioButton(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 5.dp),
                text = item0.name[0],
                selected = item0.is_default_selected,
                onClick = { onSelectApartmentSize(index) }
            )
            Text(
                text = "₹ %.2f".format(item0.price),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun CleaningServiceItem(
    cleaningServiceList: () -> List<Item0>?,
    onItemClick: (Int, Int) -> Unit,
    cleaningServiceType: () -> Int?,
    specificationIndex: Int? = null,
    userCanAddSpecificationQuantity: () -> Boolean,
    onAddButtonForItemClick: (Int, Int) -> Unit,
    onCancelButtonForItemClick: (Int, Int) -> Unit,
) {
    cleaningServiceList()?.forEachIndexed { index, item0 ->
        if (cleaningServiceType() == 1) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppRadioButton(
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 5.dp)
                        .weight(1f),
                    text = item0.name[0],
                    selected = item0.is_default_selected,
                    onClick = {
                        if (specificationIndex != null) {
                            onItemClick(specificationIndex, index)
                        }
                    }
                )
                Text(text = "₹ ${item0.price}")
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AppCheckBox(
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 5.dp)
                        .weight(1f),
                    text = item0.name[0],
                    checked = item0.is_default_selected,
                    onCheckedChange = {
                        if (specificationIndex != null) {
                            onItemClick(specificationIndex, index)
                        }
                    }
                )
                if (item0.is_default_selected && userCanAddSpecificationQuantity()) {
                    Row(
                        modifier = Modifier
                            .border(1.dp, Color(0xff00bcd4), RoundedCornerShape(50)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "-",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable {
                                    if (specificationIndex != null) {
                                        onCancelButtonForItemClick.invoke(specificationIndex, index)
                                    }
                                }
                        )

                        Divider(
                            modifier = Modifier
                                .height(30.dp)
                                .width(1.dp), color = Color.Gray
                        )

                        Text(
                            text = item0.quantity.toString(),//TODO manage counter
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Divider(
                            modifier = Modifier
                                .height(30.dp)
                                .width(1.dp), color = Color.Gray
                        )

                        Text(
                            text = "+",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable {
                                    if (specificationIndex != null) {
                                        onAddButtonForItemClick(specificationIndex, index)
                                    }
                                }
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "₹ %.2f".format(item0.price)
                )
            }
        }

    }
}

@Composable
fun TopAppBarSection(onNavigateToBack: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background), // Replace with your drawable resource
            contentDescription = "Cleaner logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onNavigateToBack.invoke() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun AppRadioButton(
    modifier: Modifier = Modifier,
    text: String = "",
    textColor: Color = Color.Black,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight = FontWeight.Medium,
    fontSize: TextUnit = 14.sp,
    selected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            modifier = Modifier.size(25.dp),
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF00BCD4),
                unselectedColor = Color(0xFF00BCD4),
            )
        )

        Text(
            modifier = Modifier.padding(start = 5.dp),
            text = text,
            style = textStyle,
            fontWeight = fontWeight,
            fontSize = fontSize,
            color = textColor,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun AppCheckBox(
    modifier: Modifier = Modifier,
    text: String = "",
    textColor: Color = Color.Black,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight = FontWeight.Medium,
    fontSize: TextUnit = TextUnit.Unspecified,
    colors: CheckboxColors = CheckboxDefaults.colors(Color(0xff00bcd4)),
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            modifier = Modifier.size(25.dp),
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = colors
        )
        Text(
            modifier = Modifier.padding(start = 5.dp),
            text = text,
            style = textStyle,
            fontWeight = fontWeight,
            fontSize = fontSize,
            color = textColor,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
        )
    }
}


@Composable
fun NavigationLaunchEffect(
    isNavigateToCleanerServiceScreen: () -> Boolean,
    itemsCount: () -> Int,
    navigateToCleanerServiceScreen: (Int) -> Unit,
    changeBackNavigate: () -> Unit = {}
) {
    if (isNavigateToCleanerServiceScreen()) {
        changeBackNavigate()
        navigateToCleanerServiceScreen(itemsCount())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookingServiceScreen() {
    BookingServiceScreenContent()
}