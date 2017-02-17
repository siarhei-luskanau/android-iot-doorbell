package siarhei.luskanau.iot.doorbell.companion.images;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import siarhei.luskanau.iot.doorbell.ImageEntry;
import siarhei.luskanau.iot.doorbell.companion.R;
import siarhei.luskanau.iot.doorbell.companion.databinding.ListItemImageEntryBinding;
import siarhei.luskanau.iot.doorbell.ui.BaseRecyclerArrayAdapter;
import siarhei.luskanau.iot.doorbell.ui.BindableViewHolder;

public class ImageEntryAdapter extends BaseRecyclerArrayAdapter<ImageEntry, BindableViewHolder> {

    @Override
    public BindableViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_image_entry, parent, false);
        return new BindableViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BindableViewHolder holder, int position) {
        ListItemImageEntryBinding binding = (ListItemImageEntryBinding) holder.getBindings();
        ImageEntry item = getItem(position);
        binding.item.setItem(item);
    }
}
