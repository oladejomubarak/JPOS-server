package com.celerpay.gopaycore.validation;

import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Slf4j
public class MessageValidatorImpl implements MessageValidator {


    @Override
    public Function<ISOMsg, Boolean> _0800Validator() {
        int[] _8088Fields = {0,7,11,12,13,70};
        return isoMsg -> isoMsg.hasFields(_8088Fields);
    }

    @Override
    public Function<ISOMsg, Boolean> _0200Validator() {
        int[] mandatory = {2,3,4,7,11,12,13,22,32,37,41,42,43};
        int[] optional = {2,13,14,17,18,20,24,23,25,26,28,32,33,35,36,37,40,41,42,43,45,46,49,52,55,56,59,60,63,90,95,99,111,112,122,123,128};

        return isoMsg -> {
            for (int fields:mandatory){
                if (!isoMsg.hasField(fields)){
                    log.info("Mandatory field missing for transaction :: field "+fields);
                    return false;
                }
            }

            for (int field:optional){
                if (!isoMsg.hasField(field)){
                }
            }
            return true;
        };

    }

    @Override
    public Function<ISOMsg, Boolean> _0100Validator() {
        int[] mandatory = {2,3,4,7,11,12,13,22,32,37,41,42,43};
        int[] optional = {2,13,14,17,18,20,24,23,25,26,28,32,33,35,36,37,40,41,42,43,45,46,49,52,55,56,59,60,63,90,95,99,111,112,122,123,128};

        return isoMsg -> {
            for (int fields:mandatory){
                if (!isoMsg.hasField(fields)){
                    log.info("Mandatory field missing for transaction :: field "+fields);
                    return false;
                }
            }

            for (int field:optional){
                if (!isoMsg.hasField(field)){
                }
            }
            return true;
        };
    }

    @Override
    public Function<ISOMsg, Boolean> _0420Validator() {
        int[] mandatory = {2,3,4,7,11,12,22,41};
        int[] optional = {2,13,14,17,18,20,24,23,25,26,28,32,33,35,36,37,38,40,41,42,43,45,46,49,52,55,56,59,60,63,90,95,99,111,112,122,123,128};

        return isoMsg -> {
            for (int fields:mandatory){
                if (!isoMsg.hasField(fields)){
                    log.info("Mandatory field missing for transaction :: field "+fields);
                    return false;
                }
            }

            for (int field:optional){
                if (!isoMsg.hasField(field)){
                }
            }
            return true;
        };
    }
}
