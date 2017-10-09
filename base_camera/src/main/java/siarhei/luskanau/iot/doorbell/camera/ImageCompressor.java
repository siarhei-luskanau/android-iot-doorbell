package siarhei.luskanau.iot.doorbell.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import io.reactivex.Observable;

public class ImageCompressor {

    private static final String TAG = ImageCompressor.class.getSimpleName();

    public static int calculateInSampleSize(final BitmapFactory.Options options, final int reqWidth, final int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Observable<byte[]> scale(final Observable<byte[]> sourceObservable, final int maxSize) {
        return sourceObservable.map(bytes -> {
            try {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                final int inSampleSize = calculateInSampleSize(options, maxSize, maxSize);
                // some times the input bytes size is very big
                final int maxImageBytes = options.outHeight * options.outWidth;
                if (inSampleSize > 1 || bytes.length > maxImageBytes) {
                    options.inSampleSize = inSampleSize;
                    options.inJustDecodeBounds = false;

                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                    return outputStream.toByteArray();
                }
            } catch (final Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return bytes;
        });
    }
}
