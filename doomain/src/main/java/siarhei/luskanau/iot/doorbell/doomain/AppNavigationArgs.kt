package siarhei.luskanau.iot.doorbell.doomain

import android.os.Bundle
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface AppNavigationArgs {
    fun createCameraListFragmentArgs(doorbellData: DoorbellData): Bundle
    fun getCameraListFragmentArgs(args: Bundle?): DoorbellData?
    fun getImagesFragmentArgs(args: Bundle?): DoorbellData?
    fun getImageDetailsFragmentArgs(args: Bundle?): ImageData?
}