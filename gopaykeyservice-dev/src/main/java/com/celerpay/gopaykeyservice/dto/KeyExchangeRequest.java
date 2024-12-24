
package com.celerpay.gopaykeyservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class KeyExchangeRequest
{
    private String transmissionDateAndTime;
    private String systemTraceAuditNumber;
    private String timeLocalTransaction;
    private String dateLocalTransaction;
    private String cardAcceptorTerminalId;
    private String managementDataOne;
    private String managementDataTwo;
    private String messageHashValue;
    

}
