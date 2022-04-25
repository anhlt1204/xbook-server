package com.ados.xbook.service;

import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.request.ProductRequest;
import com.ados.xbook.domain.response.base.BaseResponse;

public interface ProductService {
    BaseResponse findAll(Long categoryId, String key, String value, Integer page, Integer size);

    BaseResponse findById(Long id);

    BaseResponse findBySlug(String slug);

    BaseResponse create(ProductRequest request, SessionEntity info);

    BaseResponse update(Long id, ProductRequest request, SessionEntity info);

    BaseResponse deleteById(String username, Long id);
}
