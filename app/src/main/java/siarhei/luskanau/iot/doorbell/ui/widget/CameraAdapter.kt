package siarhei.luskanau.iot.doorbell.ui.widget

import siarhei.luskanau.iot.doorbell.R
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.databinding.ViewCameraItemBinding
import siarhei.luskanau.iot.doorbell.ui.base.adapter.BaseRecyclerBindingArrayAdapter
import siarhei.luskanau.iot.doorbell.ui.base.adapter.BindingViewHolder

class CameraAdapter : BaseRecyclerBindingArrayAdapter<CameraData, ViewCameraItemBinding>() {

    override fun getViewLayout(): Int = R.layout.view_camera_item

    override fun onBindViewHolder(holder: BindingViewHolder<ViewCameraItemBinding>, position: Int) {
        holder.binding.item = getItem(position)
    }

}
