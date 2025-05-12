package com.example.nonBlockingApi.ApiFactoryService;

import com.example.nonBlockingApi.DTO.RequestDTO;
import com.example.nonBlockingApi.Enums.ApiMethod;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public interface ApiFactory {

    public CompletableFuture<HttpResponse<String>> executeTarget(
            ApiMethod method,
            RequestDTO dto,
            int timeoutMillis
    ) throws Exception;
}
