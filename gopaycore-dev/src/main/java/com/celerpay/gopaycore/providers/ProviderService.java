/*
 * *
 *  * Created by Oladejo Mubarak
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 7/9/24, 7:26 AM
 *
 */

package com.celerpay.gopaycore.providers;

import com.celerpay.gopaycore.dto.Response;
import org.bouncycastle.crypto.CryptoException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public interface ProviderService {
    Response processPayment(ISOMsg posRequest, GenericPackager packager) throws IOException, NoSuchAlgorithmException, KeyManagementException, ISOException, CryptoException;
}
