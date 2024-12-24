package com.celerpay.gopaycore.socketMgr;

import com.celerpay.gopaycore.dto.Response;
import com.celerpay.gopaycore.utils.IsoResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import static com.celerpay.gopaycore.utils.StringUtils.bytesToShort;
import static com.celerpay.gopaycore.utils.StringUtils.shortToBytes;


@Slf4j
@Component
public class SocketManager {

    private Socket socket;

    public SocketManager() {

    }


    public SocketManager(String endpoint, int port) throws IOException {
        new Scanner(System.in);
        InetAddress ip = InetAddress.getByName(endpoint);
        this.socket = new Socket(ip, port);
    }

    public void disconnect() throws IOException {
        if (this.socket.isConnected()) {
            this.socket.close();
        }
    }

    public Response sendAndReceive(ISOMsg posRequest, final byte[] data, GenericPackager packager) throws IOException, ISOException {
        if (this.socket.isConnected()) {
            log.info("processor service socket is connected:{}", socket.getInetAddress());
            Response responseObj = new Response();
            DataInputStream dis = new DataInputStream(this.socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(this.socket.getOutputStream());
            short length = (short) data.length;
            byte[] headerBytes = shortToBytes(length);
            byte[] messagePayload = concat(headerBytes, data);
            dos.write(messagePayload);
            log.info("message sent to provider service");
            dos.flush();
            byte[] lenBytes = new byte[2];
            dis.readFully(lenBytes);
            int contentLength = bytesToShort(lenBytes);
            byte[] resp = new byte[contentLength];
            dis.readFully(resp);

            ISOMsg respMsg = new ISOMsg();
            respMsg.setPackager(packager);
            respMsg.unpack(resp);
            responseObj.setResponseMsg(respMsg);
            respMsg.set(4, posRequest.getString(4));
            respMsg.set(3, posRequest.getString(3));
            respMsg.set(32, posRequest.getString(32));
            respMsg.set(7, posRequest.getString(7));
            IsoResponseMapper isoResponseMapper = new IsoResponseMapper();
            log.info("Response RRN: {}| TID: {} | Code: {} | Desc: {}", posRequest.getString("37"),posRequest.getString("41"),
                    respMsg.getString("39"),isoResponseMapper.ResponseCodeMap(respMsg.getString("39")));
            responseObj.setResponseIso(respMsg);
            this.disconnect();
            return responseObj;
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
