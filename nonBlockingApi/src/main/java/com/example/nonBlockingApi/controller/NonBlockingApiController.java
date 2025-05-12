package com.example.nonBlockingApi.controller;

import com.example.nonBlockingApi.ApiFactoryService.RestFactoryAsync;
import com.example.nonBlockingApi.DTO.RequestDTO;
import com.example.nonBlockingApi.Enums.ApiMethod;
import com.example.nonBlockingApi.model.InputRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.CompletableFuture;

@RestController
public class NonBlockingApiController {
    private static final Logger logger = LoggerFactory.getLogger(NonBlockingApiController.class);

    @Autowired
    private RestFactoryAsync  restFactoryAsync;

    @PostMapping("/aysncOrchestrationApi")
    public CompletableFuture<ResponseEntity<String>> invoke(@RequestBody InputRequest request) throws Exception {
        logger.info("Request Data is Received ");
        RequestDTO dto = request.getRequestDTO();
        ApiMethod apiMethod = request.getApiMethod();
        int timeout = request.getTimeout();

        return restFactoryAsync.executeTarget(apiMethod, dto, timeout)
                .thenApply(response -> {
                    logger.info("Response received with status: {}", response.statusCode());
                    return ResponseEntity.status(response.statusCode()).body(response.body());
                })
                .exceptionally(e -> {
                    logger.error("Error occurred during async invocation", e);
                    return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
                });

    }

}
