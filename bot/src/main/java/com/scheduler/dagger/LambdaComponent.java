package com.scheduler.dagger;

import com.scheduler.lambda.AuthLambda;
import com.scheduler.lambda.BotLambda;
import com.scheduler.lambda.EventProcessorLambda;
import dagger.Component;

import javax.inject.Singleton;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
@Component(modules = {
        DaggerBotProvider.class,
        DaggerServiceProvider.class,
        DaggerUiHandlerProvider.class
})
@Singleton
public interface LambdaComponent {

    void inject(BotLambda lambda);

    void inject(AuthLambda lambda);

    void inject(EventProcessorLambda lambda);

//    void inject(UiLambda lambda);
}
