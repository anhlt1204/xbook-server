package com.ados.xbook.service;

import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.request.UserRequest;
import com.ados.xbook.domain.response.base.BaseResponse;

public interface UserService {
    BaseResponse findAll();

    BaseResponse findById(Long id);

    BaseResponse findByUsername(String username);

    BaseResponse getCurrentUser(SessionEntity info);

    BaseResponse create(UserRequest request);

    BaseResponse update(Long id, UserRequest request);

    BaseResponse deleteById(String username, String role, Long id);
}
