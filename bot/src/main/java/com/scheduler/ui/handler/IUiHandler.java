package com.scheduler.ui.handler;

import com.scheduler.ui.request.UiRequest;

/**
 * @author Serhii_Udaltsov on 10/23/2021
 */
public interface IUiHandler<REQUEST extends UiRequest> {

    Object handleRequest(REQUEST request);

    String getSupportedHandlerType();

    Class<REQUEST> getHandlerClass();
}
