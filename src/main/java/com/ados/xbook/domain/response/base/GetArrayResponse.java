package com.ados.xbook.domain.response.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetArrayResponse<T> extends BaseResponse {

    @JsonProperty("current_page")
    private long currentPage;

    @JsonProperty("total_page")
    private long totalPage;

    @JsonProperty("total_item")
    private long totalItem;

    private String key;
    private String value;
    private List<T> data;

    @Override
    public String info() {
        return super.info() + "|total=" + totalItem + "|data.size()=" + (data != null ? data.size() : 0);
    }

}
