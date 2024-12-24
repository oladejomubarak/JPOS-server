/*
 * *
 *  * Created by Oladejo Mubarak
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 1/22/24, 3:37 PM
 *
 */

package com.celerpay.gopaynibssprovider.pinCrypto;

import com.celerpay.gopaynibssprovider.utils.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.CryptoException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class PinCryptoImpl implements PinCrypto{

    @Override
    public String decryptPinBlock(String pinBlock, String pinKey) throws CryptoException{
        log.info("Start decrypting pinblock");
        try {
            byte[] tmsKeyBytes = Hex.decodeHex(pinKey.toCharArray());
            byte[] pinBlockBytes = Hex.decodeHex(pinBlock.toCharArray());
            byte[] clearPinBlockBytes = EncryptionUtil.tdesDecryptECB(pinBlockBytes, tmsKeyBytes);

            return new String(Hex.encodeHex(clearPinBlockBytes));
        } catch (DecoderException e) {
            throw new CryptoException("Could not decode hex key", e);
        }
    }

    @Override
    public String encryptPinBlock(String pinBlock, String key) throws CryptoException{
        log.info("The pin block bytes {} ", pinBlock);
        if (StringUtils.isEmpty(pinBlock)) {
            return pinBlock;
        }
        byte[] clearPinBlockBytes;
        byte[] zpk;
        try {
            clearPinBlockBytes = Hex.decodeHex(pinBlock.toCharArray());
            zpk = Hex.decodeHex(key.toCharArray());
            log.info("The clear zpk {} ", key.toCharArray());
        } catch (DecoderException e) {
            throw new CryptoException("Could not decode pin block", e);
        }

        byte[] encryptedPinBlockBytes = EncryptionUtil.tdesEncryptECB(clearPinBlockBytes, zpk);

        return new String(Hex.encodeHex(encryptedPinBlockBytes));
    }


}
