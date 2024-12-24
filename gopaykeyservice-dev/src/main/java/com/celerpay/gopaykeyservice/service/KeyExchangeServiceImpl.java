/*
 * *
 *  * Created by Oladejo Mubarak
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/24/24, 9:54 AM
 *
 */

package com.celerpay.gopaykeyservice.service;

import com.celerpay.gopaykeyservice.dto.TerminalKeyManagement;
import com.celerpay.gopaykeyservice.model.NibssKeys;
import com.celerpay.gopaykeyservice.processor.engine.ProcessorEngine;
import com.celerpay.gopaykeyservice.repository.NibssKeysRepository;
import com.celerpay.gopaykeyservice.util.KeyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeyExchangeServiceImpl implements KeyExchangeService{
    private final Environment env;
    private final ModelMapper modelMapper;
    private final NibssKeysRepository nibssKeysRepository;
    private final ProcessorEngine processorEngine;
    private TerminalKeyManagement terminalKeyManagement;
    @Override
    public TerminalKeyManagement doKeyExchange(String terminalId) {

      //  TerminalKeyManagement terminalKeyManagement = new TerminalKeyManagement();
        terminalKeyManagement = new TerminalKeyManagement();
        String finalZMK = KeyUtils.buildZMK(env.getProperty("nibss.ck1"),env.getProperty("nibss.ck2"));
        log.info("finalZMK:"+ finalZMK);
        terminalKeyManagement = processorEngine.keyManagement(terminalId,finalZMK);
        if(!StringUtils.isEmpty(terminalKeyManagement.getParameterDownloaded())){
            NibssKeys terminalKeys = nibssKeysRepository.findFirstByTerminalID(terminalId);
            if(Objects.nonNull(terminalKeys)){
                updateTerminalKeys(terminalKeys, terminalKeyManagement);
            }else{
                saveTerminalKeys(terminalKeyManagement);
            }
        }
        return terminalKeyManagement;
    }
    private void updateTerminalKeys(NibssKeys terminalKeys, TerminalKeyManagement terminalKeyManagement) {
        terminalKeys.setMasterKey(terminalKeyManagement.getMasterKey());
        terminalKeys.setSessionKey(terminalKeyManagement.getSessionKey());
        terminalKeys.setPinKey(terminalKeyManagement.getPinKey());
        terminalKeys.setTerminalID(terminalKeyManagement.getTerminalID());
        terminalKeys.setParameterDownloaded(terminalKeyManagement.getParameterDownloaded());
        terminalKeys.setLastExchangeDateTime(terminalKeyManagement.getLastExchangeDateTime());
        terminalKeys.setIsSuccessful(terminalKeyManagement.getIsSuccessful());
        nibssKeysRepository.save(terminalKeys);
    }
    private void saveTerminalKeys(TerminalKeyManagement terminalKeyManagement) {
       // NibssKeys terminalKeys = modelMapper.map(terminalKeyManagement, NibssKeys.class);
        NibssKeys terminalKeys = new NibssKeys();
        terminalKeys.setTerminalID(terminalKeyManagement.getTerminalID());
        terminalKeys.setMasterKey(terminalKeyManagement.getMasterKey());
        terminalKeys.setPinKey(terminalKeyManagement.getPinKey());
        terminalKeys.setSessionKey(terminalKeyManagement.getSessionKey());
        terminalKeys.setIsSuccessful(terminalKeyManagement.getIsSuccessful());
        terminalKeys.setLastExchangeDateTime(terminalKeyManagement.getLastExchangeDateTime());
        terminalKeys.setParameterDownloaded(terminalKeyManagement.getParameterDownloaded());
        nibssKeysRepository.save(terminalKeys);
    }
}
