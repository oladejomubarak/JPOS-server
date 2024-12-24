
package com.celerpay.gopaykeyservice.util;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DataUtil
{

    public static short bytesToShort(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).getShort();
    }
    
    public static byte[] shortToBytes(final short value) {
        return new byte[] { (byte)(value >> 8), (byte)value };
    }

    public static String timeLocalTransaction(final Date transDate) {
        final Calendar myCal = Calendar.getInstance();
        final TimeZone timeZone = TimeZone.getTimeZone("Africa/Luanda");
        myCal.setTimeZone(timeZone);
        return String.format("%02d%02d%02d", myCal.get(11), myCal.get(12), myCal.get(13));
    }

    public static String transmissionDateAndTime(final Date transDate) {
        final Calendar myCal = Calendar.getInstance();
        final TimeZone timeZone = TimeZone.getTimeZone("Africa/Luanda");
        myCal.setTimeZone(timeZone);
        return String.format("%02d%02d%02d%02d%02d", myCal.get(2) + 1, myCal.get(5), myCal.get(11), myCal.get(12), myCal.get(13));
    }

    public static String dateLocalTransaction(final Date transDate) {
        final Calendar myCal = Calendar.getInstance();
        final TimeZone timeZone = TimeZone.getTimeZone("Africa/Luanda");
        myCal.setTimeZone(timeZone);
        return String.format("%02d%02d", myCal.get(2) + 1, myCal.get(5));
    }
}
