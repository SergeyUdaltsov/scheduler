package com.scheduler.dagger;

import com.scheduler.service.IBillService;
import com.scheduler.service.IPaymentService;
import com.scheduler.service.IPlayerService;
import com.scheduler.ui.handler.IUiHandler;
import com.scheduler.ui.handler.impl.GetMonthStatisticHandler;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

import javax.inject.Singleton;

/**
 * @author Serhii_Udaltsov on 10/23/2021
 */
@Module(includes = {DaggerServiceProvider.class})
public class DaggerUiHandlerProvider {

    @Provides
    @Singleton
    @IntoSet
    public IUiHandler monthlyStatisticHandler(IPaymentService paymentService, IBillService billService,
                                              IPlayerService playerService) {
        return new GetMonthStatisticHandler(paymentService, billService, playerService);
    }
}
