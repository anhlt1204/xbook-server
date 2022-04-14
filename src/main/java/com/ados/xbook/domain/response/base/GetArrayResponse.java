package com.ados.xbook.domain.response.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetArrayResponse<T> extends BaseResponse {

    private long total;
    private List<T> rows;

    @Override
    public String info() {
        return super.info() + "|total=" + total + "|rows.size()=" + (rows != null ? rows.size() : 0);
    }

}
