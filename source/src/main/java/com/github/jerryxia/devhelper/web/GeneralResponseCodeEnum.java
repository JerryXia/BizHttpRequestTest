package com.github.jerryxia.devhelper.web;

/**
 * @author Administrator
 *
 */
public enum GeneralResponseCodeEnum {

    /**
     * OK
     */
    OK(1),
    /**
     * Fail
     */
    Fail(0);

    private int value;

    GeneralResponseCodeEnum(int v) {
        this.value = v;
    }

    public int getValue() { 
        return value; 
    }
}
