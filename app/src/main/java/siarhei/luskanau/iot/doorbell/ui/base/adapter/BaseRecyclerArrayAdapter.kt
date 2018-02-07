package siarhei.luskanau.iot.doorbell.ui.base.adapter

import android.support.v7.widget.RecyclerView
import java.util.*

abstract class BaseRecyclerArrayAdapter<T, V : RecyclerView.ViewHolder> : BaseRecyclerClickableAdapter<V>() {

    private val items = ArrayList<T>()

    fun addItem(item: T) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun addItem(position: Int, item: T) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    fun addItems(items: Collection<T>?) {
        val oldSize = itemCount
        this.items.addAll(items ?: emptyList())
        notifyItemRangeInserted(oldSize, itemCount - oldSize)
    }

    fun addItems(position: Int, items: Collection<T>?) {
        this.items.addAll(position, items ?: emptyList())
        notifyItemRangeInserted(position, items?.size ?: 0)
    }

    fun setItem(position: Int, item: T) {
        items[position] = item
        notifyItemChanged(position)
    }

    fun getItems(): List<T> = items

    fun setItems(items: Collection<T>?) {
        this.items.clear()
        this.items.addAll(items ?: emptyList())
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    fun getItem(position: Int): T = items[position]
}