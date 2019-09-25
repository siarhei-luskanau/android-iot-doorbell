package siarhei.luskanau.iot.doorbell.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.ui.common.adapter.BaseRecyclerClickablePagingAdapter
import siarhei.luskanau.iot.doorbell.ui.common.adapter.BindingViewHolder
import siarhei.luskanau.iot.doorbell.ui.databinding.ViewItemImageBinding

class ImageAdapter : BaseRecyclerClickablePagingAdapter<ImageData, ViewItemImageBinding>(
        DIFF_CALLBACK
) {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BindingViewHolder<ViewItemImageBinding> =
            BindingViewHolder(ViewItemImageBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(holder: BindingViewHolder<ViewItemImageBinding>, position: Int) {
        getItem(position)?.let { item ->
            Glide
                    .with(holder.binding.imageView)
                    .load(item.imageUri)
                    .into(holder.binding.imageView)
            holder.binding.name.text = item.imageUri
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
