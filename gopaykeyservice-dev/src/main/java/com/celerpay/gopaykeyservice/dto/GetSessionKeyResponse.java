
package com.celerpay.gopaykeyservice.dto;


import com.celerpay.gopaykeyservice.util.CryptoUtil;
import com.celerpay.gopaykeyservice.util.GeneralUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GetSessionKeyResponse
{
    private byte[] clearSessionKey;
    private String encryptedSessionKey;

    
    public void decryptSessionKey(final byte[] tmk) throws Exception {
        final byte[] sessionKeyBytes = GeneralUtils.hexStringToByteArray(this.encryptedSessionKey);
        final byte[] clearSessionKeyBytes = CryptoUtil.decrypt(sessionKeyBytes, tmk, "DESede", "DESede/ECB/NoPadding");
        this.clearSessionKey = clearSessionKeyBytes;
    }
}
