package com.scheduler.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Serhii_Udaltsov on 10/23/2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthlyStatisticRequest extends UiRequest {

    private int month;

    public MonthlyStatisticRequest() {
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
