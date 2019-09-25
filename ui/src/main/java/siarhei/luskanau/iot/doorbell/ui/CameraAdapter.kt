package siarhei.luskanau.iot.doorbell.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.ui.common.adapter.BaseRecyclerClickableListAdapter
import siarhei.luskanau.iot.doorbell.ui.common.adapter.BindingViewHolder
import siarhei.luskanau.iot.doorbell.ui.databinding.ViewItemCameraBinding

class CameraAdapter : BaseRecyclerClickableListAdapter<CameraData, ViewItemCameraBinding>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BindingViewHolder<ViewItemCameraBinding> =
            BindingViewHolder(ViewItemCameraBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(holder: BindingViewHolder<ViewItemCameraBinding>, position: Int) {
        getItem(position).let { item ->
            holder.binding.name.text = item.name?.let { it } ?: run { item.cameraId }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CameraData>() {
            override fun areItemsTheSame(oldItem: CameraData, newItem: CameraData) =
                oldItem.cameraId == newItem.cameraId

            override fun areContentsTheSame(oldItem: CameraData, newItem: CameraData) =
                oldItem == newItem
        }
    }
}
