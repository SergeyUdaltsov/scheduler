package com.scheduler.dagger;

import com.scheduler.application.TelegramBot;
import com.scheduler.helper.IBalanceHelper;
import com.scheduler.helper.IBillEditExecuteBillTypeHelper;
import com.scheduler.helper.IBillEditExecuteHelper;
import com.scheduler.helper.IBillEditSelectHelper;
import com.scheduler.helper.IBillExecuteHelper;
import com.scheduler.helper.IBillSundayIceEditHelper;
import com.scheduler.helper.ICollectActionHelper;
import com.scheduler.helper.IDashboardHelper;
import com.scheduler.helper.IExecuteCollectPaymentHelper;
import com.scheduler.helper.IPaymentActionHelper;
import com.scheduler.helper.IPaymentEditHelper;
import com.scheduler.helper.IPlayerActionHelper;
import com.scheduler.helper.IPlayerEditExecuteHelper;
import com.scheduler.helper.IPlayerHelper;
import com.scheduler.helper.ITransferHelper;
import com.scheduler.model.CollectType;
import com.scheduler.model.CommandKey;
import com.scheduler.model.CommandType;
import com.scheduler.processor.FileSender;
import com.scheduler.processor.IProcessor;
import com.scheduler.processor.IProcessorFactory;
import com.scheduler.processor.impl.DashboardProcessor;
import com.scheduler.processor.impl.ProcessorFactory;
import com.scheduler.processor.impl.StartProcessor;
import com.scheduler.processor.impl.balance.BalanceExecuteProcessor;
import com.scheduler.processor.impl.balance.BalanceSendProcessor;
import com.scheduler.processor.impl.balance.TransferExecuteProcessor;
import com.scheduler.processor.impl.bill.BillEditExecuteProcessor;
import com.scheduler.processor.impl.bill.BillEditProcessor;
import com.scheduler.processor.impl.bill.BillEditSaveProcessor;
import com.scheduler.processor.impl.bill.BillEditTypeSelectProcessor;
import com.scheduler.processor.impl.bill.BillExecuteProcessor;
import com.scheduler.processor.impl.bill.BillSundayIceExecuteProcessor;
import com.scheduler.processor.impl.bill.BillSundayIceSelectProcessor;
import com.scheduler.processor.impl.collect.CollectActionSelectProcessor;
import com.scheduler.processor.impl.collect.CollectBalanceProcessor;
import com.scheduler.processor.impl.collect.CollectCreateProcessor;
import com.scheduler.processor.impl.collect.CollectNameCreateProcessor;
import com.scheduler.processor.impl.collect.CollectPayCreateProcessor;
import com.scheduler.processor.impl.collect.CollectPayPlayerSelectProcessor;
import com.scheduler.processor.impl.collect.CollectPayTypeSelectProcessor;
import com.scheduler.processor.impl.collect.CollectPayYearSelectProcessor;
import com.scheduler.processor.impl.collect.CollectRemoveProcessor;
import com.scheduler.processor.impl.payment.PaymentActionSelectProcessor;
import com.scheduler.processor.impl.payment.PaymentCreateProcessor;
import com.scheduler.processor.impl.payment.PaymentEditExecuteProcessor;
import com.scheduler.processor.impl.payment.PaymentEditSaveProcessor;
import com.scheduler.processor.impl.payment.PaymentEditSelectProcessor;
import com.scheduler.processor.impl.payment.PaymentsDescribeProcessor;
import com.scheduler.processor.impl.player.PlayerActionExecuteProcessor;
import com.scheduler.processor.impl.player.PlayerEditExecuteProcessor;
import com.scheduler.processor.impl.player.PlayerProcessor;
import com.scheduler.processor.impl.player.PlayerRemoveProcessor;
import com.scheduler.processor.impl.player.PlayerSaveProcessor;
import com.scheduler.processor.impl.player.PlayersSelectProcessor;
import com.scheduler.processor.impl.transfer.TransferBuildProcessor;
import com.scheduler.processor.impl.transfer.TransferCreateProcessor;
import com.scheduler.processor.impl.transfer.TransferRemoveProcessor;
import com.scheduler.processor.impl.transfer.TransferSelectProcessor;
import com.scheduler.service.IBillService;
import com.scheduler.service.ICollectService;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPaymentService;
import com.scheduler.service.IPlayerService;
import com.scheduler.service.IRoleFacade;
import com.scheduler.service.ISecretService;
import com.scheduler.service.ISettingService;
import com.scheduler.service.ITransferService;
import com.scheduler.service.IUserService;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

import javax.inject.Singleton;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
@Module(includes = {
        DaggerDaoProvider.class,
        DaggerHelperProvider.class,
        DaggerServiceProvider.class})
public class DaggerBotProvider {

    @Provides
    @Singleton
    public TelegramBot telegramBot(IProcessorFactory processorFactory, IContextService contextService,
                                   IRoleFacade roleFacade, IUserService userService,
                                   ISecretService secretService) {
        return new TelegramBot(processorFactory, contextService, roleFacade, userService, secretService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.COLLECT_BALANCE_PROCESSOR)
    public IProcessor collectBalanceProcessor(ICollectService collectService) {
        return new CollectBalanceProcessor(collectService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.START)
    public IProcessor startProcessor(IContextService contextService, IRoleFacade roleFacade) {
        return new StartProcessor(contextService, roleFacade);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DASHBOARD_PROCESSOR)
    public IProcessor dashboardProcessor(Map<String, IDashboardHelper> helpersMap,
                                         IContextService contextService) {
        return new DashboardProcessor(helpersMap, contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PAYMENT_ACTION_SELECT)
    public IProcessor paymentTypeProcessor(Map<String, IPaymentActionHelper> helpersMap,
                                           IContextService contextService) {
        return new PaymentActionSelectProcessor(helpersMap, contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.COLLECT_ACTION_SELECT)
    public IProcessor collectActionProcessor(IContextService contextService, Map<String, ICollectActionHelper> helpersMap) {
        return new CollectActionSelectProcessor(contextService, helpersMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.COLLECT_REMOVE_PROCESSOR)
    public IProcessor collectRemoveProcessor(ICollectService collectService, IUserService userService) {
        return new CollectRemoveProcessor(collectService, userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.COLLECT_CREATE_PROCESSOR)
    public IProcessor collectCreateProcessor(IContextService contextService, IPlayerService playerService,
                                             IUserService userService, ICollectService collectService) {
        return new CollectCreateProcessor(contextService, playerService, userService, collectService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.COLLECT_NAME_CREATE_SELECT)
    public IProcessor collectNameProcessor(IContextService contextService) {
        return new CollectNameCreateProcessor(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.COLLECT_PAY_YEAR_SELECT)
    public IProcessor collectPayYearProcessor(IUserService userService, IContextService contextService) {
        return new CollectPayYearSelectProcessor(userService, contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.COLLECT_PAY_PLAYER_SELECT)
    public IProcessor collectPayPlayerProcessor(IPlayerService playerService, IContextService contextService,
                                                ICollectService collectService) {
        return new CollectPayPlayerSelectProcessor(playerService, contextService, collectService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.COLLECT_PAY_CREATE_PROCESSOR)
    public IProcessor collectPayCreateProcessor(IContextService contextService, ICollectService collectService,
                                                IUserService userService, IPlayerService playerService,
                                                Map<CollectType, IExecuteCollectPaymentHelper> helpersMap) {
        return new CollectPayCreateProcessor(contextService, collectService, userService, playerService, helpersMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.COLLECT_TYPE_SELECT)
    public IProcessor collectPayTypeProcessor(IContextService contextService) {
        return new CollectPayTypeSelectProcessor(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PLAYER_SELECT)
    public IProcessor playerSelectProcessor(IPlayerService playerService,
                                            IContextService contextService) {
        return new PlayersSelectProcessor(playerService, contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PLAYER_EDIT_EXECUTE)
    public IProcessor playerEditExecuteProcessor(Map<String, IPlayerEditExecuteHelper> helperMap,
                                          IContextService contextService) {
        return new PlayerEditExecuteProcessor(helperMap, contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PLAYER_SAVE)
    public IProcessor playerSaveProcessor(IPlayerService playerService,
                                          IContextService contextService) {
        return new PlayerSaveProcessor(contextService, playerService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PLAYER_REMOVE)
    public IProcessor playerRemoveProcessor(IPlayerService playerService,
                                          IContextService contextService) {
        return new PlayerRemoveProcessor(contextService, playerService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PAYMENT_CREATE)
    public IProcessor paymentCreateProcessor(IContextService contextService,
                                             IPaymentService paymentService,
                                             ISettingService settingService) {
        return new PaymentCreateProcessor(contextService, paymentService, settingService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PAYMENT_EDIT_SELECT)
    public IProcessor paymentEditProcessor(IContextService contextService,
                                           IPaymentService paymentService) {
        return new PaymentEditSelectProcessor(contextService, paymentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PAYMENT_EDIT_EXECUTE)
    public IProcessor paymentEditExecuteProcessor(IContextService contextService,
                                                  Map<String, IPaymentEditHelper> helpersMap) {
        return new PaymentEditExecuteProcessor(contextService, helpersMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PAYMENT_EDIT_EXECUTE_SAVE)
    public IProcessor paymentEditExecuteSaveProcessor(IContextService contextService,
                                                      IPaymentService paymentService) {
        return new PaymentEditSaveProcessor(contextService, paymentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PAYMENT_DESCRIBE)
    public IProcessor paymentDescribeProcessor(IContextService contextService,
                                                      IPaymentService paymentService) {
        return new PaymentsDescribeProcessor(contextService, paymentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PLAYER_ACTION_EXECUTE)
    public IProcessor playerActionExecuteProcessor(IContextService contextService,
                                                   Map<String, IPlayerActionHelper> helpersMap) {
        return new PlayerActionExecuteProcessor(contextService, helpersMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.BILL_EXECUTE)
    public IProcessor billExecuteProcessor(IContextService contextService,
                                           Map<String, IBillExecuteHelper> helperMap) {
        return new BillExecuteProcessor(contextService, helperMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.BILL_EDIT_TYPE_SELECT)
    public IProcessor billEditTypeSelectProcessor(IContextService contextService,
                                                  Map<String, IBillEditExecuteBillTypeHelper> helperMap) {
        return new BillEditTypeSelectProcessor(contextService, helperMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.BILL_EDIT)
    public IProcessor billEditProcessor(IContextService contextService,
                                        Map<String, IBillEditSelectHelper> helperMap) {
        return new BillEditProcessor(contextService, helperMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.BILL_EDIT_EXECUTE_SAVE)
    public IProcessor billEditSaveProcessor(IContextService contextService, IBillService billService) {
        return new BillEditSaveProcessor(contextService, billService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.BILL_EDIT_EXECUTE)
    public IProcessor billEditExecuteProcessor(IContextService contextService,
                                               Map<String, IBillEditExecuteHelper> helperMap) {
        return new BillEditExecuteProcessor(contextService, helperMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.BILL_SUNDAY_ICE_SELECT)
    public IProcessor billSundayIceEditSelectProcessor(IContextService contextService,
                                                        IPaymentService paymentService) {
        return new BillSundayIceSelectProcessor(contextService, paymentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.BILL_SUNDAY_ICE_EXECUTE)
    public IProcessor billSundayIceEditExecuteProcessor(IContextService contextService,
                                                        Map<String, IBillSundayIceEditHelper> helpersMap) {
        return new BillSundayIceExecuteProcessor(contextService, helpersMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.BALANCE_EXECUTE)
    public IProcessor balanceExecuteProcessor(IContextService contextService, Map<String, IBalanceHelper> helpersMap) {
        return new BalanceExecuteProcessor(contextService, helpersMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.BALANCE_SEND)
    public IProcessor balanceSendProcessor(IContextService contextService, Map<String, IBalanceHelper> helpersMap,
                                           FileSender fileSender) {
        return new BalanceSendProcessor(fileSender);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.TRANSFER_EXECUTE)
    public IProcessor transferExecuteProcessor(IContextService contextService, ITransferService transferService) {
        return new TransferExecuteProcessor(contextService, transferService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.TRANSFER_SELECT)
    public IProcessor transferSelectProcessor(Map<String, ITransferHelper> helperMap) {
        return new TransferSelectProcessor(helperMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.TRANSFER_REMOVE)
    public IProcessor transferRemoveProcessor(ITransferService transferService, IContextService contextService) {
        return new TransferRemoveProcessor(transferService, contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.TRANSFER_CREATE)
    public IProcessor transferCreateProcessor(ITransferService transferService, IContextService contextService) {
        return new TransferCreateProcessor(transferService, contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.TRANSFER_BUILD)
    public IProcessor transferBuildProcessor(IContextService contextService) {
        return new TransferBuildProcessor(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PLAYER_PROCESSOR)
    public IProcessor playerProcessor(IContextService contextService, Map<String, IPlayerHelper> helpers) {
        return new PlayerProcessor(contextService, helpers);
    }

    @Provides
    @Singleton
    public FileSender messageExecutor(ISecretService secretService) {
        return new FileSender(secretService);
    }

    @Provides
    @Singleton
    public IProcessorFactory factory(Map<CommandType, IProcessor> processors,
                                     IContextService contextService) {
        return new ProcessorFactory(processors, contextService);
    }

}
