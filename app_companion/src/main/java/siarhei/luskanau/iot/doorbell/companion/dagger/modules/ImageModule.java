package siarhei.luskanau.iot.doorbell.companion.dagger.modules;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.iot.doorbell.interactor.SendImageUseCase;
import siarhei.luskanau.iot.doorbell.presenter.send.SendImagePresenter;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

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
