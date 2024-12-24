package com.celerpay.gopaynibssprovider.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity(name = "nibss_keys")
@Getter
@Setter
@ToString
public class NibssKeys {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String terminalID;


    private String masterKey;


    private String sessionKey;


    private String pinKey;


    private String parameterDownloaded;


    private String lastExchangeDateTime;


    private String isSuccessful;


}
