//package com.scheduler.lambda;
//
//import com.scheduler.dagger.DaggerLambdaComponent;
//import com.scheduler.ui.handler.IUiHandler;
//import com.scheduler.ui.request.UiRequest;
//import com.scheduler.utils.JsonUtils;
//
//import javax.inject.Inject;
//import javax.ws.rs.BadRequestException;
//import java.util.Map;
//import java.util.Set;
//
///**
// * @author Serhii_Udaltsov on 10/4/2021
// */
//public class UiLambda extends AbstractLambda {
//
//    private Set<IUiHandler> handlers;
//
//    public UiLambda() {
//        DaggerLambdaComponent.create().inject(this);
//    }
//
//    @Override
//    protected Object handle(Map<String, Object> body) {
//        String handlerType = (String) body.get("handlerType");
//        IUiHandler handler = handlers.stream()
//                .filter(h -> h.getSupportedHandlerType().equalsIgnoreCase(handlerType))
//                .findFirst()
//                .orElse(null);
//        if (handler == null) {
//            throw new BadRequestException("Handler not found for type: " + handlerType);
//        }
//        System.out.println("Handler type -------------- " + handlerType);
//        UiRequest request = (UiRequest) JsonUtils.parseMap(body, handler.getHandlerClass());
//        System.out.println("REQUEST --------- " + JsonUtils.convertObjectToString(request));
//        return handler.handleRequest(request);
//    }
//
//    @Inject
//    public void setHandlers(Set<IUiHandler> handlers) {
//        this.handlers = handlers;
//    }
//}
