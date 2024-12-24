

package com.celerpay.gopaycore.utils;

import com.celerpay.gopaycore.pinCrypto.PinCrypto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.CryptoException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class PinTranslator {
    private final PinCrypto pinCrypto;
    private final Environment environment;

    public ISOMsg translateNibssPin(ISOMsg msg) throws CryptoException {
        String posKey = environment.getProperty("pos.pin.key");
        String coreKey = environment.getProperty("core.pin.key");
        msg.unset(59);

        String processingCode = msg.getString(3);
        String origAcctType=processingCode.substring(2,4);
        if(origAcctType.equalsIgnoreCase("10")){
            log.info("Processing code is Savings, send default for RRN | {}",msg.getString(37));
            String newProcessingCode = processingCode.substring(0,2) + "00" + processingCode.substring(4,6);

            msg.set(3, newProcessingCode);
        }

        final String pinblock = msg.getString(52);
        if (Objects.isNull(pinblock) || pinblock.trim().isEmpty()) {
            log.info("Transaction without pinblock");
            msg.unset(52);
            return msg;
        }
        else {
            final String clearPinblock = this.pinCrypto.decryptPinBlock(pinblock, posKey);
            final String nibssProviderPinBlock = this.pinCrypto.encryptPinBlock(clearPinblock, coreKey);
            msg.set(52, nibssProviderPinBlock.toUpperCase());
            return msg;
        }
    }

}
