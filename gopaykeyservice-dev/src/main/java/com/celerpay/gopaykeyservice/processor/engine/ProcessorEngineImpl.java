/*
 * *
 *  * Created by Oladejo Mubarak
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/24/24, 11:27 AM
 *
 */

package com.celerpay.gopaykeyservice.processor.engine;


import com.celerpay.gopaykeyservice.dto.*;
import com.celerpay.gopaykeyservice.util.CryptoUtil;
import com.celerpay.gopaykeyservice.util.DataUtil;
import com.celerpay.gopaykeyservice.util.GeneralUtils;
import com.celerpay.gopaykeyservice.util.ParameterParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.celerpay.gopaykeyservice.util.GeneralUtils.getDate;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessorEngineImpl implements ProcessorEngine {

    private final IsoProcessor isoProcessor;

    @Override
    public TerminalKeyManagement keyManagement(String terminalId, String zmk) {
        TerminalKeyManagement terminalKeyManagement = new TerminalKeyManagement();
        String masterKey;
        String sessionKey;
        String pinKey;
        String parameters;
        //process masterKey
        masterKey = processMasterKey(terminalId, zmk);

        //process sessionKey
        sessionKey = processSessionKey(terminalId, masterKey);


        //process pinkKey
        pinKey = processPinKey(terminalId, masterKey);

        //process Param Download
        parameters = processParamDownload(terminalId, sessionKey);

        if (!StringUtils.isEmpty(parameters)) {
            terminalKeyManagement.setMasterKey(masterKey);
            terminalKeyManagement.setSessionKey(sessionKey);
            terminalKeyManagement.setPinKey(pinKey);
            terminalKeyManagement.setParameterDownloaded(parameters);
            terminalKeyManagement.setTerminalID(terminalId);
            terminalKeyManagement.setIsSuccessful("true");
            terminalKeyManagement.setLastExchangeDateTime(getDate());
            log.info(terminalKeyManagement.toString());
            return terminalKeyManagement;
        }

        return null;
    }

    private String processMasterKey(String terminalId, String zmk) {
        GetMasterKeyRequest masterKeyRequest = new GetMasterKeyRequest();
        masterKeyRequest.setCardAcceptorTerminalId(terminalId);
        masterKeyRequest.setProcessingCode("9A0000");
        masterKeyRequest.setDateLocalTransaction(DataUtil.dateLocalTransaction(new Date()));
        masterKeyRequest.setTimeLocalTransaction(DataUtil.timeLocalTransaction(new Date()));
        masterKeyRequest.setTransmissionDateAndTime(DataUtil.transmissionDateAndTime(new Date()));
        masterKeyRequest.setSystemTraceAuditNumber(RandomStringUtils.randomNumeric(6));
        GetMasterKeyResponse masterKeyResponse = isoProcessor.process(masterKeyRequest);
        final byte[] clearKey = getClearKey(zmk, masterKeyResponse.getEncryptedMasterKey());
        masterKeyResponse.setClearMasterKey(clearKey);

        return GeneralUtils.bytesToHex(masterKeyResponse.getClearMasterKey());
    }



    private String processSessionKey(String terminalId, String masterKey) {
        GetSessionKeyRequest sessionKeyRequest = new GetSessionKeyRequest();
        sessionKeyRequest.setCardAcceptorTerminalId(terminalId);
        sessionKeyRequest.setProcessingCode("9B0000");
        sessionKeyRequest.setDateLocalTransaction(DataUtil.dateLocalTransaction(new Date()));
        sessionKeyRequest.setTimeLocalTransaction(DataUtil.timeLocalTransaction(new Date()));
        sessionKeyRequest.setTransmissionDateAndTime(DataUtil.transmissionDateAndTime(new Date()));
        sessionKeyRequest.setSystemTraceAuditNumber(RandomStringUtils.randomNumeric(6));
        GetSessionKeyResponse sessionKeyResponse = isoProcessor.process(sessionKeyRequest);
        final byte[] clearKey = getClearKey(masterKey, sessionKeyResponse.getEncryptedSessionKey());
        sessionKeyResponse.setClearSessionKey(clearKey);

        return GeneralUtils.bytesToHex(sessionKeyResponse.getClearSessionKey());
    }

    private String processPinKey(String terminalId, String masterKey) {
        GetPinKeyRequest pinKeyRequest = new GetPinKeyRequest();
        pinKeyRequest.setCardAcceptorTerminalId(terminalId);
        pinKeyRequest.setProcessingCode("9G0000");
        pinKeyRequest.setDateLocalTransaction(DataUtil.dateLocalTransaction(new Date()));
        pinKeyRequest.setTimeLocalTransaction(DataUtil.timeLocalTransaction(new Date()));
        pinKeyRequest.setTransmissionDateAndTime(DataUtil.transmissionDateAndTime(new Date()));
        pinKeyRequest.setSystemTraceAuditNumber(RandomStringUtils.randomNumeric(6));
        GetPinKeyResponse pinKeyResponse = isoProcessor.process(pinKeyRequest);
        final byte[] clearKey = getClearKey(masterKey, pinKeyResponse.getEncryptedPinKey());
        pinKeyResponse.setClearPinKey(clearKey);

        return GeneralUtils.bytesToHex(pinKeyResponse.getClearPinKey());
    }

    private String processParamDownload(String terminalId, String sessionKey) {
        GetParameterRequest parameterRequest = new GetParameterRequest();
        parameterRequest.setCardAcceptorTerminalId(terminalId);
        parameterRequest.setProcessingCode("9C0000");
        parameterRequest.setDateLocalTransaction(DataUtil.dateLocalTransaction(new Date()));
        parameterRequest.setTimeLocalTransaction(DataUtil.timeLocalTransaction(new Date()));
        parameterRequest.setTransmissionDateAndTime(DataUtil.transmissionDateAndTime(new Date()));
        parameterRequest.setSystemTraceAuditNumber(RandomStringUtils.randomNumeric(6));
        GetParameterResponse parameterResponse = isoProcessor.process(parameterRequest,GeneralUtils.hexStringToByteArray(sessionKey));
        if(Objects.nonNull(parameterResponse) && parameterResponse.getField39().equals("00")
                && !StringUtils.isEmpty(parameterResponse.getField62())){
            final Map<String, String> decodedParameters = ParameterParser.parseParameters(parameterResponse.getField62());
            log.info("PREF_CARD_ACCEPTOR_ID: {}", decodedParameters.get("03"));
            log.info("PREF_CARD_ACCEPTOR_LOC: {}", decodedParameters.get("52"));
            log.info("PREF_CURRENCY_CODE: {}", decodedParameters.get("05"));
            log.info("PREF_MERCHANT_TYPE: {}", decodedParameters.get("08"));
            return parameterResponse.getField62();
        }
        return "";
    }

    private static byte[] getClearKey(String transportKey, String encryptedKey) {
        final byte[] transportKeyBytes = GeneralUtils.hexStringToByteArray(transportKey);
        final byte[] encryptedBytes = GeneralUtils.hexStringToByteArray(encryptedKey);
        final byte[] clearKeyBytes;
        try {
            clearKeyBytes = CryptoUtil.decrypt(encryptedBytes, transportKeyBytes, "DESede", "DESede/ECB/NoPadding");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return clearKeyBytes;
    }
}
