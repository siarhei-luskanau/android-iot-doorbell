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

    private ObservableEmitter<T> emitter;
    private Query query;

    public EmitterValueEventListener(ObservableEmitter<T> emitter, Query query) {
        this.emitter = emitter;
        this.query = query;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (!isDisposed(emitter, this, query)) {
            try {
                Type genericSuperclass = getClass().getGenericSuperclass();
                Type typeArgument = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
                Object value = dataSnapshot.getValue();
                T typedValue = GSON.fromJson(GSON.toJson(value), typeArgument);
                T checkedValue = checkValue(typedValue);
                emitter.onNext(checkedValue);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    protected T checkValue(T value) {
        return value;
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        if (!isDisposed(emitter, this, query)) {
            emitter.onError(databaseError.toException());
        }
        Log.w(TAG, databaseError.toString(), databaseError.toException());
    }

    private boolean isDisposed(ObservableEmitter<T> emitter, ValueEventListener valueEventListener, Query query) {
        boolean isDisposed = emitter.isDisposed();
        if (isDisposed) {
            query.removeEventListener(valueEventListener);
        }
        return isDisposed;
    }
}
