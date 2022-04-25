package com.ados.xbook.service;

import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.request.UserRequest;
import com.ados.xbook.domain.response.base.BaseResponse;

public interface UserService {
    BaseResponse findAll(String key, String value, Integer page, Integer size);

    BaseResponse findById(Long id);

    BaseResponse findByUsername(String username);

    BaseResponse getCurrentUser(SessionEntity info);

    BaseResponse create(UserRequest request, SessionEntity info);

    BaseResponse update(Long id, UserRequest request, SessionEntity info);

    BaseResponse deleteById(String username, String role, Long id);
}
