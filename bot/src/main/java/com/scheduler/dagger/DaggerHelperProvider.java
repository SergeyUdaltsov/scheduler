package com.scheduler.dagger;

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
import com.scheduler.helper.impl.balance.CardBalanceHelper;
import com.scheduler.helper.impl.balance.DebtorsBalanceHelper;
import com.scheduler.helper.impl.balance.MonthlyAnalyticHelper;
import com.scheduler.helper.impl.balance.PLayerBalanceHelper;
import com.scheduler.helper.impl.balance.PrePayedBalanceHelper;
import com.scheduler.helper.impl.bill.create.CreateBillHelper;
import com.scheduler.helper.impl.bill.create.SundayIceBillCreateHelper;
import com.scheduler.helper.impl.bill.create.SundayIceExecuteHelper;
import com.scheduler.helper.impl.bill.execute.BillEditExecuteCommonTypeHelper;
import com.scheduler.helper.impl.bill.execute.BillEditExecuteHelper;
import com.scheduler.helper.impl.bill.execute.BillRemoveExecuteHelper;
import com.scheduler.helper.impl.bill.execute.sunday.BillEditExecuteSundayIceTypeHelper;
import com.scheduler.helper.impl.bill.execute.sunday.BillEditSundayIceEditHelper;
import com.scheduler.helper.impl.bill.execute.sunday.BillEditSundayIceRemoveHelper;
import com.scheduler.helper.impl.bill.select.BillEditSelectBillHelper;
import com.scheduler.helper.impl.collect.BalanceCollectionActionHelper;
import com.scheduler.helper.impl.collect.BalanceTotalCollectActionHelper;
import com.scheduler.helper.impl.collect.CreateCollectActionHelper;
import com.scheduler.helper.impl.collect.CreateCollectPaymentHelper;
import com.scheduler.helper.impl.collect.PayCollectActionHelper;
import com.scheduler.helper.impl.collect.RemoveCollectActionHelper;
import com.scheduler.helper.impl.collect.RemoveCollectPaymentHelper;
import com.scheduler.helper.impl.dashboard.BalanceDashboardHelper;
import com.scheduler.helper.impl.dashboard.CollectDashboardHelper;
import com.scheduler.helper.impl.dashboard.PaymentBillDashboardHelper;
import com.scheduler.helper.impl.dashboard.PlayerDashboardHelper;
import com.scheduler.helper.impl.dashboard.TransferDashboardHelper;
import com.scheduler.helper.impl.payment.LastPaymentsHelper;
import com.scheduler.helper.impl.payment.PaymentBillActionEditHelper;
import com.scheduler.helper.impl.payment.PaymentBillActionExecuteHelper;
import com.scheduler.helper.impl.payment.PaymentsToFileHelper;
import com.scheduler.helper.impl.payment.edit.execute.PaymentEditExecuteHelper;
import com.scheduler.helper.impl.payment.edit.execute.PaymentRemoveExecuteHelper;
import com.scheduler.helper.impl.player.PlayerBalanceShowHelper;
import com.scheduler.helper.impl.player.PlayerCreateHelper;
import com.scheduler.helper.impl.player.PlayerEditHelper;
import com.scheduler.helper.impl.player.PlayerPaymentEditActionSelectHelper;
import com.scheduler.helper.impl.player.PlayerPaymentSelectHelper;
import com.scheduler.helper.impl.player.PlayerRemoveHelper;
import com.scheduler.helper.impl.player.PlayerSundayIceListHelper;
import com.scheduler.helper.impl.transfer.TransferCreateHelper;
import com.scheduler.helper.impl.transfer.TransferRemoveHelper;
import com.scheduler.model.CollectType;
import com.scheduler.model.CommandType;
import com.scheduler.processor.FileSender;
import com.scheduler.service.IBillService;
import com.scheduler.service.ICollectService;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPaymentService;
import com.scheduler.service.IPlayerService;
import com.scheduler.service.ISettingService;
import com.scheduler.service.ITransferService;
import com.scheduler.service.IUserService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/18/2021
 */
@Module(includes = {DaggerServiceProvider.class})
public class DaggerHelperProvider {

    @Provides
    @Singleton
    public Map<CollectType, IExecuteCollectPaymentHelper> collectPaymentHelperMap(ICollectService collectService, IUserService userService,
                                                                                  IPlayerService playerService, IContextService contextService) {
        CreateCollectPaymentHelper createHelper = new CreateCollectPaymentHelper(collectService, userService, contextService);
        RemoveCollectPaymentHelper removeHelper = new RemoveCollectPaymentHelper(contextService, collectService);
        return CollectionUtils.<CollectType, IExecuteCollectPaymentHelper>mapBuilder()
                .withPair(CollectType.BANK, createHelper)
                .withPair(CollectType.CASH, createHelper)
                .withPair(CollectType.COACH, createHelper)
                .withPair(CollectType.REMOVE, removeHelper)
                .build();
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Создать")
    public IPlayerHelper createHelper(IUserService userService) {
        return new PlayerEditHelper(userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Удалить")
    public IPlayerHelper removeHelper(IUserService userService) {
        return new PlayerEditHelper(userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Воскресный список")
    public IPlayerHelper sundayIceListHelper(IPaymentService paymentService) {
        return new PlayerSundayIceListHelper(paymentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Должники")
    public IBalanceHelper debtorsBalanceHelper(IPaymentService paymentService, IPlayerService playerService,
                                               IBillService billService, IUserService userService) {
        return new DebtorsBalanceHelper(paymentService, playerService, billService, userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Переплаты")
    public IBalanceHelper prePayedBalanceHelper(IPaymentService paymentService, IPlayerService playerService,
                                               IBillService billService, IUserService userService) {
        return new PrePayedBalanceHelper(paymentService, playerService, billService, userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Аналитика месяца")
    public IBalanceHelper monthlyAnalyticHelper(IPaymentService paymentService, IBillService billService,
                                                IUserService userService, IPlayerService playerService) {
        return new MonthlyAnalyticHelper(paymentService, billService, userService, playerService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("По игроку")
    public IBalanceHelper playerBalanceHelper(IContextService contextService, IUserService userService) {
        return new PLayerBalanceHelper(contextService, userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("На карте")
    public IBalanceHelper cardBalanceHelper(IPaymentService paymentService, ITransferService transferService) {
        return new CardBalanceHelper(paymentService, transferService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Платежи")
    public IDashboardHelper paymentDashboard() {
        return new PaymentBillDashboardHelper("Платежи", CommandType.PAYMENT_ACTION_SELECT,
                "платежем", MessageUtils.paymentsButtons());
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Сбор")
    public IDashboardHelper collectDashboard() {
        return new CollectDashboardHelper("Сбор", CommandType.COLLECT_ACTION_SELECT,
                "сбором", Arrays.asList("Создать", "Провести оплату", "Баланс",
                "Баланс сбора", "Закрыть"));
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Создать")
    public ICollectActionHelper createCollect(IContextService contextService) {
        return new CreateCollectActionHelper(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Провести оплату")
    public ICollectActionHelper payCollect(IUserService userService) {
        return new PayCollectActionHelper(userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Баланс сбора")
    public ICollectActionHelper collectBalance(IUserService userService) {
        return new BalanceCollectionActionHelper(userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Закрыть")
    public ICollectActionHelper removeCollect(IUserService userService) {
        return new RemoveCollectActionHelper(userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Баланс")
    public ICollectActionHelper balanceCollect(ICollectService collectService, IUserService userService) {
        return new BalanceTotalCollectActionHelper(collectService, userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Начисления")
    public IDashboardHelper billsDashboard() {
        return new PaymentBillDashboardHelper("Начисления", CommandType.PAYMENT_ACTION_SELECT,
                "начислением", MessageUtils.billsButtons());
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Баланс")
    public IDashboardHelper balanceDashboard() {
        return new BalanceDashboardHelper();
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Игроки")
    public IDashboardHelper playerDashboard(IContextService contextService) {
        return new PlayerDashboardHelper(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Переводы")
    public IDashboardHelper transferDashboard(IContextService contextService) {
        return new TransferDashboardHelper(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Создать")
    public ITransferHelper transferCreateHelper(IContextService contextService) {
        return new TransferCreateHelper();
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Удалить")
    public ITransferHelper transferRemoveHelper(ITransferService transferService) {
        return new TransferRemoveHelper(transferService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Создать")
    public IPlayerEditExecuteHelper playerCreateHelper() {
        return new PlayerCreateHelper();
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Удалить")
    public IPlayerEditExecuteHelper playerRemoveHelper(IContextService contextService, IPlayerService playerService) {
        return new PlayerRemoveHelper(playerService, contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Провести")
    public IPaymentActionHelper paymentExecuteAction(IContextService contextService, IUserService userService) {
        return new PaymentBillActionExecuteHelper(contextService, false, userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Редактировать")
    public IPaymentActionHelper paymentEditActionHelper(IContextService contextService,
                                                        IUserService userService) {
        return new PaymentBillActionEditHelper(contextService, userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Провести Льготный")
    public IPaymentActionHelper executeHiddenPaymentActionHelper(IContextService contextService,
                                                                 IUserService userService) {
        return new PaymentBillActionExecuteHelper(contextService, true, userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Показать Последние")
    public IPaymentActionHelper lastPaymentsActionHelper(IContextService contextService,
                                                         IPaymentService paymentService) {
        return new LastPaymentsHelper(paymentService, contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Файл с платежами")
    public IPaymentActionHelper lastPaymentsToFileActionHelper(IPaymentService paymentService, FileSender fileSender,
                                                               ITransferService transferService) {
        return new PaymentsToFileHelper(paymentService, fileSender, transferService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("paymentExecution")
    public IPlayerActionHelper playerPaymentExecuteHelper(IContextService contextService, IPlayerService playerService) {
        return new PlayerPaymentSelectHelper(contextService, playerService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("playerBalance")
    public IPlayerActionHelper playerBalanceExecuteHelper(IContextService contextService, IPaymentService paymentService,
                                                          IBillService billService, IPlayerService playerService) {
        return new PlayerBalanceShowHelper(contextService, paymentService, billService, playerService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("billEdit")
    public IPlayerActionHelper sundayIceEditExecuteHelper(IContextService contextService, IPlayerService playerService) {
        return new PlayerPaymentEditActionSelectHelper(contextService, playerService, CommandType.BILL_SUNDAY_ICE_SELECT);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("paymentEdit")
    public IPlayerActionHelper playerPaymentEditHelper(IContextService contextService, IPlayerService playerService) {
        return new PlayerPaymentEditActionSelectHelper(contextService, playerService, CommandType.PAYMENT_EDIT_SELECT);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("edit")
    public IBillEditExecuteHelper billEditExecuteHelper(IContextService contextService, IBillService billService) {
        return new BillEditExecuteHelper(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("remove")
    public IBillEditExecuteHelper billRemoveExecuteHelper(IContextService contextService, IBillService billService) {
        return new BillRemoveExecuteHelper(contextService, billService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("billExecution")
    public IPlayerActionHelper billExecutionHelper(IContextService contextService, IPaymentService paymentService,
                                                   IPlayerService playerService, ISettingService settingService) {
        return new SundayIceBillCreateHelper(contextService, paymentService, playerService, settingService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Удалить")
    public IPaymentEditHelper paymentRemoveHelper(IContextService contextService, IPaymentService paymentService) {
        return new PaymentRemoveExecuteHelper(contextService, paymentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Изменить")
    public IPaymentEditHelper paymentEditHelper(IContextService contextService) {
        return new PaymentEditExecuteHelper(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Тренерские")
    public IBillExecuteHelper coachBillExecuteHelper(IContextService contextService, IBillService billService,
                                                     ISettingService settingService) {
        return new CreateBillHelper(contextService, billService, settingService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Изменить")
    public IBillEditSelectHelper coachBillEditHelper(IContextService contextService, IBillService billService) {
        return new BillEditSelectBillHelper(contextService, billService, "edit");
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Удалить")
    public IBillEditSelectHelper monthlyIceBillEditHelper(IContextService contextService, IBillService billService) {
        return new BillEditSelectBillHelper(contextService, billService, "remove");
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Изменить")
    public IBillSundayIceEditHelper sundayIceEditHelper(IContextService contextService, IBillService billService) {
        return new BillEditSundayIceEditHelper(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Удалить")
    public IBillSundayIceEditHelper sundayIceRemoveHelper(IContextService contextService, IPaymentService paymentService) {
        return new BillEditSundayIceRemoveHelper(contextService, paymentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Лед")
    public IBillExecuteHelper monthlyIceBillExecuteHelper(IContextService contextService, IBillService billService,
                                                          ISettingService settingService) {
        return new CreateBillHelper(contextService, billService, settingService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Воскресный лед")
    public IBillExecuteHelper sundayIceBillExecuteHelper(IContextService contextService, IUserService userService) {
        return new SundayIceExecuteHelper(contextService, userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Тренерские")
    public IBillEditExecuteBillTypeHelper coachBillExecuteBillTypeHelper(IContextService contextService) {
        return new BillEditExecuteCommonTypeHelper(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Лед")
    public IBillEditExecuteBillTypeHelper iceBillExecuteBillTypeHelper(IContextService contextService) {
        return new BillEditExecuteCommonTypeHelper(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey("Воскресный лед")
    public IBillEditExecuteBillTypeHelper sundayIceBillExecuteBillTypeHelper(IContextService contextService, IUserService userService) {
        return new BillEditExecuteSundayIceTypeHelper(contextService, userService);
    }
}
