package siarhei.luskanau.iot.doorbell.ui.imagelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import coil.api.load
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.ui.common.R
import siarhei.luskanau.iot.doorbell.ui.common.adapter.BaseRecyclerClickablePagingAdapter
import siarhei.luskanau.iot.doorbell.ui.common.adapter.BindingViewHolder
import siarhei.luskanau.iot.doorbell.ui.common.databinding.ViewItemImageBinding

class ImageAdapter : BaseRecyclerClickablePagingAdapter<ImageData, ViewItemImageBinding>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder<ViewItemImageBinding> =
        BindingViewHolder(ViewItemImageBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(holder: BindingViewHolder<ViewItemImageBinding>, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.name.text = item.imageUri
            holder.binding.imageView.load(item.imageUri) {
                placeholder(R.drawable.ic_image)
            }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ImageData>() {
            override fun areItemsTheSame(oldItem: ImageData, newItem: ImageData) =
                oldItem.imageId == newItem.imageId

            override fun areContentsTheSame(oldItem: ImageData, newItem: ImageData) =
                oldItem == newItem
        }
    }
}
