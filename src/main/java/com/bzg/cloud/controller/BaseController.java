package com.bzg.cloud.controller;


import com.bzg.cloud.vo.BaseResponse;
import com.bzg.cloud.vo.ResultCode;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author syl
 */
public class BaseController {
    protected static final String DEFAULT_CHARTSET = "UTF-8";
    protected static final String DEFAULT_JSON_CONTENT_TYPE = "application/json;charset=UTF-8";
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    //NOTE  Thread SAFE ?
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }


    protected String getToken() {
        return request.getHeader("token");
    }

    /*
        构建响应信息
     */
    <T> BaseResponse<T> buildErrorResp(String message) {
        return new BaseResponse<T>(ResultCode.ERROR, message);
    }


    <T> BaseResponse<T> buildErrorResp(int status, String message) {
        return new BaseResponse<T>(status, message);
    }


    <T> BaseResponse<T> buildSuccessResp(String message) {
        return new BaseResponse<T>(ResultCode.SUCCESS, message);
    }


    <T> BaseResponse<T> buildSuccessResp(T data) {
        return new BaseResponse<T>(ResultCode.SUCCESS, data);
    }


    <T> BaseResponse<T> buildSuccessResp(int status, String message, T data) {
        return new BaseResponse<T>(ResultCode.SUCCESS, message, data);
    }


    <T> BaseResponse<T> buildSuccessResp(String message, T data) {
        return new BaseResponse<T>(ResultCode.SUCCESS, message, data);
    }


}