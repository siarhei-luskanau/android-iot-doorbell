package siarhei.luskanau.iot.doorbell.koin.imagedetails.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.koin.common.di.fragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenter
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlideFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlidePresenter
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlidePresenterImpl

val imageDetailsModule = module {

    fragment { activity: FragmentActivity ->
        ImageDetailsFragment { fragment: Fragment ->
            get { parametersOf(activity, fragment, fragment.arguments) }
        }
    }

    factory<ImageDetailsPresenter> { (
        activity: FragmentActivity,
        fragment: Fragment,
        args: Bundle?
    ) ->
        val doorbellId = ImageDetailsFragmentArgs.fromBundle(requireNotNull(args)).doorbellId
        val imageId = ImageDetailsFragmentArgs.fromBundle(args).imageId
        ImageDetailsPresenterImpl(
            appNavigation = get { parametersOf(activity) },
            fragment = fragment,
            doorbellId = doorbellId,
            imageId = imageId
        )
    }

    factory { (_: AppNavigation) ->
        ImageDetailsSlideFragment { fragment: Fragment ->
            get { parametersOf(fragment.arguments) }
        }
    }
    factory<ImageDetailsSlidePresenter> { (args: Bundle?) ->
        val doorbellId = ImageDetailsFragmentArgs.fromBundle(requireNotNull(args)).doorbellId
        val imageId = ImageDetailsFragmentArgs.fromBundle(args).imageId
        ImageDetailsSlidePresenterImpl(
            doorbellId = doorbellId,
            imageId = imageId,
            doorbellRepository = get()
        )
    }
}
