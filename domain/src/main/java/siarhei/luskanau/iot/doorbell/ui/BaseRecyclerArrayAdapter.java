package siarhei.luskanau.iot.doorbell.ui;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseRecyclerArrayAdapter<T, V extends RecyclerView.ViewHolder> extends BaseRecyclerAdapter<V> {

    private final List<T> mData = new ArrayList<>();

    public BaseRecyclerArrayAdapter() {
    }

    public BaseRecyclerArrayAdapter(final OnItemClickListener<V> listener) {
        super(listener);
    }

    public void addItem(final T item) {
        mData.add(item);
        notifyItemInserted(mData.size() - 1);
    }

    public void addItem(final int position, final T item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    public void addItems(final Collection<? extends T> items) {
        if (items != null) {
            final int oldSize = getItemCount();
            mData.addAll(items);
            notifyItemRangeInserted(oldSize, getItemCount() - oldSize);
        }
    }

    public void addItems(final int position, final Collection<? extends T> items) {
        if (items != null) {
            mData.addAll(position, items);
            notifyItemRangeInserted(position, items.size());
        }
    }

    public void setItem(final int position, final T item) {
        mData.set(position, item);
        notifyItemChanged(position);
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(final Collection<? extends T> data) {
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public T getItem(final int position) {
        return mData.get(position);
    }
}