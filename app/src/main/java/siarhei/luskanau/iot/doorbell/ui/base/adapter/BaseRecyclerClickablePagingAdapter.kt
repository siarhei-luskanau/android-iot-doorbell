package siarhei.luskanau.iot.doorbell.ui.base.adapter

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseRecyclerClickablePagingAdapter<T, V : RecyclerView.ViewHolder>(
        diffCallback: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, V>(
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