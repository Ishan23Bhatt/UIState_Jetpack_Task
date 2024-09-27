package com.example.task.ui.view.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.task.ui.view.state.BookingServiceState
import com.example.task.ui.view.state.CleanerServiceState
import com.example.task.ui.view.state.CleanerServiceUIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CleanerServiceViewModel @Inject constructor() : ViewModel(){

    private val uiState = MutableStateFlow(CleanerServiceState())
    fun consumableState() = uiState.asStateFlow()

    fun onEvent(event: CleanerServiceUIEvent){
        when(event){
            is CleanerServiceUIEvent.AddButtonClick -> {
                uiState.update {
                    it.copy(
                        showBottomSheet = true
                    )
                }
            }

            is CleanerServiceUIEvent.DismissBottomSheet -> {
                uiState.update {
                    it.copy(
                        showBottomSheet = false
                    )
                }
            }
        }
    }
}