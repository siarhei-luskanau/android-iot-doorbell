package siarhei.luskanau.iot.doorbell.ui.imagelist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import coil.load
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.ui.common.R
import siarhei.luskanau.iot.doorbell.ui.common.adapter.BaseRecyclerClickablePagingAdapter
import siarhei.luskanau.iot.doorbell.ui.common.adapter.BindingViewHolder

class ImageAdapter :
    BaseRecyclerClickablePagingAdapter<ImageData>(
        DIFF_CALLBACK
    ) {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder =
        BindingViewHolder(inflater.inflate(R.layout.view_item_image, parent, false))

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.itemView.findViewById<TextView>(R.id.name).text = item.imageUri
            holder.itemView.findViewById<ImageView>(R.id.imageView).load(item.imageUri) {
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
