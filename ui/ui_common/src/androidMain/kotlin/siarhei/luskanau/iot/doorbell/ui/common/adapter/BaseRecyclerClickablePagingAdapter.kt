package siarhei.luskanau.iot.doorbell.ui.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class BaseRecyclerClickablePagingAdapter<T : Any>(diffCallback: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, BindingViewHolder>(diffCallback) {

    var onItemClickListener: (
        context: Context,
        holder: BindingViewHolder,
        position: Int
    ) -> Unit = { _, _, _ -> }

    @Suppress("UNCHECKED_CAST")
    private val innerClickListener = View.OnClickListener { view ->
        val holder = view.tag as BindingViewHolder
        onItemClickListener(holder.itemView.context, holder, holder.bindingAdapterPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val holder = onCreateViewHolder(inflater, parent, viewType)
        holder.itemView.setOnClickListener(innerClickListener)
        holder.itemView.tag = holder
        return holder
    }

    abstract fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder

    fun getItemAtPosition(position: Int) = super.getItem(position)
}
