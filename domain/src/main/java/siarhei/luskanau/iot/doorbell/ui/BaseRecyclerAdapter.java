package siarhei.luskanau.iot.doorbell.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseRecyclerAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    private OnItemClickListener<V> itemClickListener;

    private View.OnClickListener innerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                V holder = (V) v.getTag();
                itemClickListener.onClick(holder.itemView.getContext(),
                        holder, holder.getAdapterPosition());
            }
        }
    };

    public BaseRecyclerAdapter() {
    }

    public BaseRecyclerAdapter(OnItemClickListener<V> listener) {
        this.itemClickListener = listener;
    }

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        V holder = onCreateViewHolder(inflater, parent, viewType);
        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(innerClickListener);
        }
        holder.itemView.setTag(holder);
        return holder;
    }

    public abstract V onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);

    protected View.OnClickListener getInnerClickListener() {
        return innerClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<V> listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener<V extends RecyclerView.ViewHolder> {
        void onClick(Context context, V holder, int position);
    }
}