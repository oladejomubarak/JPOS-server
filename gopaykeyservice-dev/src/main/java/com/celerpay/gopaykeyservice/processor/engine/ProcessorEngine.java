/*
 * *
 *  * Created by Mubarak Oladejo
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/24/24, 11:27 AM
 *
 */

package com.celerpay.gopaykeyservice.processor.engine;


import com.celerpay.gopaykeyservice.dto.TerminalKeyManagement;

public interface ProcessorEngine {
    TerminalKeyManagement keyManagement(String terminalId, String zmk);
}
