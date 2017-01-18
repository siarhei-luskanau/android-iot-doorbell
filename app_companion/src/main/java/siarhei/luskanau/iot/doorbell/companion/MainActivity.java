package siarhei.luskanau.iot.doorbell.companion;

import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;

import siarhei.luskanau.iot.doorbell.presenter.send.SendImagePresenter;
import siarhei.luskanau.iot.doorbell.presenter.send.SendImageView;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.DaggerImageComponent;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.ImageComponent;

public class MainActivity extends BaseComponentActivity implements SendImageView {

    @Inject
    protected SendImagePresenter sendImagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initializeInjector();

        sendImagePresenter.setView(this);
    }

    private void initializeInjector() {
        ImageComponent imageComponent = DaggerImageComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
        imageComponent.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sendImagePresenter.destroy();
    }

    @Override
    public void showErrorMessage(CharSequence errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
