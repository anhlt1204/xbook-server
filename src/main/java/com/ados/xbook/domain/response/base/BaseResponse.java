package com.ados.xbook.domain.response.base;

import lombok.Data;

@Data
public class BaseResponse {
    protected int rc;
    protected String rd;

    public void setSuccess() {
        this.rc = 0;
        this.rd = "OK";
    }

    public void setFailed(int rc, String rd) {
        this.rc = rc;
        this.rd = rd;
    }

    public String info() {
        return "rc=" + rc + "|rd=" + rd;
    }
}
