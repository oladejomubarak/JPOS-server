
package com.celerpay.gopaykeyservice.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GetMasterKeyResponse
{
    private String field39;
    private String encryptedMasterKey;
    private byte[] clearMasterKey;


}
