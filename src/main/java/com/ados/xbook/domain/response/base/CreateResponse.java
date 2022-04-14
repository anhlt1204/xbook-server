package com.ados.xbook.domain.response.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateResponse<T> extends BaseResponse {
    protected T item;
}
