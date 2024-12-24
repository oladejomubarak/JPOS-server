package com.celerpay.gopaycore.validation;

import org.jpos.iso.ISOMsg;

import java.util.function.Function;

public interface MessageValidator{

    Function<ISOMsg,Boolean> _0800Validator();

    Function<ISOMsg,Boolean> _0200Validator();

    Function<ISOMsg,Boolean> _0100Validator();

    Function<ISOMsg,Boolean> _0420Validator();

}
