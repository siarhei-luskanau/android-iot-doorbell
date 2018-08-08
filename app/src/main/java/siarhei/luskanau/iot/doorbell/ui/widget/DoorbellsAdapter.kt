package siarhei.luskanau.iot.doorbell.ui.widget

import androidx.recyclerview.widget.DiffUtil
import siarhei.luskanau.iot.doorbell.R
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.databinding.ViewDoorbellItemBinding
import siarhei.luskanau.iot.doorbell.ui.base.adapter.BaseRecyclerBindingPagingAdapter
import siarhei.luskanau.iot.doorbell.ui.base.adapter.BindingViewHolder

class DoorbellsAdapter : BaseRecyclerBindingPagingAdapter<DoorbellData, ViewDoorbellItemBinding>(
        DIFF_CALLBACK
) {

    override fun getViewLayout(): Int = R.layout.view_doorbell_item

    override fun onBindViewHolder(holder: BindingViewHolder<ViewDoorbellItemBinding>, position: Int) {
        holder.binding.item = getItem(position)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DoorbellData>() {
            override fun areItemsTheSame(oldItem: DoorbellData, newItem: DoorbellData) = oldItem.doorbellId == newItem.doorbellId
            override fun areContentsTheSame(oldItem: DoorbellData, newItem: DoorbellData) = oldItem == newItem
        }

    }

}
