package siarhei.luskanau.iot.doorbell.ui.widget

import siarhei.luskanau.iot.doorbell.R
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.databinding.ViewImageItemBinding
import siarhei.luskanau.iot.doorbell.ui.base.adapter.BaseRecyclerBindingAdapter
import siarhei.luskanau.iot.doorbell.ui.base.adapter.BindingViewHolder

class ImageAdapter : BaseRecyclerBindingAdapter<ImageData, ViewImageItemBinding>() {

    override fun getViewLayout(): Int = R.layout.view_image_item

    override fun onBindViewHolder(holder: BindingViewHolder<ViewImageItemBinding>, position: Int) {
        holder.binding.item = getItem(position)
    }

}
