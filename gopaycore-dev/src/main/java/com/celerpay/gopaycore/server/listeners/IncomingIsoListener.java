package com.celerpay.gopaycore.server.listeners;

import com.celerpay.gopaycore.handler.RequestHandler;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class IncomingIsoListener extends Log implements ISORequestListener {
    Logger log = LoggerFactory.getLogger(IncomingIsoListener.class);

    @Autowired
    RequestHandler requestHandler;



    @Override
    public boolean process(ISOSource source, ISOMsg posRequest) {
        ISOMsg response = new ISOMsg();
        try {
                //call business logic service
                response = requestHandler.processRequest(posRequest);

        } catch (Exception e) {
            e.printStackTrace();
            response = (ISOMsg) posRequest.clone();
            response.set(39,"68");
            log.error("Error message {}",e.getMessage());
        }

        finally {
            try {
                if (!response.isResponse())
                    response.setResponseMTI();
                if (response.hasField(98))
                    response.unset(44,98, 113,103,127);

                source.send(response);
            } catch (IOException | ISOException e) {
                e.printStackTrace();
                log.error("Error sending response {}",e.getMessage());
            }

        }

        return true;
    }


}
