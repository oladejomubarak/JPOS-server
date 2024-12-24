/*
 * *
 *  * Created by Oladejo Mubarak
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 7/9/24, 7:44 AM
 *
 */

package com.celerpay.gopaynibssprovider.handler;


import com.celerpay.gopaynibssprovider.model.NibssKeys;
import com.celerpay.gopaynibssprovider.repository.NibssKeysRepository;
import com.celerpay.gopaynibssprovider.socketMgr.SocketManager;
import com.celerpay.gopaynibssprovider.utils.PinTranslator;
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
import java.util.Objects;


@Service
@Slf4j
public class RequestHandler {



    @Autowired
    ApplicationContext context;

    @Autowired
    Environment env;

    @Autowired
    GenericPackager packager;
    @Autowired
    private NibssKeysRepository nibssKeysRepository;
    @Autowired
    PinTranslator pinTranslator;

    public ISOMsg processRequest(ISOMsg coreRequest) throws IOException, NoSuchAlgorithmException, KeyManagementException, ISOException, CryptoException {
        //get nibss keys for terminal
        NibssKeys nibssKeys = nibssKeysRepository.findFirstByTerminalID(coreRequest.getString(41));
        if (Objects.isNull(nibssKeys)) {
            log.info("No Terminal keys found for TID: {}", coreRequest.getString(41));
            return mockChannelResponse(coreRequest, "93");
        }
        int port = Integer.parseInt(Objects.requireNonNull(env.getProperty("nibss.port")));
        String ip = env.getProperty("nibss.ip");


        SocketManager channelSocketRequestManager = new SocketManager(ip, port);
        coreRequest.setPackager(packager);

        return channelSocketRequestManager.sendAndReceive(pinTranslator.translateNibssPin(coreRequest, nibssKeys),coreRequest.pack(), packager);
    }

    private static ISOMsg mockChannelResponse(ISOMsg coreRequest, String responseCode) {
        try {
            ISOMsg tranResponse = (ISOMsg) coreRequest.clone();
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
