package siarhei.luskanau.iot.doorbell.koin.imagedetails.di

import androidx.fragment.app.Fragment
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.koin.common.di.fragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenter
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlideFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlidePresenter
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlidePresenterImpl

val imageDetailsModule = module {

    fragment { appNavigation: AppNavigation ->
        ImageDetailsFragment { fragment: Fragment ->
            val doorbellData = fragment.arguments?.let { args ->
                ImageDetailsFragmentArgs.fromBundle(args).doorbellData
            }
            val imageData = fragment.arguments?.let { args ->
                ImageDetailsFragmentArgs.fromBundle(args).imageData
            }
            get { parametersOf(appNavigation, fragment, doorbellData, imageData) }
        }
    }

    factory<ImageDetailsPresenter> { (
        appNavigation: AppNavigation,
        fragment: Fragment,
        doorbellData: DoorbellData,
        imageData: ImageData
    ) ->
        ImageDetailsPresenterImpl(
            appNavigation = appNavigation,
            fragment = fragment,
            doorbellData = doorbellData,
            imageData = imageData
        )
    }

    factory { (_: AppNavigation) ->
        ImageDetailsSlideFragment { fragment: Fragment ->
            val imageData = fragment.arguments?.let { args ->
                ImageDetailsFragmentArgs.fromBundle(args).imageData
            }
            get { parametersOf(imageData) }
        }
    }
    factory<ImageDetailsSlidePresenter> { (imageData: ImageData) ->
        val imageDetailsSlidePresenterImpl = ImageDetailsSlidePresenterImpl(
            imageData = imageData
        )
        imageDetailsSlidePresenterImpl
    }
}
