package com.celerpay.gopaykeyservice.config;

import org.jpos.iso.ISOException;
import org.jpos.iso.packager.GenericPackager;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ModelMapper mapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

    @Bean
    public GenericPackager getPackager() throws ISOException {
        return new GenericPackager("nibss.xml");
    }

}
