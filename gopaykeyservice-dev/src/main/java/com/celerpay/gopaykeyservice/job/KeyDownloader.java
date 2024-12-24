package com.celerpay.gopaykeyservice.job;


import com.celerpay.gopaykeyservice.repository.NibssKeysRepository;
import com.celerpay.gopaykeyservice.service.KeyExchangeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeyDownloader {
    private final NibssKeysRepository nibssKeysRepository;
    private final KeyExchangeService keyExchange;

//    @Scheduled(cron = "0 0 0 * * ?")//every midnight
    @PostConstruct
    public void pushTerminalIDsForExchange(){

//        List<NibssKeys> findAll = nibssKeysRepository.findAll();
//        for (int i = 0; i < findAll.size(); i++) {
//            try{
//                NibssKeys nibssKeys = findAll.get(i);
//                keyExchange.doKeyExchange(nibssKeys.getTerminalID());
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }
        try {
            keyExchange.doKeyExchange("0532C987");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
