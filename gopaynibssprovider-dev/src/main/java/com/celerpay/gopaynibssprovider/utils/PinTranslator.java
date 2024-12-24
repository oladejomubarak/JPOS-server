

package com.celerpay.gopaynibssprovider.utils;

import com.celerpay.gopaynibssprovider.model.NibssKeys;
import com.celerpay.gopaynibssprovider.pinCrypto.PinCrypto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.CryptoException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;

import static com.celerpay.gopaynibssprovider.utils.StringUtils.bytesToHex;

@Service
@Slf4j
@RequiredArgsConstructor
public class PinTranslator {
    private final PinCrypto pinCrypto;
    private final Environment environment;

    public ISOMsg translateNibssPin(ISOMsg msg, NibssKeys nibssKeys) throws CryptoException, ISOException {
        String coreKey = environment.getProperty("core.pin.key");


        final byte[] nibssSessionKeyBytes = StringUtils.hexStringToByteArray(nibssKeys.getSessionKey());

        String parameterDownloaded = nibssKeys.getParameterDownloaded();

        String bin = msg.getString(2);
        bin = bin.substring(0, 6);
        msg.set(32, bin);

        msg.set(28, "D00000000");

        final Map<String, String> decodedParameters = ParameterParser.parseParameters(parameterDownloaded);
        String merchantID = decodedParameters.get("03");
        msg.set(42, merchantID);
        log.info(merchantID);
        String merLoc = decodedParameters.get("52");
        msg.set(43, merLoc);
        log.info(merLoc);
        String merType = decodedParameters.get("08");
        msg.set(18, merType);
        log.info(merType);
        msg.unset(59);
        msg.unset(60);

        final String pinblock = msg.getString(52);
        if (Objects.isNull(pinblock) || pinblock.trim().isEmpty()) {
            log.info("Transaction without pinblock");
            msg.unset(52);

        } else {
            final String clearPinblock = this.pinCrypto.decryptPinBlock(pinblock, coreKey);
            final String nibssProviderPinBlock = this.pinCrypto.encryptPinBlock(clearPinblock, nibssKeys.getPinKey());
            msg.set(52, nibssProviderPinBlock.toUpperCase());

        }

        final String hashHex = getFld128(msg, nibssSessionKeyBytes);
        msg.set(128, hashHex);
        return msg;
    }

    private String getFld128(ISOMsg msg, byte[] nibssSessionKeyBytes) throws ISOException {
        final byte[] bites = msg.pack();
        final int length = bites.length;
        final byte[] temp = new byte[length - 64];
        if (length >= 64) {
            System.arraycopy(bites, 0, temp, 0, length - 64);
        }
        final String hashHex = generateHash256Value(temp, nibssSessionKeyBytes);
        return hashHex;
    }

    public String generateHash256Value(final byte[] iso, final byte[] key) {
        String hashText = null;
        try {
            final MessageDigest m = MessageDigest.getInstance("SHA-256");
            m.update(key, 0, key.length);
            m.update(iso, 0, iso.length);
            hashText = bytesToHex(m.digest());
            hashText = hashText.replace(" ", "");
        } catch (NoSuchAlgorithmException ex) {
        }
        if (hashText.length() < 64) {
            final int numberOfZeroes = 64 - hashText.length();
            String zeroes = "";
            String temp = hashText;
            for (int i = 0; i < numberOfZeroes; ++i) {
                zeroes += "0";
            }
            temp = zeroes + temp;
            return temp;
        }
        return hashText;
    }

}
