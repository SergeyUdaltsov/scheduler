package com.scheduler.ui;

/**
 * @author Serhii_Udaltsov on 10/4/2021
 */
public interface IUiProcessor<T> {

    T executeRequest();

    String getSupportedAction();
}
