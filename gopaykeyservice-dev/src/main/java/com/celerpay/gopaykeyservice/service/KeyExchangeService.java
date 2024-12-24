/*
 * *
 *  * Created by Oladejo Mubarak
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/24/24, 9:54 AM
 *
 */

package com.celerpay.gopaykeyservice.service;


import com.celerpay.gopaykeyservice.dto.TerminalKeyManagement;

public interface KeyExchangeService {
    TerminalKeyManagement doKeyExchange(String terminalId);
}
