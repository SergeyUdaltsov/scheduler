package com.scheduler.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Serhii_Udaltsov on 10/4/2021
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BotResponse {
    private String status;
    private int statusCode;
    private Object payload;
}
