package siarhei.luskanau.iot.doorbell.navigation

import android.os.Bundle
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.doomain.AppNavigationArgs
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragmentDirections
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragmentArgs

class DefaultAppNavigationArgs : AppNavigationArgs {

    override fun createCameraListFragmentArgs(doorbellData: DoorbellData): Bundle =
        DoorbellListFragmentDirections.actionDoorbellListToImageList(doorbellData).arguments

    override fun getCameraListFragmentArgs(args: Bundle?): DoorbellData? =
        args?.let { ImageListFragmentArgs.fromBundle(it).doorbellData }

    override fun getImagesFragmentArgs(args: Bundle?): DoorbellData? =
        args?.let { ImageListFragmentArgs.fromBundle(it).doorbellData }

    override fun getImageDetailsFragmentArgs(args: Bundle?): ImageData? =
        args?.let { ImageDetailsFragmentArgs.fromBundle(it).imageData }
}