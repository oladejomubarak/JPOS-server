package com.celerpay.gopaycore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jpos.iso.ISOMsg;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private ISOMsg responseIso;
    private Object responseMsg;
}
