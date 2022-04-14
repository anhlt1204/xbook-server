package com.ados.xbook.domain.response;

import com.ados.xbook.domain.response.base.BaseResponse;
import lombok.Data;

@Data
public class RegisterResponse extends BaseResponse {
    private String username;
}
