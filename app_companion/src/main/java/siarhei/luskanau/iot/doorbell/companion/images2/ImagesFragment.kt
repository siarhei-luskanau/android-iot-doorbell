package siarhei.luskanau.iot.doorbell.companion.images2

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import siarhei.luskanau.iot.doorbell.DomainConstants
import siarhei.luskanau.iot.doorbell.Injectable
import siarhei.luskanau.iot.doorbell.companion.NavigationController
import siarhei.luskanau.iot.doorbell.domain.R
import javax.inject.Inject

class ImagesFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigationController: NavigationController

    lateinit var viewModel: ImagesViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val deviceId: String = arguments?.getString(DomainConstants.DEVICE_ID) ?: ""
        val deviceName: String = arguments?.getString(DomainConstants.NAME) ?: ""
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ImagesViewModel::class.java)
        viewModel.init(deviceId)

        viewModel.loadImages()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_images, container, false)
    }

}