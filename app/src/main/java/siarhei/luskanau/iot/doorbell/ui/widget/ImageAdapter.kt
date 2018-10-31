package siarhei.luskanau.iot.doorbell.ui.widget

import androidx.recyclerview.widget.DiffUtil
import siarhei.luskanau.iot.doorbell.R
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.databinding.ViewImageItemBinding
import siarhei.luskanau.iot.doorbell.ui.base.adapter.BaseRecyclerBindingPagingAdapter
import siarhei.luskanau.iot.doorbell.ui.base.adapter.BindingViewHolder

class ImageAdapter : BaseRecyclerBindingPagingAdapter<ImageData, ViewImageItemBinding>(
        DIFF_CALLBACK
) {

    override fun getViewLayout(): Int = R.layout.view_image_item

    override fun onBindViewHolder(holder: BindingViewHolder<ViewImageItemBinding>, position: Int) {
        holder.binding.item = getItem(position)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ImageData>() {
            override fun areItemsTheSame(oldItem: ImageData, newItem: ImageData) = oldItem.imageId == newItem.imageId
            override fun areContentsTheSame(oldItem: ImageData, newItem: ImageData) = oldItem == newItem
        }
    }
}
