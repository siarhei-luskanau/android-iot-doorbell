package siarhei.luskanau.iot.doorbell.ui.widget

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import siarhei.luskanau.iot.doorbell.R

object DataBinder {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setImageUrl(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
                .load(url)
                .apply(
                        RequestOptions()
                                .placeholder(R.drawable.ic_image)
                                .error(R.drawable.ic_error_outline)
                )
                .into(imageView)
    }

}