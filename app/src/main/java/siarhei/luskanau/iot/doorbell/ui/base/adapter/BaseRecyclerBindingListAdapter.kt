package siarhei.luskanau.iot.doorbell.ui.base.adapter

import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class BaseRecyclerBindingListAdapter<T, B : ViewDataBinding>(
        diffCallback: DiffUtil.ItemCallback<T>
) : BaseRecyclerClickableListAdapter<T, BindingViewHolder<B>>(
        diffCallback
) {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BindingViewHolder<B> =
            BindingViewHolder.create(LayoutInflater.from(parent.context), getViewLayout(), parent)

    @LayoutRes
    protected abstract fun getViewLayout(): Int

    public override fun getItem(position: Int): T = super.getItem(position)

}
