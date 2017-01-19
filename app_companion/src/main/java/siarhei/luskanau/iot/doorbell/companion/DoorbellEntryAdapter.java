package siarhei.luskanau.iot.doorbell.companion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class DoorbellEntryAdapter extends FirebaseRecyclerAdapter<DoorbellEntry, DoorbellEntryAdapter.DoorbellEntryViewHolder> {

    private static final String TAG = "DoorbellEntryAdapter";

    /**
     * ViewHolder for each doorbell entry
     */
    public static class DoorbellEntryViewHolder extends RecyclerView.ViewHolder {

        public final ImageView image;
        public final TextView time;
        public final TextView metadata;

        public DoorbellEntryViewHolder(View itemView) {
            super(itemView);

            this.image = (ImageView) itemView.findViewById(R.id.imageView1);
            this.time = (TextView) itemView.findViewById(R.id.textView1);
            this.metadata = (TextView) itemView.findViewById(R.id.textView2);
        }
    }

    private Context mApplicationContext;

    public DoorbellEntryAdapter(Context context, DatabaseReference ref) {
        super(DoorbellEntry.class, R.layout.doorbell_entry, DoorbellEntryViewHolder.class, ref);

        mApplicationContext = context.getApplicationContext();
    }

    @Override
    protected void populateViewHolder(DoorbellEntryViewHolder viewHolder, DoorbellEntry model, int position) {
        // Display the timestamp
        try {
            CharSequence prettyTime = DateUtils.getRelativeDateTimeString(mApplicationContext,
                    model.getTimestamp(), DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0);
            viewHolder.time.setText(prettyTime);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            viewHolder.time.setText(e.getMessage());
        }

        // Display the image
        Bitmap bitmap = null;
        if (model.getImage() != null) {
            // Decode image data encoded by the Cloud Vision library
            byte[] imageBytes = Base64.decode(model.getImage(), Base64.NO_WRAP | Base64.URL_SAFE);
            bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }
        if (bitmap != null) {
            viewHolder.image.setImageBitmap(bitmap);
        } else {
            Drawable placeholder =
                    ContextCompat.getDrawable(mApplicationContext, R.drawable.ic_image);
            viewHolder.image.setImageDrawable(placeholder);
        }

        // Display the metadata
        if (model.getAnnotations() != null) {
            List<String> keywords = new ArrayList<>(model.getAnnotations().keySet());

            int limit = Math.min(keywords.size(), 3);
            viewHolder.metadata.setText(TextUtils.join("\n", keywords.subList(0, limit)));
        } else {
            viewHolder.metadata.setText("no annotations yet");
        }
    }

}
