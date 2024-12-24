package com.celerpay.gopaycore.utils;


import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.apache.logging.log4j.util.Strings.isEmpty;

public class StringUtils
{

    public static short bytesToShort(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).getShort();
    }

    public static byte[] shortToBytes(final short value) {
        return new byte[] { (byte)(value >> 8), (byte)value };
    }

}
