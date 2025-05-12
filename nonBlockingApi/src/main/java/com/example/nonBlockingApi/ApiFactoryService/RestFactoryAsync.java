package com.example.nonBlockingApi.ApiFactoryService;

import com.example.nonBlockingApi.DTO.NameValuePair;
import com.example.nonBlockingApi.DTO.RequestDTO;
import com.example.nonBlockingApi.Enums.ApiMethod;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
@Service
public class RestFactoryAsync implements ApiFactory{


    private HttpClient createHttpSecureClient(String url) throws Exception {

        if (url.startsWith("http://")) {
            char[] password = "changeit".toCharArray();
            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream("C:\\Users\\Lenovo\\mySSlCrt.jks")) {
                keyStore.load(fis, password);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
                e.printStackTrace();
            }

            // TrustManager: trust the certs in your keystore
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            return HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .sslContext(sslContext)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
        } else {
            return HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
        }
    }

    @Override
    public CompletableFuture<HttpResponse<String>> executeTarget(ApiMethod apiMethod, RequestDTO dto, int timeoutMillis) throws Exception {
         return switch (apiMethod) {
             case GET -> asyncGet(dto, timeoutMillis);
             case POST -> asyncPost(dto, timeoutMillis);
             case PUT -> asyncPut(dto, timeoutMillis);
             case PATCH -> asyncPatch(dto, timeoutMillis);
             case DELETE -> asyncDelete(dto, timeoutMillis);
             case OPTIONS -> asyncOptions(dto, timeoutMillis);
         };
    }
    private CompletableFuture<HttpResponse<String>> asyncGet(RequestDTO dto, int timeoutMillis) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dto.getUrl()))
                .timeout(Duration.ofMillis(timeoutMillis))
                .headers(getHeaders(dto.getHeaderVariables()))
                .GET()
                .build();
        HttpClient httpClient = createHttpSecureClient(dto.getUrl());
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private CompletableFuture<HttpResponse<String>> asyncPost(RequestDTO dto, int timeout) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dto.getUrl()))
                .timeout(Duration.ofMillis(timeout))
                .headers(getHeaders(dto.getHeaderVariables()))
                .header("Content-Type", dto.getBodyType())
                .POST(buildBodyPublisher(dto))
                .build();

        HttpClient httpClient = createHttpSecureClient(dto.getUrl());
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private CompletableFuture<HttpResponse<String>> asyncPut(RequestDTO dto, int timeout) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dto.getUrl()))
                .timeout(Duration.ofMillis(timeout))
                .headers(getHeaders(dto.getHeaderVariables()))
                .PUT(HttpRequest.BodyPublishers.ofString(dto.getRequestBody()))
                .build();
        HttpClient httpClient = createHttpSecureClient(dto.getUrl());
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private CompletableFuture<HttpResponse<String>> asyncPatch(RequestDTO dto, int timeout) throws Exception {
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(dto.getRequestBody());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dto.getUrl()))
                .timeout(Duration.ofMillis(timeout))
                .headers(getHeaders(dto.getHeaderVariables()))
                .method("PATCH", body)
                .build();
        HttpClient httpClient = createHttpSecureClient(dto.getUrl());
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private CompletableFuture<HttpResponse<String>> asyncDelete(RequestDTO dto, int timeout) throws Exception {
        HttpRequest.BodyPublisher body = (dto.getRequestBody() != null && !dto.getRequestBody().isEmpty())
                ? HttpRequest.BodyPublishers.ofString(dto.getRequestBody())
                : HttpRequest.BodyPublishers.noBody();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dto.getUrl()))
                .timeout(Duration.ofMillis(timeout))
                .headers(getHeaders(dto.getHeaderVariables()))
                .method("DELETE", body)
                .build();
        HttpClient httpClient = createHttpSecureClient(dto.getUrl());
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private CompletableFuture<HttpResponse<String>> asyncOptions(RequestDTO dto, int timeout) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dto.getUrl()))
                .timeout(Duration.ofMillis(timeout))
                .headers(getHeaders(dto.getHeaderVariables()))
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpClient httpClient = createHttpSecureClient(dto.getUrl());
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private String[] getHeaders(Map<String, String> headerVariables) {
        if (headerVariables == null || headerVariables.isEmpty()){
            return new String[0];
        }else{
            List<String> flat = new ArrayList<>();
            headerVariables.forEach((key, value) -> {
                flat.add(key);
                flat.add(value);
            });
            return flat.toArray(new String[0]);
        }

    }

    private HttpRequest.BodyPublisher buildBodyPublisher(RequestDTO dto) {
        String type = dto.getBodyType();
        String body = dto.getRequestBody();
        List<NameValuePair> params = dto.getParams();

        if (type == null || type.isBlank()) {
            return (body != null) ? HttpRequest.BodyPublishers.ofString(body) : HttpRequest.BodyPublishers.noBody();
        }

        switch (type) {
            case "application/json":
                return HttpRequest.BodyPublishers.ofString(body != null ? body : "");

            case "application/x-www-form-urlencoded":
                StringBuilder formBody = new StringBuilder();
                if (params != null && !params.isEmpty()) {
                    for (NameValuePair param : params) {
                        if (formBody.length() > 0) formBody.append("&");
                        formBody.append(URLEncoder.encode(param.getName(), StandardCharsets.UTF_8));
                        formBody.append("=");
                        formBody.append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8));
                    }
                }
                return HttpRequest.BodyPublishers.ofString(formBody.toString());

            case "multipart/form-data":
                throw new UnsupportedOperationException("multipart/form-data is not supported in Java 11 HttpClient directly.");

            default:
                return (body != null) ? HttpRequest.BodyPublishers.ofString(body) : HttpRequest.BodyPublishers.noBody();
        }
    }

}
