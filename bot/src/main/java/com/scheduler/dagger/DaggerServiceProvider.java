package com.scheduler.dagger;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.scheduler.dao.IContextDao;
import com.scheduler.dao.ISettingDao;
import com.scheduler.dao.IUserDao;
import com.scheduler.dao.IUserSessionDao;
import com.scheduler.localization.ILocalizer;
import com.scheduler.localization.Localizer;
import com.scheduler.model.EventType;
import com.scheduler.model.EventTypeKey;
import com.scheduler.processor.events.IEventProcessor;
import com.scheduler.processor.events.IEventProcessorFactory;
import com.scheduler.processor.events.impl.EventProcessorFactory;
import com.scheduler.processor.events.impl.LocalizationProcessor;
import com.scheduler.service.IAuthService;
import com.scheduler.service.IContextService;
import com.scheduler.service.IRoleFacade;
import com.scheduler.service.IS3Service;
import com.scheduler.service.ISecretService;
import com.scheduler.service.ISettingService;
import com.scheduler.service.ISqsService;
import com.scheduler.service.IUserService;
import com.scheduler.service.IUserSessionService;
import com.scheduler.service.impl.AuthService;
import com.scheduler.service.impl.ContextService;
import com.scheduler.service.impl.RoleFacade;
import com.scheduler.service.impl.S3Service;
import com.scheduler.service.impl.SecretService;
import com.scheduler.service.impl.SettingService;
import com.scheduler.service.impl.SqsService;
import com.scheduler.service.impl.UserService;
import com.scheduler.service.impl.UserSessionService;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

import javax.inject.Singleton;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
@Module(includes = {DaggerDaoProvider.class})
public class DaggerServiceProvider {

    @Provides
    @Singleton
    public ILocalizer localizer(IContextService contextService, ISqsService sqsService, ISettingService settingService) {
        return new Localizer(contextService, sqsService, settingService);
    }

    @Provides
    @Singleton
    public IContextService contextService(IContextDao contextDao) {
        return new ContextService(contextDao);
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
    public IUserSessionService userSessionService(IUserSessionDao sessionDao, ISettingService settingService,
                                                  IUserService userService) {
        return new UserSessionService(sessionDao, settingService, userService);
    }

    @Provides
    @Singleton
    public IUserService userService(IUserDao userDao) {
        return new UserService(userDao);
    }

    @Provides
    @Singleton
    public AmazonSQS amazonSQSClient() {
        AmazonSQS amazonSQS = AmazonSQSClientBuilder.defaultClient();
        return amazonSQS;
    }

    @Provides
    @Singleton
    public ISqsService sqsService(AmazonSQS sqs) {
        return new SqsService(sqs);
    }

    @Provides
    @Singleton
    @IntoMap
    @EventTypeKey(EventType.LOCALIZATION)
    public IEventProcessor localizationProcessor(IS3Service s3Service) {
        return new LocalizationProcessor(s3Service);
    }

    @Provides
    @Singleton
    public IEventProcessorFactory eventProcessorFactory(Map<EventType, IEventProcessor> processorMap) {
        return new EventProcessorFactory(processorMap);
    }
}
