package siarhei.luskanau.iot.doorbell.companion.doorbells;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.companion.R;
import siarhei.luskanau.iot.doorbell.companion.databinding.ViewDoorbellEntryBinding;

public class DoorbellEntryView extends LinearLayout {

    private ViewDoorbellEntryBinding binding;

    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.view_doorbell_entry, this, true);
    }

    public DoorbellEntryView(Context context) {
        super(context);
    }

    public DoorbellEntryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DoorbellEntryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setItem(DoorbellEntry doorbellEntry) {
        if (doorbellEntry != null) {
            if (!TextUtils.isEmpty(doorbellEntry.getName())) {
                binding.nameTextView.setText(doorbellEntry.getName());
            } else {
                binding.nameTextView.setText(doorbellEntry.getDeviceId());
            }
        }
    }
}
