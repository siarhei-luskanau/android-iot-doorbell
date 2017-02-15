package siarhei.luskanau.iot.doorbell.ui;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseRecyclerArrayAdapter<T, V extends RecyclerView.ViewHolder> extends BaseRecyclerAdapter<V> {

    private List<T> mData = new ArrayList<>();

    public BaseRecyclerArrayAdapter() {
    }

    public BaseRecyclerArrayAdapter(OnItemClickListener<V> listener) {
        super(listener);
    }

    public void addItem(T item) {
        mData.add(item);
        notifyItemInserted(mData.size() - 1);
    }

    public void addItem(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    public void addItems(Collection<? extends T> items) {
        if (items != null) {
            int oldSize = getItemCount();
            mData.addAll(items);
            notifyItemRangeInserted(oldSize, getItemCount() - oldSize);
        }
    }

    public void addItems(int position, Collection<? extends T> items) {
        if (items != null) {
            mData.addAll(position, items);
            notifyItemRangeInserted(position, items.size());
        }
    }

    public void setItem(int position, T item) {
        mData.set(position, item);
        notifyItemChanged(position);
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(Collection<? extends T> data) {
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

    public T getItem(int position) {
        return mData.get(position);
    }
}