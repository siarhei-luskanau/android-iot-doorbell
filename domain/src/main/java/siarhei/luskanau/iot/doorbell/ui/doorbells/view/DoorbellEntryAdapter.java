package siarhei.luskanau.iot.doorbell.ui.doorbells.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.domain.R;
import siarhei.luskanau.iot.doorbell.domain.databinding.ListItemDoorbellEntryBinding;
import siarhei.luskanau.iot.doorbell.ui.BaseRecyclerArrayAdapter;
import siarhei.luskanau.iot.doorbell.ui.BindableViewHolder;

public class DoorbellEntryAdapter extends BaseRecyclerArrayAdapter<DoorbellEntry, BindableViewHolder> {

    @Override
    public BindableViewHolder onCreateViewHolder(final LayoutInflater inflater, final ViewGroup parent, final int viewType) {
        final ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_doorbell_entry, parent, false);
        return new BindableViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(final BindableViewHolder holder, final int position) {
        final ListItemDoorbellEntryBinding binding = (ListItemDoorbellEntryBinding) holder.getBindings();
        final DoorbellEntry item = getItem(position);
        binding.item.setItem(item);
    }
}
