package siarhei.luskanau.iot.doorbell.kodein.di

import androidx.fragment.app.Fragment
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

data class ImageDetailsPresenterArgs(
    val appNavigation: AppNavigation,
    val fragment: Fragment,
    val doorbellData: DoorbellData?,
    val imageData: ImageData?
)
