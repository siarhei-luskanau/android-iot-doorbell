package siarhei.luskanau.iot.doorbell.navigation

import android.os.Bundle
import siarhei.luskanau.iot.doorbell.common.AppNavigationArgs
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragmentArgs

class DefaultAppNavigationArgs : AppNavigationArgs {

    override fun getCameraListFragmentArgs(args: Bundle?): DoorbellData? =
        args?.let { ImageListFragmentArgs.fromBundle(it).doorbellData }

    override fun getImagesFragmentArgs(args: Bundle?): DoorbellData? =
        args?.let { ImageListFragmentArgs.fromBundle(it).doorbellData }

    override fun getDoorbellDataImageDetailsFragmentArgs(args: Bundle?): DoorbellData? =
        args?.let { ImageDetailsFragmentArgs.fromBundle(it).doorbellData }

    override fun getImageDataImageDetailsFragmentArgs(args: Bundle?): ImageData? =
        args?.let { ImageDetailsFragmentArgs.fromBundle(it).imageData }

    override fun buildImageDetailsArgs(doorbellData: DoorbellData, imageData: ImageData): Bundle =
        NavRootDirections.actionImageListToImageDetails(doorbellData, imageData).arguments
}
