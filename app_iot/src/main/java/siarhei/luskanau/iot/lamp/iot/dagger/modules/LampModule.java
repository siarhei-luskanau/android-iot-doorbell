package siarhei.luskanau.iot.lamp.iot.dagger.modules;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.iot.doorbell.domain.ImageRepository;
import siarhei.luskanau.iot.doorbell.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.doorbell.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.doorbell.domain.executor.ThreadExecutor;
import siarhei.luskanau.iot.doorbell.domain.interactor.ListenLampStateUseCase;
import siarhei.luskanau.iot.doorbell.domain.interactor.SendImageUseCase;
import siarhei.luskanau.iot.doorbell.presenter.listen.ListenLampStatePresenter;
import siarhei.luskanau.iot.doorbell.presenter.send.SendImagePresenter;

@Module
public class LampModule {

    @Provides
    ListenLampStatePresenter provideListenLampStatePresenter(ImageRepository imageRepository,
                                                             ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             ErrorMessageFactory errorMessageFactory) {
        ListenLampStateUseCase listenLampStateUseCase = new ListenLampStateUseCase(imageRepository,
                threadExecutor, postExecutionThread);
        return new ListenLampStatePresenter(listenLampStateUseCase, errorMessageFactory);
    }

    @Provides
    SendImagePresenter provideSendLampStatePresenter(ImageRepository imageRepository,
                                                     ThreadExecutor threadExecutor,
                                                     PostExecutionThread postExecutionThread,
                                                     ErrorMessageFactory errorMessageFactory) {
        SendImageUseCase sendImageUseCase = new SendImageUseCase(imageRepository,
                threadExecutor, postExecutionThread);
        return new SendImagePresenter(sendImageUseCase, errorMessageFactory);
    }
}
