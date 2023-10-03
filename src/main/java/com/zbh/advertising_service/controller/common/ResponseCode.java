package com.zbh.advertising_service.controller.common;

public enum ResponseCode {

    SUCCESS("0", "成功"),
	FAIL("9999", "服务异常"),
    ;

    private final String code;
    private final String msg;

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }


    private ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    @Override
    public String toString() {
        return "ResponseCode(code=" + this.getCode() + ", msg=" + this.getMsg() + ")";
    }

}