/*
 * *
 *  * Created by Kolawole Omirin
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/24/24, 10:00 AM
 *
 */

package com.celerpay.gopaykeyservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TerminalKeyManagement {
    private String terminalID;
    private String masterKey;
    private String sessionKey;
    private String pinKey;
    private String parameterDownloaded;
    private String lastExchangeDateTime;
    private String isSuccessful;
}
