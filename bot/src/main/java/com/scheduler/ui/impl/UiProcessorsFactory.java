package com.scheduler.ui.impl;

import com.scheduler.ui.IUIProcessorsFactory;
import com.scheduler.ui.IUiProcessor;
import com.scheduler.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 10/4/2021
 */
public class UiProcessorsFactory implements IUIProcessorsFactory {

    private static final String PROCESSOR_KEY = "processor";

    private Map<String, IUiProcessor> processors;

    public UiProcessorsFactory(List<IUiProcessor> processors) {
        this.processors = processors.stream()
                .collect(Collectors.toMap(IUiProcessor::getSupportedAction, Function.identity()));
    }

    @Override
    public IUiProcessor getProcessor(Map<String, Object> params) {
        Object key = params.get(PROCESSOR_KEY);
        if (key == null || StringUtils.isBlank((String) key)) {
            throw new IllegalArgumentException("Processor key was not found");
        }
        IUiProcessor processor = processors.get(key);
        if (processor == null) {
            throw new IllegalArgumentException(String.format("Processor not found for key %s", key));
        }
        return processor;
    }
}
