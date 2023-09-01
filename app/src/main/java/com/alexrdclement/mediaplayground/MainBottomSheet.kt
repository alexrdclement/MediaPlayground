package com.alexrdclement.mediaplayground

sealed class MainBottomSheet {
    data object MediaSourceChooserBottomSheet : MainBottomSheet()
}
