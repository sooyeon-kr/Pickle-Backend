package com.example.real_common.global.restClient;

import org.springframework.web.client.RestClient;

public class CustomRestClient {

    private static final String baseUrl = "http://localhost";

    public static RestClient connectPb(String path) {
        return RestClient.builder()
                .baseUrl(path)
                .build();
    }

    public static RestClient connectCommon(String path) {
        return RestClient.builder()
                .baseUrl(baseUrl + ":8004/api/pickle-common" + path)
                .build();
    }
//    public static RestClient connectCustomerRestClient(String path) {}
}
