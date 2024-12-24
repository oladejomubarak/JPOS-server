/*
 * *
 *  * Created by Oladejo Mubarak
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 7/9/24, 7:44 AM
 *
 */

package com.celerpay.gopaycore.handler;


import com.celerpay.gopaycore.dto.Response;
import com.celerpay.gopaycore.providers.ProviderService;
import com.celerpay.gopaycore.validation.MessageValidator;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.CryptoException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static com.celerpay.gopaycore.utils.Constants.*;

@Service
@Slf4j
public class RequestHandler {


    @Autowired
    MessageValidator validator;


    @Autowired
    ApplicationContext context;

    @Autowired
    Environment env;

    @Autowired
    GenericPackager packager;

    public ISOMsg processRequest(ISOMsg posRequest) throws ISOException, IOException, NoSuchAlgorithmException, KeyManagementException, CryptoException {
        //todo add logic to select provider for transaction

        String provider = "NIBSS";
        String mti = posRequest.getMTI();


        switch(mti){
            case _0800:
                if (validator._0800Validator().apply(posRequest)) {
                    ProviderService networkProvider = (ProviderService) context.getBean(provider);

                    Response networkResponse = networkProvider.processPayment(posRequest, packager);
                    return networkResponse.getResponseIso();
                } else {
                    return mockChannelResponse(posRequest, "12");
                }

            case _0100:
                if (validator._0100Validator().apply(posRequest)) {
                    //todo save authorization request
                    ProviderService authProvider = (ProviderService) context.getBean(provider);

                    Response authResponse = authProvider.processPayment(posRequest, packager);

                    return authResponse.getResponseIso();
                } else {
                    return mockChannelResponse(posRequest, "12");
                }
            case _0200:
                if (validator._0200Validator().apply(posRequest)) {
                    //todo save transaction request


                    ProviderService transactionProvider = (ProviderService) context.getBean(provider);
                    Response transactionResponse = transactionProvider.processPayment(posRequest, packager);
                    return transactionResponse.getResponseIso();

                } else {
                    return mockChannelResponse(posRequest, "12");
                }
            case _0420:
            case _0421:
                if (validator._0420Validator().apply(posRequest)) {
                    log.info("Reversal request for terminal {}", posRequest.getString("41"));
                    //process reversal

                    ProviderService service = (ProviderService) context.getBean(provider);
                    Response response = service.processPayment(posRequest, packager);
                    return response.getResponseIso();
                } else {
                    return mockChannelResponse(posRequest, "12");
                }
        }
        return mockChannelResponse(posRequest, "12");
    }
    private static ISOMsg mockChannelResponse(ISOMsg posRequest, String responseCode) {
        try {
            ISOMsg tranResponse = (ISOMsg) posRequest.clone();
            tranResponse.set(39, responseCode);
            tranResponse.setResponseMTI();
            if (tranResponse.hasField(98)) {
                tranResponse.unset(44, 98, 113, 103, 127);
            }

            return tranResponse;
        } catch (ISOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
