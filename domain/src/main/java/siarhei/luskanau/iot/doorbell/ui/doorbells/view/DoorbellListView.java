package siarhei.luskanau.iot.doorbell.ui.doorbells.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.widget.Toast;

import java.util.List;

import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.domain.R;
import siarhei.luskanau.iot.doorbell.domain.databinding.ViewDoorbellListBinding;
import siarhei.luskanau.iot.doorbell.ui.base.BindingFrameLayout;

public class DoorbellListView extends BindingFrameLayout<ViewDoorbellListBinding> implements IDoorbellListView {

    private DoorbellEntryAdapter adapter;
    private OnDoorbellEntryClickListener listener;

    public DoorbellListView(final Context context) {
        super(context);
    }

    public DoorbellListView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public DoorbellListView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    public DoorbellListView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.view_doorbell_list;
    }

    @Override
    protected void onViewCreated(final Context context) {
        super.onViewCreated(context);

        adapter = new DoorbellEntryAdapter();
        adapter.setOnItemClickListener((c, holder, position) -> {
            if (listener != null) {
                listener.onDoorbellEntryClicked(adapter.getItem(position));
            }
        });

        bindings.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        bindings.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDoorbellListUpdated(final List<DoorbellEntry> doorbellEntries) {
        adapter.setData(doorbellEntries);
    }

    @Override
    public void showErrorMessage(final String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    public void setOnDoorbellEntryClickListener(final OnDoorbellEntryClickListener listener) {
        this.listener = listener;
    }

    public interface OnDoorbellEntryClickListener {

        void onDoorbellEntryClicked(DoorbellEntry doorbellEntry);
    }
}
