package siarhei.luskanau.iot.doorbell.common

import android.os.Bundle
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface AppNavigationArgs {
    fun getCameraListFragmentArgs(args: Bundle?): DoorbellData?
    fun getImagesFragmentArgs(args: Bundle?): DoorbellData?
    fun getDoorbellDataImageDetailsFragmentArgs(args: Bundle?): DoorbellData?
    fun getImageDataImageDetailsFragmentArgs(args: Bundle?): ImageData?
    fun buildImageDetailsArgs(doorbellData: DoorbellData, imageData: ImageData): Bundle
}
