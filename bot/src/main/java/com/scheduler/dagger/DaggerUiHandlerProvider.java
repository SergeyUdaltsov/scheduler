package com.scheduler.dagger;

import dagger.Module;

/**
 * @author Serhii_Udaltsov on 10/23/2021
 */
@Module(includes = {DaggerServiceProvider.class})
public class DaggerUiHandlerProvider {

//    @Provides
//    @Singleton
//    @IntoSet
//    public IUiHandler monthlyStatisticHandler(IPaymentService paymentService, IBillService billService,
//                                              IPlayerService playerService) {
//        return new GetMonthStatisticHandler(paymentService, billService, playerService);
//    }
}
