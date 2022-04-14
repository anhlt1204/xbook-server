package com.ados.xbook.domain.response.base;

import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetSingleResponse<T> extends BaseResponse {

    private T item;

    @Override
    public String info() {
        return super.info() + "|item=" + (item != null ? new Gson().toJson(item) : "null");
    }

}

