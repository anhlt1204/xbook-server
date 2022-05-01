package com.ados.xbook.service;

import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.request.AddToCardRequest;
import com.ados.xbook.domain.request.PaymentRequest;
import com.ados.xbook.domain.response.base.BaseResponse;

public interface SaleOrderService {

    BaseResponse findAll(SessionEntity info, Integer page, Integer size);

    BaseResponse findAllAdmin(SessionEntity info, Long userId, String key, String value, Integer page, Integer size);

    BaseResponse findById(SessionEntity info, Long saleOrderId);

    BaseResponse addToCard(SessionEntity info, AddToCardRequest request);

    BaseResponse removeFromCard(SessionEntity info, AddToCardRequest request);

    BaseResponse getCurrentCart(SessionEntity info);

    BaseResponse payment(SessionEntity info, PaymentRequest request);

    BaseResponse submitSaleOrder(SessionEntity info, Long saleOrderId);

}
