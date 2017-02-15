package siarhei.luskanau.iot.doorbell.presenter.doorbells;

import java.util.List;

import siarhei.luskanau.iot.doorbell.DoorbellEntry;

public interface DoorbellListView {

    void onDoorbellListUpdated(List<DoorbellEntry> doorbellEntries);

    void showErrorMessage(CharSequence errorMessage);
}
