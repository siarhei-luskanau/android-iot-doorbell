package siarhei.luskanau.iot.doorbell.ui.doorbells.view;

import java.util.List;

import siarhei.luskanau.iot.doorbell.DoorbellEntry;

public interface IDoorbellListView {

    void onDoorbellListUpdated(List<DoorbellEntry> doorbellEntries);

    void showErrorMessage(String errorMessage);
}
