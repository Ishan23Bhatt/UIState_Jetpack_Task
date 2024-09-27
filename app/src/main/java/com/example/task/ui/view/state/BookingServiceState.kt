package com.example.task.ui.view.state

import android.widget.GridLayout.Spec
import com.example.task.data.model.CartItems
import com.example.task.data.model.Item0
import com.example.task.data.model.Specification

data class BookingServiceState(
//    val isNavigateToBack: Boolean = false,
    val isNavigateToCleanerServiceScreen: Boolean = false,
    val data: CartItems? = null,
    val apartmentSizeList: List<Item0>? = listOf(),
    val specificationList: List<Specification>? = listOf(),
    val filteredSpecifications: List<Specification>? = listOf(),
    val selectedOptionFromApartment: Item0? = null,
    val selectedOptionFromCleaningService: Item0? = null,
    val price: Double? = null,
    val count: Int? = 1,
    val quantity : Int? = null,
    val addToCartList: List<Specification>? = listOf(),
)

sealed class BookingServiceUIEvent {
    data class OnSelectApartmentSize(val index: Int) : BookingServiceUIEvent()
    data class OnSelectCleaningService(val specificationIndex: Int, val itemIndex: Int) : BookingServiceUIEvent()
    data class AddButtonForItemClick(val specificationIndex: Int, val itemIndex: Int) : BookingServiceUIEvent()
    data class MinusButtonForItemClick(val specificationIndex: Int, val itemIndex: Int) : BookingServiceUIEvent()
    data object AddButtonClick : BookingServiceUIEvent()
    data object CancelButtonClick : BookingServiceUIEvent()
    data object AddToCartClick : BookingServiceUIEvent()
    data object ChangeNavigateBack : BookingServiceUIEvent()
}
