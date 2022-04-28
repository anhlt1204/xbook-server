package com.ados.xbook.service;

import com.ados.xbook.domain.entity.ProductImage;
import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.request.ProductImageRequest;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.domain.response.base.GetArrayResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {
    BaseResponse findAll(Long productId);

    BaseResponse findById(Long id);

    BaseResponse upload(MultipartFile file);

    GetArrayResponse<ProductImage> create(ProductImageRequest request, SessionEntity info);

    BaseResponse update(Long id, Long productId, MultipartFile image, String username);

    BaseResponse deleteById(String username, Long id);
}
