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
    public BindableViewHolder onCreateViewHolder(final LayoutInflater inflater, final ViewGroup parent, final int viewType) {
        final ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_image_entry, parent, false);
        return new BindableViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(final BindableViewHolder holder, final int position) {
        final ListItemImageEntryBinding binding = (ListItemImageEntryBinding) holder.getBindings();
        final ImageEntry item = getItem(position);
        binding.item.setItem(item);
    }
}
