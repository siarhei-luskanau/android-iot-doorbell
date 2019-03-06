package siarhei.luskanau.iot.doorbell.ui.images

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.davemorrissey.labs.subscaleview.ImageSource
import siarhei.luskanau.iot.doorbell.R
import siarhei.luskanau.iot.doorbell.databinding.FragmentImageDetailsBinding
import siarhei.luskanau.iot.doorbell.ui.base.BaseAppFragment
import javax.inject.Inject

class ImageDetailsFragment : BaseAppFragment<FragmentImageDetailsBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val deviceId: String by lazy {
        ImagesFragmentArgs.fromBundle(arguments ?: Bundle()).doorbellId
    }
    private val deviceName: String by lazy {
        ImagesFragmentArgs.fromBundle(arguments ?: Bundle()).deviceName
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.subsamplingScaleImageView.setImage(ImageSource.uri(Uri.parse("http://drscdn.500px.org/photo/296116069/q%3D80_m%3D2000/v2?webp=true&sig=f9d41967d65928e9f1685f250f7e3d81e974974be7403bd70b4838ed00761e34")))
    }

    override fun getViewLayout(): Int = R.layout.fragment_image_details

    override fun getActionBarTitle(): String =
            if (deviceName.isNotBlank()) {
                deviceName
            } else {
                deviceId
            }
}