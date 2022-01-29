package com.scheduler.ui;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 10/4/2021
 */
public interface IUIProcessorsFactory {

    IUiProcessor getProcessor(Map<String, Object> params);
}
