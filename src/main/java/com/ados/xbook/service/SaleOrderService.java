package com.ados.xbook.service;

import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.request.AddToCardRequest;
import com.ados.xbook.domain.response.base.BaseResponse;

public interface SaleOrderService {

    BaseResponse findAll(SessionEntity info, Integer page, Integer size);

    BaseResponse addToCard(SessionEntity info, AddToCardRequest request);

    BaseResponse getCurrentCart(SessionEntity info);

}
