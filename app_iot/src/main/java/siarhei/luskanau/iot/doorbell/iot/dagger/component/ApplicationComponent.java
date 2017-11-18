package siarhei.luskanau.iot.doorbell.iot.dagger.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.interactor.ISchedulerSet;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.iot.AppApplication;
import siarhei.luskanau.iot.doorbell.iot.dagger.modules.ApplicationModule;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(AppApplication appApplication);

    Application application();

    DeviceInfo deviceInfo();

    ISchedulerSet schedulerSet();

    TakePictureRepository takePictureRepository();

    ImageRepository lampRepository();

    ErrorMessageFactory errorMessageFactory();

    /*
     * SubComponents
     */

    //LampComponent subLampComponent();

}