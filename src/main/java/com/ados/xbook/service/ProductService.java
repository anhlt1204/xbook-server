package com.ados.xbook.service;

import com.ados.xbook.domain.request.ProductRequest;
import com.ados.xbook.domain.response.base.BaseResponse;

public interface ProductService {
    BaseResponse findAll();

    BaseResponse create(ProductRequest request);

    BaseResponse update(Long id, ProductRequest request);

    BaseResponse deleteById(String username, Long id);
}
