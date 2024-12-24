package com.celerpay.gopaynibssprovider.utils;


import java.nio.ByteBuffer;

public class StringUtils
{

    public static short bytesToShort(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).getShort();
    }

    public static byte[] shortToBytes(final short value) {
        return new byte[] { (byte)(value >> 8), (byte)value };
    }

    public static byte[] hexStringToByteArray(final String s) {
        final int len = s.length();
        final byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String bytesToHex(final byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        final char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; ++j) {
            final int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0xF];
        }
        return new String(hexChars);
    }
}
