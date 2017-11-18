package siarhei.luskanau.iot.doorbell.companion.images;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.List;

import siarhei.luskanau.iot.doorbell.ImageEntry;
import siarhei.luskanau.iot.doorbell.companion.R;
import siarhei.luskanau.iot.doorbell.companion.databinding.ViewImageEntryBinding;
import timber.log.Timber;

public class ImageEntryView extends LinearLayout {

    private final ViewImageEntryBinding binding;

    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.view_image_entry, this, true);
    }

    public ImageEntryView(final Context context) {
        super(context);
    }

    public ImageEntryView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageEntryView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setItem(final ImageEntry imageEntry) {
        if (imageEntry != null) {
            // Display the timestamp
            final CharSequence prettyTime = DateUtils.getRelativeDateTimeString(getContext(),
                    imageEntry.getTimestamp(), DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0);
            binding.textView1.setText(prettyTime);

            // Display the image
            Bitmap bitmap = null;
            if (imageEntry.getImage() != null) {
                try {
                    final byte[] imageBytes = Base64.decode(imageEntry.getImage(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                } catch (final Exception e) {
                    Timber.e(e);
                }
            }
            if (bitmap != null) {
                binding.imageView1.setImageBitmap(bitmap);
            } else {
                binding.imageView1.setImageResource(R.drawable.ic_image);
            }

            // Display the metadata
            if (imageEntry.getAnnotations() != null) {
                final List<String> annotations = imageEntry.getAnnotations();
                final int limit = Math.min(annotations.size(), 3);
                binding.textView2.setText(TextUtils.join("\n", annotations.subList(0, limit)));
            } else {
                binding.textView2.setText(null);
            }
        }
    }
}
