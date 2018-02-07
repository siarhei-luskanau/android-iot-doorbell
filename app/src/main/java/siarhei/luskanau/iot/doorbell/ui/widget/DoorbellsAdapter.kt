package siarhei.luskanau.iot.doorbell.ui.widget

import siarhei.luskanau.iot.doorbell.R
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.databinding.ViewDoorbellItemBinding
import siarhei.luskanau.iot.doorbell.ui.base.adapter.BaseRecyclerBindingAdapter
import siarhei.luskanau.iot.doorbell.ui.base.adapter.BindingViewHolder

class DoorbellsAdapter : BaseRecyclerBindingAdapter<DoorbellData, ViewDoorbellItemBinding>() {

    override fun getViewLayout(): Int = R.layout.view_doorbell_item

    override fun onBindViewHolder(holder: BindingViewHolder<ViewDoorbellItemBinding>, position: Int) {
        holder.binding.item = getItem(position)
    }

}
