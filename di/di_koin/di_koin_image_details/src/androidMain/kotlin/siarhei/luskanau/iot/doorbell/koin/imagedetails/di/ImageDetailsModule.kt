package siarhei.luskanau.iot.doorbell.koin.imagedetails.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import siarhei.luskanau.iot.doorbell.koin.common.di.fragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenter
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl

val imageDetailsModule = module {

    fragment { activity: FragmentActivity ->
        ImageDetailsFragment { fragment: Fragment ->
            get { parametersOf(activity, fragment, fragment.arguments) }
        }
    }

    factory<ImageDetailsPresenter> { (_: FragmentActivity, _: Fragment, args: Bundle?) ->
        val doorbellId = "doorbellId"
        val imageId = "imageId"
        ImageDetailsPresenterImpl(
            doorbellId = doorbellId,
            imageId = imageId,
            doorbellRepository = get()
        )
    }
}
