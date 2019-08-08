package siarhei.luskanau.iot.doorbell.ui.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerClickableListAdapter<T, V : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, V>(
        diffCallback
) {

    var onItemClickListener: (context: Context, holder: V, position: Int) -> Unit = { _, _, _ -> }

    @Suppress("UNCHECKED_CAST")
    private val innerClickListener = View.OnClickListener { view ->
        val holder = view.tag as V
        onItemClickListener(holder.itemView.context, holder, holder.adapterPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
        val inflater = LayoutInflater.from(parent.context)
        val holder = onCreateViewHolder(inflater, parent, viewType)
        holder.itemView.setOnClickListener(innerClickListener)
        holder.itemView.tag = holder
        return holder
    }

    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): V
}
