package com.example.task.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppModalBottomSheet(
    modifier: Modifier = Modifier,
    dismissedOnOutsideTap: Boolean = true,
    shouldDismissOnBackPress: Boolean = true,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { dismissedOnOutsideTap },
    ),
    shape: Shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    backgroundColor: Color = Color.White,
    onDismissRequest: () -> Unit = {},
    onBackPress: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        dragHandle = {},
        shape = shape,
        containerColor = backgroundColor,
        properties =    ModalBottomSheetProperties(
            shouldDismissOnBackPress = shouldDismissOnBackPress,
            securePolicy = SecureFlagPolicy.Inherit,
            isFocusable = false,
        ),
        onDismissRequest = onDismissRequest,
    ) {
        content()
        BackHandler(enabled = !shouldDismissOnBackPress) {
            onBackPress.invoke()
        }
    }
}