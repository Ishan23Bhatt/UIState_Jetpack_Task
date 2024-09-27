package com.example.task.ui.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.data.model.Item0
import com.example.task.data.model.Specification
import com.example.task.data.singleton.CurrentOrder
import com.example.task.data.singleton.CurrentOrder.addToCartItemsList
import com.example.task.data.singleton.CurrentOrder.quantity
import com.example.task.data.singleton.CurrentOrder.quantityList
import com.example.task.data.singleton.CurrentOrder.totalAddToCartItemsList
import com.example.task.repository.ItemRepository
import com.example.task.ui.view.state.BookingServiceState
import com.example.task.ui.view.state.BookingServiceUIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingServiceViewModel @Inject constructor(private val itemRepository: ItemRepository) :
    ViewModel() {
    private val uiState = MutableStateFlow(BookingServiceState())
    fun consumableState() = uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getDataFromJSON()
        }
    }

    fun onEvent(event: BookingServiceUIEvent) {
        when (event) {
            is BookingServiceUIEvent.OnSelectApartmentSize -> {
                val items = uiState.value.apartmentSizeList?.mapIndexed { index, item0 ->
                    if (index == event.index) {
                        uiState.update {
                            it.copy(
                                selectedOptionFromApartment = item0
                            )
                        }
                        item0.copy(is_default_selected = true)
                    } else {
                        item0.copy(is_default_selected = false)
                    }
                }

                val defaultSelectedItem = items?.find { it.is_default_selected }

                val filteredSpecifications = filterSpecificationsByApartmentSize(
                    selectedOptionFromApartment = uiState.value.selectedOptionFromApartment,
                    specificationList = uiState.value.specificationList
                )

//                Log.d("asdf", "onEvent: filteredSpecifications $filteredSpecifications")

                uiState.update {
                    it.copy(
                        apartmentSizeList = items,
                        filteredSpecifications = filteredSpecifications,
                        price = defaultSelectedItem?.price ?: 0.0,
                        count = 1
                    )
                }
            }

            /* is BookingServiceUIEvent.onSelectCleaningService -> {
                 val updatedSpecifications = uiState.value.filteredSpecifications?.mapIndexed { specIndex, specification ->
                     if (specIndex == event.specificationIndex) {
                         // Only modify the specified specification
                         val updatedList = specification.list.mapIndexed { itemIndex, item ->
                             if (itemIndex == event.itemIndex && specification.type == 1) {
                                 // Radio button: single selection
                                 item.copy(is_default_selected = true)
                             } else if (itemIndex == event.itemIndex && specification.type != 1) {
                                 // Checkbox: toggle selection
                                 item.copy(is_default_selected = !item.is_default_selected)
                             } else {
                                 // For radio buttons, deselect other items in the same specification
                                 if (specification.type == 1) {
                                     item.copy(is_default_selected = false)
                                 } else {
                                     item
                                 }
                             }
                         }
                         specification.copy(list = updatedList)
                     } else {
                         specification // Return other specifications unchanged
                     }
                 }

                 uiState.update {
                     it.copy(
                         filteredSpecifications = updatedSpecifications
                     )
                 }
             }*/

            is BookingServiceUIEvent.OnSelectCleaningService -> {
                var totalPrice = uiState.value.price ?: 0.0 // Initialize total price

                val updatedSpecifications =
                    uiState.value.filteredSpecifications?.mapIndexed { specIndex, specification ->
                        if (specIndex == event.specificationIndex) {
                            // Count how many checkboxes are currently selected
                            val selectedCount = specification.list.count { it.is_default_selected }

                            // Toggle selection, respecting max range
                            val updatedList =
                                if (!specification.list[event.itemIndex].is_default_selected && selectedCount >= specification.max_range) {
                                    // If max range is reached, find the first selected checkbox to unselect it
                                    val firstSelectedIndex =
                                        specification.list.indexOfFirst { it.is_default_selected }
                                    specification.list.mapIndexed { itemIndex, item ->
                                        when (itemIndex) {
                                            firstSelectedIndex -> {
                                                // Unselect first selected and subtract its price
                                                totalPrice -= item.quantity * item.price
                                                item.copy(
                                                    is_default_selected = false,
                                                    quantity = 0
                                                ) // Set quantity to 0 when unselected
                                            }

                                            event.itemIndex -> {
                                                // Select the current one and add its price
                                                totalPrice += item.price
                                                item.copy(
                                                    is_default_selected = true,
                                                    quantity = 1
                                                ) // Set quantity to 1 when selected
                                            }

                                            else -> item // Leave others unchanged
                                        }
                                    }
                                } else {
                                    // Otherwise, just toggle the current checkbox or handle radio button logic
                                    specification.list.mapIndexed { itemIndex, item ->
                                        if (itemIndex == event.itemIndex && specification.type != 1) {
                                            if (!item.is_default_selected) {
                                                totalPrice += item.price // Add price when selected
                                                item.copy(
                                                    is_default_selected = true,
                                                    quantity = 1
                                                ) // Set quantity to 1 when selected
                                            } else {
                                                totalPrice -= item.quantity * item.price // Subtract price when deselected
                                                item.copy(
                                                    is_default_selected = false,
                                                    quantity = 0
                                                ) // Set quantity to 0 when deselected
                                            }
                                        } else if (specification.type == 1) {
                                            // For radio buttons, handle single selection
                                            if (itemIndex == event.itemIndex) {
                                                if (!item.is_default_selected) {
                                                    totalPrice += item.price // Add price when selected
                                                    item.copy(
                                                        is_default_selected = true,
                                                        quantity = 1
                                                    ) // Set quantity to 1 when selected
                                                } else {
                                                    item.copy(is_default_selected = true) // Already selected, no change in quantity
                                                }
                                            } else if (item.is_default_selected) {
                                                totalPrice -= item.quantity * item.price// Subtract price when deselected
                                                item.copy(
                                                    is_default_selected = false,
                                                    quantity = 0
                                                ) // Set quantity to 0 for deselected radio button
                                            } else {
                                                item // Return unchanged item
                                            }
                                        } else {
                                            item // Return unchanged item
                                        }
                                    }
                                }

                            // Return updated specification
                            specification.copy(list = updatedList)
                        } else {
                            specification // Return other specifications unchanged
                        }
                    }


                uiState.update {
                    it.copy(
                        filteredSpecifications = updatedSpecifications,
                        price = totalPrice // Update the total price in the state
                    )
                }
            }

            is BookingServiceUIEvent.AddButtonForItemClick -> {
                var totalPrice = uiState.value.price ?: 0.0

                val updatedSpecifications =
                    uiState.value.filteredSpecifications?.mapIndexed { specIndex, specification ->
                        if (specIndex == event.specificationIndex) {
                            totalPrice += specification.list[event.itemIndex].price

                            val updatedItem = specification.list[event.itemIndex].copy(
                                quantity = (specification.list[event.itemIndex].quantity
                                    ?: 0) + 1 // Increase quantity
                            )

                            specification.copy(
                                list = specification.list.mapIndexed { itemIndex, item ->
                                    if (itemIndex == event.itemIndex) updatedItem else item
                                }
                            )
                        } else {
                            specification
                        }
                    }

                uiState.update {
                    it.copy(
                        price = totalPrice,
                        filteredSpecifications = updatedSpecifications
                    )
                }
            }

            /*is BookingServiceUIEvent.AddButtonForItemClick -> {
                var totalPrice = uiState.value.price ?: 0.0

                val updatedSpecifications = uiState.value.filteredSpecifications?.mapIndexed { specIndex, specification ->
                    if (specIndex == event.specificationIndex) {


                        val updatedItem = specification.list[event.itemIndex].copy(
                            quantity = (specification.list[event.itemIndex].quantity ?: 0) + 1 // Increase quantity
                        )

                        // Update the total price based on the item's price
                        totalPrice += updatedItem.price

                        // Update the list with the modified item
                        specification.copy(
                            list = specification.list.mapIndexed { itemIndex, item ->
                                if (itemIndex == event.itemIndex) updatedItem else item
                            }
                        )
                    } else {
                        specification // Return other specifications unchanged
                    }
                }

                // Update the UI state with the new price and updated specifications
                uiState.update {
                    it.copy(
                        price = totalPrice,
                        filteredSpecifications = updatedSpecifications
                    )
                }
            }*/

            is BookingServiceUIEvent.MinusButtonForItemClick -> {
                var totalPrice = uiState.value.price ?: 0.0
                val updatedSpecifications =
                    uiState.value.filteredSpecifications?.mapIndexed { specIndex, specification ->
                        if (specIndex == event.specificationIndex) {

                            if (specification.list[event.itemIndex].quantity > 1) {

                                totalPrice -= specification.list[event.itemIndex].price

                                val updatedItem = specification.list[event.itemIndex].copy(
                                    quantity = (specification.list[event.itemIndex].quantity
                                        ?: 0) - 1 // Increase quantity
                                )
                                specification.copy(
                                    list = specification.list.mapIndexed { itemIndex, item ->
                                        if (itemIndex == event.itemIndex) updatedItem else item
                                    }
                                )
                            } else {
                                specification.copy(
                                    list = specification.list.mapIndexed { itemIndex, item ->
                                        item
                                    }
                                )
                            }


                        } else {
                            specification
                        }
                    }

                uiState.update {
                    it.copy(
                        price = totalPrice,
                        filteredSpecifications = updatedSpecifications
                    )
                }
            }

            is BookingServiceUIEvent.AddButtonClick -> {
                uiState.update {
                    it.copy(
                        count = it.count?.plus(1)
                    )
                }
            }

            is BookingServiceUIEvent.CancelButtonClick -> {
                if (uiState.value.count!! > 1) {
                    uiState.update {
                        it.copy(
                            count = it.count?.minus(1)
                        )
                    }
                }
            }

            is BookingServiceUIEvent.AddToCartClick -> {
                /*val updatedSpecificationList = uiState.value.specificationList?.mapIndexed { index, specification ->
                    if (index == 0 ) {
                        specification.copy(list = uiState.value.apartmentSizeList ?: uiState.value.specificationList!![0].list)
                    } else {
                        specification
                    }
                }*/

                val addToCartList = uiState.value.filteredSpecifications?.map { specification ->
                    specification.copy(
                        list = specification.list.filter { it.is_default_selected }
                    )
                }?.filter { it.list.isNotEmpty() }?.toMutableList()

                addToCartList?.let {
                    CurrentOrder.checkoutList.add(it)
                }

                val apartmentSizeList =
                    uiState.value.apartmentSizeList?.filter { it.is_default_selected }

//                addToCartItemsList = addToCartItemsList.addAll(apartmentSizeList)

                apartmentSizeList.let{ list ->
                    if (list != null) {
                        totalAddToCartItemsList.addAll(list)
                    }
                }

                apartmentSizeList?.let { list ->
                    val filteredList = list.filter { newItem ->
                        // Check if an item with the same name[0] already exists in addToCartItemsList
                        addToCartItemsList.none { existingItem ->
                            existingItem.name[0] == newItem.name[0]
                        }
                    }

                    // Add the filtered list to addToCartItemsList
                    addToCartItemsList.addAll(filteredList)
                }


                quantityList.add(uiState.value.count ?: 0)

                quantity += uiState.value.count ?: 0

                uiState.update {
                    it.copy(
                        addToCartList = addToCartList,
                        isNavigateToCleanerServiceScreen = true
                    )
                }
            }

            BookingServiceUIEvent.ChangeNavigateBack -> {
                uiState.update {
                    it.copy(
                        isNavigateToCleanerServiceScreen = false,
                    )
                }
            }
        }
    }

    private fun filterSpecificationsByApartmentSize(
        selectedOptionFromApartment: Item0?,
        specificationList: List<Specification>?
    ): List<Specification>? {
        return specificationList?.subList(1, specificationList.size)
            ?.filterIndexed { index, specification ->
                specification.modifierName == (selectedOptionFromApartment?.name?.get(0) ?: "")
            }
    }

    private suspend fun getDataFromJSON() {
        val itemData = itemRepository.getItemData()
        val apartmentSizeList =
            itemData.specifications[0].list.sortedBy { it.sequence_number } // apartment
        val specificationList = itemData.specifications
        val filteredSpecifications = filterSpecificationsByApartmentSize(
            selectedOptionFromApartment = apartmentSizeList[0],
            specificationList = specificationList
        )
        val defaultSelectedItem = apartmentSizeList.find { it.is_default_selected }
        val price = defaultSelectedItem?.price ?: 0.0
//        Log.d("asdf", "getDataFromJSON: filteredSpecifications $filteredSpecifications")
        uiState.update {
            it.copy(
                data = itemData,
                apartmentSizeList = apartmentSizeList,
                specificationList = specificationList,
                filteredSpecifications = filteredSpecifications,
                price = price,
                count = 1
            )
        }
    }

}