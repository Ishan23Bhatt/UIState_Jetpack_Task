package com.example.task.ui.view.state

data class CleanerServiceState(
    val showBottomSheet: Boolean = false,
)

sealed class CleanerServiceUIEvent {
    data object AddButtonClick: CleanerServiceUIEvent()
    data object DismissBottomSheet: CleanerServiceUIEvent()
}
