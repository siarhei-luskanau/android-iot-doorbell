package siarhei.luskanau.iot.doorbell.ui.imagelist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.ui.common.R
import siarhei.luskanau.iot.doorbell.ui.common.adapter.BaseRecyclerClickableListAdapter
import siarhei.luskanau.iot.doorbell.ui.common.adapter.BindingViewHolder

class CameraAdapter :
    BaseRecyclerClickableListAdapter<CameraData>(
        DIFF_CALLBACK
    ) {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder =
        BindingViewHolder(inflater.inflate(R.layout.view_item_camera, parent, false))

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        getItem(position).let { item ->
            holder.itemView.findViewById<TextView>(R.id.name).text = item.name ?: item.cameraId
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
