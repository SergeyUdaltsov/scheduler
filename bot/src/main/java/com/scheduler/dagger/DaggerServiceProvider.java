package com.scheduler.dagger;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.scheduler.dao.IBillDao;
import com.scheduler.dao.ICollectPaymentDao;
import com.scheduler.dao.IContextDao;
import com.scheduler.dao.IPaymentDao;
import com.scheduler.dao.IPlayerDao;
import com.scheduler.dao.ISettingDao;
import com.scheduler.dao.ITransferDao;
import com.scheduler.dao.IUserDao;
import com.scheduler.dao.IUserSessionDao;
import com.scheduler.service.IAuthService;
import com.scheduler.service.IBillService;
import com.scheduler.service.ICollectService;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPaymentService;
import com.scheduler.service.IPlayerService;
import com.scheduler.service.IRoleFacade;
import com.scheduler.service.IS3Service;
import com.scheduler.service.ISecretService;
import com.scheduler.service.ISettingService;
import com.scheduler.service.ITransferService;
import com.scheduler.service.IUserService;
import com.scheduler.service.IUserSessionService;
import com.scheduler.service.impl.AuthService;
import com.scheduler.service.impl.BillService;
import com.scheduler.service.impl.CollectService;
import com.scheduler.service.impl.ContextService;
import com.scheduler.service.impl.PaymentService;
import com.scheduler.service.impl.PlayerService;
import com.scheduler.service.impl.RoleFacade;
import com.scheduler.service.impl.S3Service;
import com.scheduler.service.impl.SecretService;
import com.scheduler.service.impl.SettingService;
import com.scheduler.service.impl.TransferService;
import com.scheduler.service.impl.UserService;
import com.scheduler.service.impl.UserSessionService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
@Module(includes = {DaggerDaoProvider.class})
public class DaggerServiceProvider {

    @Provides
    @Singleton
    public IPlayerService playerService(IPlayerDao playerDao) {
        return new PlayerService(playerDao);
    }

    @Provides
    @Singleton
    public IContextService contextService(IContextDao contextDao) {
        return new ContextService(contextDao);
    }

    @Provides
    @Singleton
    public IPaymentService paymentService(IPaymentDao paymentDao) {
        return new PaymentService(paymentDao);
    }

    @Provides
    @Singleton
    public IBillService billService(IBillDao billDao) {
        return new BillService(billDao);
    }

    @Provides
    @Singleton
    public ITransferService transferService(ITransferDao transferDao, IContextService contextService) {
        return new TransferService(transferDao, contextService);
    }

    @Provides
    @Singleton
    public ISettingService settingService(ISettingDao settingDao) {
        return new SettingService(settingDao);
    }

    @Provides
    @Singleton
    public AmazonS3 s3Client() {
        return AmazonS3Client.builder().withRegion("eu-central-1").build();
    }

    @Provides
    @Singleton
    public IS3Service s3Service(AmazonS3 client) {
        return new S3Service(client);
    }

    @Provides
    @Singleton
    public IUserService userService(IUserDao userDao) {
        return new UserService(userDao);
    }

    @Provides
    @Singleton
    public IUserSessionService sessionService(IUserSessionDao sessionDao, ISettingService settingService,
                                              IUserService userService) {
        return new UserSessionService(sessionDao, settingService, userService);
    }

    @Provides
    @Singleton
    public IRoleFacade roleFacade(IUserSessionService sessionService) {
        return new RoleFacade(sessionService);
    }

    @Provides
    @Singleton
    public ISecretService secretService() {
        AWSSimpleSystemsManagement management = AWSSimpleSystemsManagementClientBuilder.defaultClient();
        return new SecretService(management);
    }

    @Provides
    @Singleton
    public IAuthService authService(ISecretService secretService, ISettingService settingService) {
        return new AuthService(secretService, settingService);
    }

    @Provides
    @Singleton
    public ICollectService collectService(ICollectPaymentDao collectPaymentDao) {
        return new CollectService(collectPaymentDao);
    }
}
