package com.example.real_common.global.restClient;

import org.springframework.web.client.RestClient;

public class CustomRestClient {

    private static final String baseUrl = "http://localhost";

    public static RestClient connectPbRestClient(String path) {
        return RestClient.builder()
                .baseUrl(baseUrl + ":8002/pickle-pb" + path)
                .build();
    }

//    public static RestClient connectCustomerRestClient(String path) {}
//    public static RestClient connectCommonRestClient(String path) {}
}
