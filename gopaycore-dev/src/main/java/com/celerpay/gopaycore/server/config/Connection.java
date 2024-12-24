/*
 * *
 *  * Created by Oladejo Mubarak
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 7/9/24, 7:19 AM
 *
 */

package com.celerpay.gopaycore.server.config;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.jpos.q2.Q2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component @Slf4j
public class Connection implements CommandLineRunner {
    private static final String configDirectory = "deploy";
    Q2 server = new Q2(configDirectory);



    @Override
    public void run(String... args) {
        log.info("deploying MEDUSA HOST connection configs");
        server.start();

    }

    @PreDestroy
    public void stop(){
        log.info("un_deploying core connection configs");

        if (server.running())
            server.shutdown();
    }
}
