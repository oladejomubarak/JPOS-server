
package com.celerpay.gopaykeyservice.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GetParameterRequest extends KeyExchangeRequest
{
    private String processingCode;

}
