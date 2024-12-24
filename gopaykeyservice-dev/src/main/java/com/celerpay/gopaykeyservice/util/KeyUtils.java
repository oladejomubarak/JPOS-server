package com.celerpay.gopaykeyservice.util;

public class KeyUtils {
    // Function to convert a hexadecimal string to a byte array
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    // Function to convert a byte array to a hexadecimal string
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    // Function to XOR two component keys and return the result as a hexadecimal string
    public static String buildZMK(String... keysHex) {
        if (keysHex.length < 2) {
            throw new IllegalArgumentException("At least two keys are required");
        }

        byte[][] byteKeys = new byte[keysHex.length][];
        for (int i = 0; i < keysHex.length; i++) {
            byteKeys[i] = hexStringToByteArray(keysHex[i]);
        }

        int length = byteKeys[0].length;
        for (byte[] byteKey : byteKeys) {
            if (byteKey.length != length) {
                throw new IllegalArgumentException("All keys must be of the same length");
            }
        }

        byte[] result = new byte[length];

        for (int i = 0; i < length; i++) {
            byte temp = byteKeys[0][i]; // Initialize with the first key's byte
            for (int j = 1; j < byteKeys.length; j++) {
                temp ^= byteKeys[j][i]; // XOR with subsequent keys
            }
            result[i] = temp;
        }

        return byteArrayToHexString(result);
    }

}
