

package com.celerpay.gopaykeyservice.processor.network;


import com.celerpay.gopaykeyservice.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


@Component
@Slf4j
public class SocketManager {

    private SSLSocket socket;

    public SocketManager() {

    }

    public SocketManager(final String endpoint, final int port) throws IOException, KeyManagementException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException {
        final TrustManager[] trustAllCerts = {new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                final X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                return myTrustedAnchors;
            }

            public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
            }

            public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
            }
        }};
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        this.socket = (SSLSocket) sc.getSocketFactory().createSocket(endpoint, port);
        this.socket.setSoTimeout(60000);
    }


    public void disconnect() throws IOException {
        if (this.socket.isConnected()) {
            this.socket.close();
        }
    }

    public byte[] sendAndReceiveData(final byte[] data) throws IOException {
        if (this.socket.isConnected()) {
            final DataOutputStream os = new DataOutputStream(this.socket.getOutputStream());
            final DataInputStream is = new DataInputStream(this.socket.getInputStream());
            final short length = (short) data.length;
            final byte[] headerBytes = DataUtil.shortToBytes(length);
            final byte[] messagePayload = concat(headerBytes, data);
            os.write(messagePayload);
            os.flush();
            final byte[] lenBytes = new byte[2];
            is.readFully(lenBytes);
            final int contentLength = DataUtil.bytesToShort(lenBytes);
            final byte[] resp = new byte[contentLength];
            is.readFully(resp);
            String s = new String(resp);
            log.info("Response: {}", s);
            return resp;
        }
        throw new IOException("Socket not connected");
    }


    private static byte[] concat(final byte[] A, final byte[] B) {
        final int aLen = A.length;
        final int bLen = B.length;
        final byte[] C = new byte[aLen + bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }


}
