package com.ados.xbook.domain.response;

import com.ados.xbook.domain.response.base.BaseResponse;
import lombok.Data;

@Data
public class LoginResponse extends BaseResponse {
    private String username;
    private String role;
    private String token;
}
