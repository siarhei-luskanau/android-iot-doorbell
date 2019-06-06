package siarhei.luskanau.iot.doorbell.ui

import androidx.recyclerview.widget.DiffUtil
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.ui.common.adapter.BaseRecyclerBindingPagingAdapter
import siarhei.luskanau.iot.doorbell.ui.common.adapter.BindingViewHolder
import siarhei.luskanau.iot.doorbell.ui.databinding.ViewItemDoorbellBinding

class DoorbellsAdapter : BaseRecyclerBindingPagingAdapter<DoorbellData, ViewItemDoorbellBinding>(
    DIFF_CALLBACK
) {

    override fun getViewLayout(): Int = R.layout.view_item_doorbell

    override fun onBindViewHolder(
        holder: BindingViewHolder<ViewItemDoorbellBinding>,
        position: Int
    ) {
        getItem(position)?.let { item ->
            holder.binding.name.text = item.name?.let { it } ?: run { item.doorbellId }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DoorbellData>() {
            override fun areItemsTheSame(oldItem: DoorbellData, newItem: DoorbellData) =
                oldItem.doorbellId == newItem.doorbellId

            override fun areContentsTheSame(oldItem: DoorbellData, newItem: DoorbellData) =
                oldItem == newItem
        }
    }
}
