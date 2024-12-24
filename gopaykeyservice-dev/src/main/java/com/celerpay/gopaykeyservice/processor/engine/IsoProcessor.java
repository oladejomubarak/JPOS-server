
package com.celerpay.gopaykeyservice.processor.engine;


import com.celerpay.gopaykeyservice.dto.*;
import com.celerpay.gopaykeyservice.processor.network.SocketManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static com.celerpay.gopaykeyservice.util.GeneralUtils.bytesToHex;


@Slf4j
@Service
@RequiredArgsConstructor
public class IsoProcessor {

    @Autowired
    GenericPackager packager;

    private final Environment env;

    public GetMasterKeyResponse process(final GetMasterKeyRequest request) {
        GetMasterKeyResponse response = null;
        SocketManager socketRequester = null;
        try {
            final ISOMsg ismsg = new ISOMsg();
            ismsg.setPackager(packager);
            ismsg.setMTI("0800");
            ismsg.set(3, request.getProcessingCode());
            ismsg.set(7, request.getTransmissionDateAndTime());
            ismsg.set(11, request.getSystemTraceAuditNumber());
            ismsg.set(12, request.getTimeLocalTransaction());
            ismsg.set(13, request.getDateLocalTransaction());
            ismsg.set(41, request.getCardAcceptorTerminalId());
            ismsg.set(62, "0100820390018");
            printIsoFields(ismsg, "MASTER KEY REQUEST ====> ");
            final byte[] messagepayload = ismsg.pack();
            socketRequester = new SocketManager(env.getProperty("nibss.ip"), Integer.parseInt(Objects.requireNonNull(env.getProperty("nibss.port"))));
            final byte[] responseBytes = socketRequester.sendAndReceiveData(messagepayload);

            ISOMsg isoResponseMsg = new ISOMsg();
            isoResponseMsg.setPackager(packager);
            isoResponseMsg.unpack(responseBytes);

            printIsoFields(isoResponseMsg, "MASTER KEY RESPONSE ====> ");
            response = new GetMasterKeyResponse();
            if (isoResponseMsg.hasField(39)) {
                response.setField39(isoResponseMsg.getString(39));
            }
            if (isoResponseMsg.hasField(53)) {
                response.setEncryptedMasterKey(isoResponseMsg.getString(53).substring(0, 32));
            }

            log.info("Get masterkey response: {}", response);
        } catch (Exception e) {
            response = new GetMasterKeyResponse();
            response.setField39("-1");
            log.info("Failed to get pin key" + e);
        } finally {
            if (socketRequester != null) {
                try {
                    socketRequester.disconnect();
                } catch (IOException ex) {
                    log.info("Failed to disconnect socket ");
                }
            }
        }
        return response;
    }

    public GetSessionKeyResponse process(final GetSessionKeyRequest request) {
        GetSessionKeyResponse response = null;
        SocketManager socketRequester = null;
        try {
            final ISOMsg ismsg = new ISOMsg();
            ismsg.setPackager(packager);
            ismsg.setMTI("0800");
            ismsg.set(3, request.getProcessingCode());
            ismsg.set(7, request.getTransmissionDateAndTime());
            ismsg.set(11, request.getSystemTraceAuditNumber());
            ismsg.set(12, request.getTimeLocalTransaction());
            ismsg.set(13, request.getDateLocalTransaction());
            ismsg.set(41, request.getCardAcceptorTerminalId());
            ismsg.set(62, "0100820390018");
            printIsoFields(ismsg, "SESSION KEY REQUEST ====> ");
            final byte[] messagepayload = ismsg.pack();
            socketRequester = new SocketManager(env.getProperty("nibss.ip"),
                    Integer.parseInt(Objects.requireNonNull(env.getProperty("nibss.port"))));
            final byte[] responseBytes = socketRequester.sendAndReceiveData(messagepayload);

            ISOMsg isoResponseMsg = new ISOMsg();
            isoResponseMsg.setPackager(packager);
            isoResponseMsg.unpack(responseBytes);
            printIsoFields(isoResponseMsg, "SESSION KEY RESPONSE ====> ");
            response = new GetSessionKeyResponse();
            if (isoResponseMsg.hasField(53)) {
                response.setEncryptedSessionKey(isoResponseMsg.getString(53).substring(0, 32));
            }

        } catch (Exception e2) {
            response = new GetSessionKeyResponse();
            log.info("Failed to get session key" + e2);
        } finally {
            if (socketRequester != null) {
                try {
                    socketRequester.disconnect();
                } catch (IOException ex) {
                    log.info("Failed to disconnect socket");
                }
            }
        }
        return response;
    }

    public GetPinKeyResponse process(final GetPinKeyRequest request) {
        GetPinKeyResponse response = null;
        SocketManager socketRequester = null;
        try {
            final ISOMsg ismsg = new ISOMsg();
            ismsg.setPackager(packager);
            ismsg.setMTI("0800");
            ismsg.set(3, request.getProcessingCode());
            ismsg.set(7, request.getTransmissionDateAndTime());
            ismsg.set(11, request.getSystemTraceAuditNumber());
            ismsg.set(12, request.getTimeLocalTransaction());
            ismsg.set(13, request.getDateLocalTransaction());
            ismsg.set(41, request.getCardAcceptorTerminalId());
            ismsg.set(62, "0100820390018");
            printIsoFields(ismsg, "PIN KEY REQUEST ====> ");
            final byte[] messagepayload = ismsg.pack();
            socketRequester = new SocketManager(env.getProperty("nibss.ip"),
                    Integer.parseInt(Objects.requireNonNull(env.getProperty("nibss.port"))));
            final byte[] responseBytes = socketRequester.sendAndReceiveData(messagepayload);

            ISOMsg isoResponseMsg = new ISOMsg();
            isoResponseMsg.setPackager(packager);
            isoResponseMsg.unpack(responseBytes);

            printIsoFields(isoResponseMsg, "PIN KEY RESPONSE ====> ");
            response = new GetPinKeyResponse();
            if (isoResponseMsg.hasField(53)) {
                response.setEncryptedPinKey(isoResponseMsg.getString(53).substring(0, 32));
            }

        } catch (Exception e2) {
            response = new GetPinKeyResponse();
            e2.printStackTrace();
        } finally {
            if (socketRequester != null) {
                try {
                    socketRequester.disconnect();
                } catch (IOException ex) {
                    log.info("Failed to disconnect socket");
                }
            }
        }
        return response;
    }

    public GetParameterResponse process(final GetParameterRequest request, final byte[] sessionKey) {
        GetParameterResponse response = null;
        SocketManager socketRequester = null;
        try {
            final ISOMsg ismsg = new ISOMsg();
            ismsg.setPackager(packager);
            ismsg.setMTI("0800");
            ismsg.set(3, request.getProcessingCode());
            ismsg.set(7, request.getTransmissionDateAndTime());
            ismsg.set(11, request.getSystemTraceAuditNumber());
            ismsg.set(12, request.getTimeLocalTransaction());
            ismsg.set(13, request.getDateLocalTransaction());
            ismsg.set(41, request.getCardAcceptorTerminalId());
            ismsg.set(62, "0100820390018");
            ismsg.set(64, "AA8F79C32F1933A4CB24D0AA62C173F76C37D9D83A766A9FB2A57647CA5D6005");

            final byte[] bites = ismsg.pack();
            log.info("Get Params bytes {}" + new String(bites));
            final int length = bites.length;
            final byte[] temp = new byte[length - 64];
            if (length >= 64) {
                System.arraycopy(bites, 0, temp, 0, length - 64);
            }
            final String hashHex = generateHash256Value(temp, sessionKey);
            ismsg.set(64, hashHex);

            printIsoFields(ismsg, "PARAM REQUEST ====> ");
            final byte[] messagepayload = ismsg.pack();
            socketRequester = new SocketManager(env.getProperty("nibss.ip"),
                    Integer.parseInt(Objects.requireNonNull(env.getProperty("nibss.port"))));
            final byte[] responseBytes = socketRequester.sendAndReceiveData(messagepayload);

            ISOMsg isoResponseMsg = new ISOMsg();
            isoResponseMsg.setPackager(packager);
            isoResponseMsg.unpack(responseBytes);
            response = new GetParameterResponse();
            if (isoResponseMsg.hasField(39)) {
                response.setField39(isoResponseMsg.getString(39));
            }
            if (isoResponseMsg.hasField(62)) {
                response.setField62(isoResponseMsg.getString(62));
            }
        } catch (
                Exception e) {
            log.info("Failed to get params key" + e);
            if (socketRequester != null) {
                try {
                    socketRequester.disconnect();
                } catch (IOException ex) {
                    log.info("Failed to disconnect socket");
                }
            }
        } finally {
            if (socketRequester != null) {
                try {
                    socketRequester.disconnect();
                } catch (IOException ex2) {
                    log.info("Failed to disconnect socket" + 2);
                }
            }
        }
        return response;
    }

    public static void printIsoFields(final ISOMsg isoMsg, final String type) {
        if (isoMsg == null) {
            return;
        }
        log.info("==================================================");
        log.info(type);
        for (int index = 1; index <= 128; ++index) {
            if (isoMsg.hasField(index)) {
                if (index == 2) {
                    String pan = maskPanForReceipt(isoMsg.getString(index));
                    log.info("<field {}> = {}", index, pan);
                } else {
                    log.info("field {} : {}", index, isoMsg.getString(index));
                }
            }
        }
    }

    public static String maskPanForReceipt(String pan) {
        StringBuilder maskedPan = new StringBuilder();
        int maskingStartIndex = 6;
        int maskingEndIndex = pan.length() - 4;

        for (int index = 0; index < pan.length(); index++) {
            if (index >= maskingStartIndex && index < maskingEndIndex) {
                maskedPan.append("*");
            } else {
                maskedPan.append(pan.charAt(index));
            }
        }
        return maskedPan.toString();
    }


    public static String generateHash256Value(final byte[] iso, final byte[] key) {
        String hashText = null;
        try {
            final MessageDigest m = MessageDigest.getInstance("SHA-256");
            m.update(key, 0, key.length);
            m.update(iso, 0, iso.length);
            hashText = bytesToHex(m.digest());
            hashText = hashText.replace(" ", "");
        } catch (NoSuchAlgorithmException ex) {
            log.info("Hashing ");
        }
        if (hashText.length() < 64) {
            final int numberOfZeroes = 64 - hashText.length();
            String zeroes = "";
            String temp = hashText;
            for (int i = 0; i < numberOfZeroes; ++i) {
                zeroes += "0";
            }
            temp = zeroes + temp;
            log.info("Utility :: generateHash256Value :: HashValue with zeroes: {}" + temp);
            return temp;
        }
        return hashText;
    }

}
