
package com.celerpay.gopaykeyservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GetMasterKeyRequest extends KeyExchangeRequest
{
    private String processingCode;
}
