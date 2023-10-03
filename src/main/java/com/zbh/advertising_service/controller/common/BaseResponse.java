package com.zbh.advertising_service.controller.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -8844494281780273538L;

    private String code;
    private String msg;
    private T      data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseResponse(){
        this.code = ResponseCode.SUCCESS.getCode();
        this.msg = ResponseCode.SUCCESS.getMsg();
        this.data = null;
    }


    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> resp = new BaseResponse<T>();
        resp.setCode(ResponseCode.SUCCESS.getCode());
        resp.setMsg(ResponseCode.SUCCESS.getMsg());
        resp.setData(data);
        return resp;
    }

    public static <T> BaseResponse<T> success() {
        BaseResponse<T> resp = new BaseResponse<T>();
        resp.setCode(ResponseCode.SUCCESS.getCode());
        resp.setMsg(ResponseCode.SUCCESS.getMsg());
        return resp;
    }


    public static BaseResponse fail(String msg) {
        BaseResponse resp = new BaseResponse();
        resp.setCode(ResponseCode.FAIL.getCode());
        resp.setMsg(msg);
        return resp;
    }

    public static <T> BaseResponse<T> fail() {
        BaseResponse<T> resp = new BaseResponse<T>();
        resp.setCode(ResponseCode.SUCCESS.getCode());
        resp.setMsg(ResponseCode.SUCCESS.getMsg());
        return resp;
    }

}
