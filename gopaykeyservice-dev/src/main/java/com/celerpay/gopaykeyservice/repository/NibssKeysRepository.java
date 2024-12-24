/*
 * *
 *  * Created by Oladejo Mubarak
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 7/9/24, 10:00 AM
 *
 */

package com.celerpay.gopaykeyservice.repository;

import com.celerpay.gopaykeyservice.model.NibssKeys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NibssKeysRepository extends JpaRepository<NibssKeys, Long> {
    NibssKeys findFirstByTerminalID(String terminalID);
}
