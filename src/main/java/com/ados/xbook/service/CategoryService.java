package com.ados.xbook.service;

import com.ados.xbook.domain.request.CategoryRequest;
import com.ados.xbook.domain.response.base.BaseResponse;

public interface CategoryService {
    BaseResponse findAll();

    BaseResponse findById(Long id);

    BaseResponse findBySlug(String slug);

    BaseResponse create(CategoryRequest request);

    BaseResponse update(Long id, CategoryRequest request);

    BaseResponse deleteById(String username, Long id);
}
