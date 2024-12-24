package com.celerpay.gopaynibssprovider.socketMgr;


import com.celerpay.gopaynibssprovider.utils.IsoResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import static com.celerpay.gopaynibssprovider.utils.StringUtils.bytesToShort;
import static com.celerpay.gopaynibssprovider.utils.StringUtils.shortToBytes;


@Slf4j
@Component
public class SocketManager {

    private SSLSocket socket;

    public SocketManager() {

    }


    public SocketManager(String endpoint, int port) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        final TrustManager[] trustAllCerts = { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
            }

            public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
            }
        } };

        final SSLContext sc = SSLContext.getInstance("TLSv1.2");
        sc.init(null, trustAllCerts, new SecureRandom());
        this.socket = (SSLSocket)sc.getSocketFactory().createSocket(endpoint, port);
    }

    public void disconnect() throws IOException {
        if (this.socket.isConnected()) {
            this.socket.close();
        }
    }

    public ISOMsg sendAndReceive(ISOMsg posRequest, final byte[] data, GenericPackager packager) throws IOException, ISOException {
        if (this.socket.isConnected()) {
            log.info("NIBSS service socket is connected:{}", socket.getInetAddress());
            DataInputStream dis = new DataInputStream(this.socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(this.socket.getOutputStream());
            short length = (short) data.length;
            byte[] headerBytes = shortToBytes(length);
            byte[] messagePayload = concat(headerBytes, data);
            dos.write(messagePayload);
            log.info("message sent to NIBSS");
            dos.flush();
            byte[] lenBytes = new byte[2];
            dis.readFully(lenBytes);
            int contentLength = bytesToShort(lenBytes);
            byte[] resp = new byte[contentLength];
            dis.readFully(resp);

            ISOMsg respMsg = new ISOMsg();
            respMsg.setPackager(packager);
            respMsg.unpack(resp);
            IsoResponseMapper isoResponseMapper = new IsoResponseMapper();
            log.info("Response RRN: {}| TID: {} | Code: {} | Desc: {}", posRequest.getString("37"),posRequest.getString("41"),
                    respMsg.getString("39"),isoResponseMapper.ResponseCodeMap(respMsg.getString("39")));

            this.disconnect();
            return respMsg;
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
