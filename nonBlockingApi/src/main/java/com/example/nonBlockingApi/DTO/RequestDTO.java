package com.example.nonBlockingApi.DTO;

import com.example.nonBlockingApi.Enums.ApiMethod;

import java.util.List;
import java.util.Map;

public class RequestDTO {
    private ApiMethod apiMethod;
    private String url;
    private Map<String, String> queryParams;
    private Map<String, String> headerVariables;
    private String bodyType;
    private String requestBody;
    private Map<String, String> pathMap;
    private Map<String, String> formParam;
    private Map<String, String> urlEncodedParam;
    private List<NameValuePair> params;


    public ApiMethod getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(ApiMethod apiMethod) {
        this.apiMethod = apiMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public Map<String, String> getHeaderVariables() {
        return headerVariables;
    }

    public void setHeaderVariables(Map<String, String> headerVariables) {
        this.headerVariables = headerVariables;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, String> getPathMap() {
        return pathMap;
    }

    public void setPathMap(Map<String, String> pathMap) {
        this.pathMap = pathMap;
    }

    public Map<String, String> getFormParam() {
        return formParam;
    }

    public void setFormParam(Map<String, String> formParam) {
        this.formParam = formParam;
    }

    public Map<String, String> getUrlEncodedParam() {
        return urlEncodedParam;
    }

    public void setUrlEncodedParam(Map<String, String> urlEncodedParam) {
        this.urlEncodedParam = urlEncodedParam;
    }

    public List<NameValuePair> getParams() {
        return params;
    }

    public void setParams(List<NameValuePair> params) {
        this.params = params;
    }
}
