package siarhei.luskanau.iot.doorbell.ui.widget

import androidx.recyclerview.widget.DiffUtil
import siarhei.luskanau.iot.doorbell.R
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.databinding.ViewCameraItemBinding
import siarhei.luskanau.iot.doorbell.ui.base.adapter.BaseRecyclerBindingListAdapter
import siarhei.luskanau.iot.doorbell.ui.base.adapter.BindingViewHolder

class CameraAdapter : BaseRecyclerBindingListAdapter<CameraData, ViewCameraItemBinding>(
        DIFF_CALLBACK
) {

    override fun getViewLayout(): Int = R.layout.view_camera_item

    override fun onBindViewHolder(holder: BindingViewHolder<ViewCameraItemBinding>, position: Int) {
        holder.binding.item = getItem(position)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CameraData>() {
            override fun areItemsTheSame(oldItem: CameraData, newItem: CameraData) = oldItem.cameraId == newItem.cameraId
            override fun areContentsTheSame(oldItem: CameraData, newItem: CameraData) = oldItem == newItem
        }

    }

}
