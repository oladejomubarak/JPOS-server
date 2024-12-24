/*
 * *
 *  * Created by Oladejo Mubarak
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 7/9/24, 7:27 AM
 *
 */

package com.celerpay.gopaycore.providers.nibss;


import com.celerpay.gopaycore.dto.Response;
import com.celerpay.gopaycore.providers.ProviderService;
import com.celerpay.gopaycore.socketMgr.SocketManager;
import com.celerpay.gopaycore.utils.PinTranslator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.CryptoException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service("NIBSS")
public class NibssProvider implements ProviderService {

    private final Environment env;

    private final PinTranslator pinTranslator;

    @Override
    public Response processPayment(ISOMsg posRequest, GenericPackager packager) throws IOException, NoSuchAlgorithmException, KeyManagementException, ISOException, CryptoException {
        SocketManager socketManager= getSocketManger();
        if(Objects.isNull(socketManager)){
            log.error("SocketManager is null");
            throw new IOException("SocketManager is null");
        }

        posRequest.setPackager(packager);

        return socketManager.sendAndReceive(pinTranslator.translateNibssPin(posRequest),posRequest.pack(),packager);
    }


    private SocketManager getSocketManger() throws IOException {
        String ip = env.getProperty("nibss.server.ip");
        int port = Integer.parseInt(Objects.requireNonNull(env.getProperty("nibss.server.port")));
        return new SocketManager(ip, port);
    }
}
