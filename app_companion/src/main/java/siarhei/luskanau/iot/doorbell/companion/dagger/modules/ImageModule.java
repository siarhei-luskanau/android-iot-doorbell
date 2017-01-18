package siarhei.luskanau.iot.doorbell.companion.dagger.modules;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.iot.doorbell.domain.ImageRepository;
import siarhei.luskanau.iot.doorbell.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.doorbell.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.doorbell.domain.executor.ThreadExecutor;
import siarhei.luskanau.iot.doorbell.domain.interactor.SendImageUseCase;
import siarhei.luskanau.iot.doorbell.presenter.send.SendImagePresenter;

@Module
public class ImageModule {

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
