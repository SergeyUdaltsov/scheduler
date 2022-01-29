package com.scheduler.ui.impl;

import com.scheduler.ui.IUiProcessor;

import java.util.Collections;
import java.util.List;

/**
 * @author Serhii_Udaltsov on 10/4/2021
 */
public class DashboardUiProcessor implements IUiProcessor<List<Object>> {

    @Override
    public List<Object> executeRequest() {
        return Collections.emptyList();
    }

    @Override
    public String getSupportedAction() {
        return "dashboard";
    }
}
