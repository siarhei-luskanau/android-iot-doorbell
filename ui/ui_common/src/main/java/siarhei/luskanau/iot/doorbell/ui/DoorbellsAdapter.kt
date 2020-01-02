package siarhei.luskanau.iot.doorbell.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.ui.common.adapter.BaseRecyclerClickablePagingAdapter
import siarhei.luskanau.iot.doorbell.ui.common.adapter.BindingViewHolder
import siarhei.luskanau.iot.doorbell.ui.common.databinding.ViewItemDoorbellBinding

class DoorbellsAdapter : BaseRecyclerClickablePagingAdapter<DoorbellData, ViewItemDoorbellBinding>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder<ViewItemDoorbellBinding> =
        BindingViewHolder(ViewItemDoorbellBinding.inflate(inflater, parent, false))

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
