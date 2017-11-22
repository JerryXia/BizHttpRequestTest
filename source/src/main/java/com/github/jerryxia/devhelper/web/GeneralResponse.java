package com.github.jerryxia.devhelper.web;

import java.util.Map;

/**
 * @author Administrator
 *
 */
public class GeneralResponse {
    private int                 code;
    private String              message;
    private Map<String, Object> data;

    public GeneralResponse(int c, String msg, Map<String, Object> d) {
        this.code = c;
        this.message = msg;
        this.data = d;
    }

    public GeneralResponse fail() {
        this.code = GeneralResponseCodeEnum.Fail.getValue();
        return this;
    }

    public GeneralResponse message(String msg) {
        this.message = msg;
        return this;
    }

    public GeneralResponse failWithMsg(String withMessage) {
        return fail().message(withMessage);
    }

    public GeneralResponse failWithSysError() {
        return failWithMsg(GeneralResponseMsg.SYS_ERROR);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
