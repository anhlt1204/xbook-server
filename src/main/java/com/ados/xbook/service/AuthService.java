package com.ados.xbook.service;

import com.ados.xbook.domain.request.LoginRequest;
import com.ados.xbook.domain.request.RegisterRequest;
import com.ados.xbook.domain.response.base.BaseResponse;

public interface AuthService {
    BaseResponse login(LoginRequest request);

    BaseResponse register(RegisterRequest request);
}
