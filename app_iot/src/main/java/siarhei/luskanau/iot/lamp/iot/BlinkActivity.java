package siarhei.luskanau.iot.lamp.iot;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

import javax.inject.Inject;

import siarhei.luskanau.iot.lamp.iot.dagger.component.DaggerLampComponent;
import siarhei.luskanau.iot.lamp.iot.dagger.component.LampComponent;
import siarhei.luskanau.iot.doorbell.presenter.listen.ListenLampStatePresenter;
import siarhei.luskanau.iot.doorbell.presenter.listen.ListenLampStateView;
import siarhei.luskanau.iot.doorbell.presenter.send.SendLampStatePresenter;
import siarhei.luskanau.iot.doorbell.presenter.send.SendLampStateView;

public class BlinkActivity extends BaseComponentActivity implements ListenLampStateView, SendLampStateView {

    private static final String TAG = BlinkActivity.class.getSimpleName();
    private static final String GPIO_LAMP = "BCM6";
    private static final String GPIO_BUTTON = "BCM22";

    @Inject
    protected ListenLampStatePresenter listenLampStatePresenter;
    @Inject
    protected SendLampStatePresenter sendLampStatePresenter;

    private Gpio lampGpio;
    private Gpio buttonGpio;
    private SwitchCompat switchCompat;
    private boolean lampState;
    private long toggleLastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Starting BlinkActivity");
        this.initializeInjector();

        listenLampStatePresenter.setView(this);
        sendLampStatePresenter.setView(this);

        try {
            setContentView(R.layout.activity_main);
            switchCompat = (SwitchCompat) findViewById(R.id.switchCompat);
            switchCompat.setOnClickListener(v -> {
                boolean isChecked = switchCompat.isChecked();
                sendLampStatePresenter.sendLampState(isChecked);
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        PeripheralManagerService service = new PeripheralManagerService();
        try {
            buttonGpio = service.openGpio(GPIO_BUTTON);
            buttonGpio.setDirection(Gpio.DIRECTION_IN);
            buttonGpio.setEdgeTriggerType(Gpio.EDGE_FALLING);
            buttonGpio.registerGpioCallback(new GpioCallback() {
                @Override
                public boolean onGpioEdge(Gpio gpio) {
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - toggleLastTime > 200){
                        toggleLastTime = currentTimeMillis;
                        Log.i(TAG, "GPIO changed, button pressed");
                        boolean newLampState = !lampState;
                        showLampState(newLampState);
                        sendLampStatePresenter.sendLampState(newLampState);
                    }

                    // Return true to continue listening to events
                    return true;
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }

        try {
            lampGpio = service.openGpio(GPIO_LAMP);
            lampGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            Log.i(TAG, "Start blinking LED GPIO pin");
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    private void initializeInjector() {
        LampComponent lampComponent = DaggerLampComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
        lampComponent.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        listenLampStatePresenter.listenLampState();
    }

    @Override
    protected void onPause() {
        super.onPause();

        listenLampStatePresenter.destroy();
        sendLampStatePresenter.destroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lampGpio != null) {
            Log.i(TAG, "Closing LED GPIO pin");
            try {
                lampGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            } finally {
                lampGpio = null;
            }
        }

        if (buttonGpio != null) {
            Log.i(TAG, "Closing Button GPIO pin");
            try {
                buttonGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            } finally {
                buttonGpio = null;
            }
        }
    }

    @Override
    public void showLampState(Boolean lampState) {
        this.lampState = lampState;
        if (lampGpio != null) {
            try {
                lampGpio.setValue(lampState);
                Log.d(TAG, "State set to " + lampState);
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }

        if (switchCompat != null) {
            switchCompat.setChecked(lampState);
        }
    }

    @Override
    public void showErrorMessage(CharSequence errorMessage) {
        Log.e(TAG, "showErrorMessage: " + errorMessage);
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
