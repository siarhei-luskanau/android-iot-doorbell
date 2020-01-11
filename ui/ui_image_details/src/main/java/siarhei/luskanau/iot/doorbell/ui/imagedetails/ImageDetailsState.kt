package siarhei.luskanau.iot.doorbell.ui.imagedetails

import androidx.viewpager2.adapter.FragmentStateAdapter

sealed class ImageDetailsState

data class NormalImageDetailsState(val adapter: FragmentStateAdapter) : ImageDetailsState()

data class ErrorImageDetailsState(val error: Throwable) : ImageDetailsState()
