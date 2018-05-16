package com.bzg.cloud.vo;

/**
 * @author: syl  Date: 2018/5/11 Email:nerosyl@live.com
 */
public class SystemStatusResponse  {
    private String message;
    private String ram;
    private String init;
    private String max;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}
