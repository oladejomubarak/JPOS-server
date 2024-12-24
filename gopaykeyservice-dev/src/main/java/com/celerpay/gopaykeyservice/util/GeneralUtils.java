
package com.celerpay.gopaykeyservice.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneralUtils
{
    
    public static String bytesToHex(final byte[] bytes) {
        if (bytes != null) {
            final char[] hexArray = "0123456789ABCDEF".toCharArray();
            final char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; ++j) {
                final int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0xF];
            }
            return new String(hexChars);
        }
        return "";
    }

    public static String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    public static byte[] hexStringToByteArray(final String s) {
        final int len = s.length();
        final byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
