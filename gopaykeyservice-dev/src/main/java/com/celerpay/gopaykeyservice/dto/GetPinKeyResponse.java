
package com.celerpay.gopaykeyservice.dto;

import com.celerpay.gopaykeyservice.util.CryptoUtil;
import com.celerpay.gopaykeyservice.util.GeneralUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GetPinKeyResponse
{
    private byte[] clearPinKey;
    private String encryptedPinKey;

    public void descryptPinKey(final byte[] tmk) throws Exception {
        final byte[] pinKeyBytes = GeneralUtils.hexStringToByteArray(this.encryptedPinKey.replace(" ", ""));
        this.clearPinKey = CryptoUtil.decrypt(pinKeyBytes, tmk, "DESede", "DESede/ECB/NoPadding");
    }
}
