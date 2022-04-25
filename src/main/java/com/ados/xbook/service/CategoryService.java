package com.ados.xbook.service;

import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.request.CategoryRequest;
import com.ados.xbook.domain.response.base.BaseResponse;

public interface CategoryService {
    BaseResponse findAll(String key, String value, Integer page, Integer size);

    BaseResponse findById(Long id);

    BaseResponse findBySlug(String slug);

    BaseResponse create(CategoryRequest request, SessionEntity info);

    BaseResponse update(Long id, CategoryRequest request, SessionEntity info);

    BaseResponse deleteById(String username, Long id);
}
