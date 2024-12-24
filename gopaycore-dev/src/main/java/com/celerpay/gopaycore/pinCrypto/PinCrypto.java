package com.celerpay.gopaycore.pinCrypto;

import org.bouncycastle.crypto.CryptoException;

public interface PinCrypto {
    String decryptPinBlock(String pinBlock, String pinKey) throws CryptoException;

    String encryptPinBlock(String newPinBlock, String pinKey) throws CryptoException;
}
