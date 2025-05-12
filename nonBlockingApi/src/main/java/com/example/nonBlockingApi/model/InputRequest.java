package com.example.nonBlockingApi.model;

import com.example.nonBlockingApi.DTO.RequestDTO;
import com.example.nonBlockingApi.Enums.ApiMethod;
import jakarta.validation.constraints.NotNull;


public class InputRequest {
    @NotNull(message="Api Method Can Not be  Not Null , Please Set The Api Method")
    private ApiMethod apiMethod;
    @NotNull(message ="RequestDTO Can Not be Not Null, Please Set The Request DTo ")
    private RequestDTO requestDTO;

    private int timeout;

    public ApiMethod getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(ApiMethod apiMethod) {
        this.apiMethod = apiMethod;
    }

    public RequestDTO getRequestDTO() {
        return requestDTO;
    }

    public void setRequestDTO(RequestDTO requestDTO) {
        this.requestDTO = requestDTO;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
