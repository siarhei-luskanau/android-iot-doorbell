package siarhei.luskanau.iot.doorbell.ui.doorbells.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.domain.R;
import siarhei.luskanau.iot.doorbell.domain.databinding.ViewDoorbellEntryBinding;

public class DoorbellEntryView extends LinearLayout {

    private final ViewDoorbellEntryBinding binding;

    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.view_doorbell_entry, this, true);
    }

    public DoorbellEntryView(final Context context) {
        super(context);
    }

    public DoorbellEntryView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public DoorbellEntryView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setItem(final DoorbellEntry doorbellEntry) {
        if (doorbellEntry != null) {
            if (!TextUtils.isEmpty(doorbellEntry.getName())) {
                binding.nameTextView.setText(doorbellEntry.getName());
            } else {
                binding.nameTextView.setText(doorbellEntry.getDeviceId());
            }
        }
    }
}
