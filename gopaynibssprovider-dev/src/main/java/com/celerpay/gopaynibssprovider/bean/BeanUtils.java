/*
 * *
 *  * Created by Oladejo Mubarak
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 1/6/24, 8:30 AM
 *
 */

package com.celerpay.gopaynibssprovider.bean;

import org.jpos.iso.ISOException;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanUtils {

    @Bean
    public GenericPackager getPackager() throws ISOException {
        return new GenericPackager("nibss.xml");

    }
}
