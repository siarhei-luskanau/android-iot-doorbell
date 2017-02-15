package siarhei.luskanau.iot.doorbell.companion.doorbells;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.companion.R;
import siarhei.luskanau.iot.doorbell.companion.databinding.ListItemDoorbellEntryBinding;
import siarhei.luskanau.iot.doorbell.ui.BaseRecyclerArrayAdapter;
import siarhei.luskanau.iot.doorbell.ui.BindableViewHolder;

public class DoorbellEntryAdapter extends BaseRecyclerArrayAdapter<DoorbellEntry, BindableViewHolder> {

    @Override
    public BindableViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_doorbell_entry, parent, false);
        return new BindableViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BindableViewHolder holder, int position) {
        ListItemDoorbellEntryBinding binding = (ListItemDoorbellEntryBinding) holder.getBindings();
        DoorbellEntry item = getItem(position);
        binding.item.setItem(item);
    }
}
