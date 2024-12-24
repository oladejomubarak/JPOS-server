
package com.celerpay.gopaykeyservice.util;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class CryptoUtil
{

    public static byte[] encrypt(final byte[] plainText, final byte[] key, final String algorithm, final String transformation) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] keyRef = key;
        if (keyRef.length == 16) {
            keyRef = new byte[24];
            System.arraycopy(key, 0, keyRef, 0, 16);
            System.arraycopy(key, 0, keyRef, 16, 8);
        }
        byte[] ivBytes = null;
        if (transformation != null && transformation.indexOf("CBC") != -1) {
            ivBytes = new byte[8];
            for (int i = 0; i < ivBytes.length; ++i) {
                ivBytes[i] = 0;
            }
        }
        final KeySpec ks = new DESedeKeySpec(keyRef);
        final SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
        final Cipher c = Cipher.getInstance(transformation);
        final SecretKey sKey = skf.generateSecret(ks);
        if (ivBytes != null) {
            final IvParameterSpec iv = new IvParameterSpec(ivBytes);
            c.init(1, sKey, iv);
        }
        else {
            c.init(1, sKey);
        }
        final byte[] cipherText = c.doFinal(plainText);
        return cipherText;
    }
    
    public static byte[] decrypt(final byte[] cipherText, final byte[] key, final String algorithm, final String transformation) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] keyRef = key;
        if (keyRef.length == 16) {
            keyRef = new byte[24];
            System.arraycopy(key, 0, keyRef, 0, 16);
            System.arraycopy(key, 0, keyRef, 16, 8);
        }
        byte[] ivBytes = null;
        if (transformation != null && transformation.indexOf("CBC") != -1) {
            ivBytes = new byte[8];
            for (int i = 0; i < ivBytes.length; ++i) {
                ivBytes[i] = 0;
            }
        }
        final KeySpec ks = new DESedeKeySpec(keyRef);
        final SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
        final Cipher c = Cipher.getInstance(transformation);
        final SecretKey sKey = skf.generateSecret(ks);
        if (ivBytes != null) {
            final IvParameterSpec iv = new IvParameterSpec(ivBytes);
            c.init(2, sKey, iv);
        }
        else {
            c.init(2, sKey);
        }
        final byte[] plainText = c.doFinal(cipherText);
        return plainText;
    }
}
