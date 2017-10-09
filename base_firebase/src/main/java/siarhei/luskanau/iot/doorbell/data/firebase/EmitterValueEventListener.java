package siarhei.luskanau.iot.doorbell.data.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.ObservableEmitter;

public abstract class EmitterValueEventListener<T> implements ValueEventListener {

    private static final String TAG = EmitterValueEventListener.class.getSimpleName();
    private static final Gson GSON = new Gson();

    private final ObservableEmitter<T> emitter;
    private final Query query;

    public EmitterValueEventListener(final ObservableEmitter<T> emitter, final Query query) {
        this.emitter = emitter;
        this.query = query;
    }

    @Override
    public void onDataChange(final DataSnapshot dataSnapshot) {
        if (!isDisposed(emitter, this, query)) {
            try {
                final Type genericSuperclass = getClass().getGenericSuperclass();
                final Type typeArgument = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
                final Object value = dataSnapshot.getValue();
                final T typedValue = GSON.fromJson(GSON.toJson(value), typeArgument);
                final T checkedValue = checkValue(typedValue);
                emitter.onNext(checkedValue);
            } catch (final Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    protected T checkValue(final T value) {
        return value;
    }

    @Override
    public void onCancelled(final DatabaseError databaseError) {
        if (!isDisposed(emitter, this, query)) {
            emitter.onError(databaseError.toException());
        }
        Log.w(TAG, databaseError.toString(), databaseError.toException());
    }

    private boolean isDisposed(final ObservableEmitter<T> emitter, final ValueEventListener valueEventListener, final Query query) {
        final boolean isDisposed = emitter.isDisposed();
        if (isDisposed) {
            query.removeEventListener(valueEventListener);
        }
        return isDisposed;
    }
}
