package com.scheduler.dagger;

import com.scheduler.application.TelegramBot;
import com.scheduler.localization.ILocalizer;
import com.scheduler.model.CommandKey;
import com.scheduler.model.CommandType;
import com.scheduler.processor.FileSender;
import com.scheduler.processor.IProcessor;
import com.scheduler.processor.IProcessorFactory;
import com.scheduler.processor.impl.ProcessorFactory;
import com.scheduler.processor.impl.StartProcessor;
import com.scheduler.processor.impl.registration.FirstNameProcessor;
import com.scheduler.processor.impl.registration.SetLanguageProcessor;
import com.scheduler.processor.impl.registration.StartRegistrationProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.service.IRoleFacade;
import com.scheduler.service.ISecretService;
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
                                   IRoleFacade roleFacade, ISecretService secretService, ILocalizer localizer) {
        return new TelegramBot(processorFactory, contextService, roleFacade, secretService, localizer);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.START)
    public IProcessor startProcessor(IContextService contextService) {
        return new StartProcessor(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.REGISTRATION_START)
    public IProcessor registrationProcessor(IContextService contextService) {
        return new StartRegistrationProcessor(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.REGISTER_FIRST_NAME)
    public IProcessor firstNameProcessor(IUserService userService) {
        return new FirstNameProcessor(userService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.SET_LANGUAGE)
    public IProcessor setLanguageProcessor(IContextService contextService) {
        return new SetLanguageProcessor(contextService);
    }

//    @Provides
//    @Singleton
//    @IntoMap
//    @CommandKey(CommandType.DASHBOARD_PROCESSOR)
//    public IProcessor dashboardProcessor(Map<String, IDashboardHelper> helpersMap,
//                                         IContextService contextService) {
//        return new DashboardProcessor(helpersMap, contextService);
//    }

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
