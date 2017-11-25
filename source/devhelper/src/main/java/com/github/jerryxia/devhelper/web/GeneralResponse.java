package com.github.jerryxia.devhelper.web;

import java.util.Map;

/**
 * @author Administrator
 *
 */
public class GeneralResponse {
    private int                 code;
    private String              msg;
    private Map<String, Object> data;

    public GeneralResponse(int c, String message, Map<String, Object> d) {
        this.code = c;
        this.msg = message;
        this.data = d;
    }

    public GeneralResponse fail() {
        this.code = GeneralResponseCodeEnum.Fail.getValue();
        return this;
    }

    public GeneralResponse msg(String message) {
        this.msg = message;
        return this;
    }

    public GeneralResponse failWithMsg(String withMessage) {
        return fail().msg(withMessage);
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
