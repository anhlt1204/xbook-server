package com.ados.xbook.service.impl;

import com.ados.xbook.domain.entity.*;
import com.ados.xbook.domain.request.AddToCardRequest;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.domain.response.base.GetArrayResponse;
import com.ados.xbook.domain.response.base.GetSingleResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.helper.PagingInfo;
import com.ados.xbook.repository.*;
import com.ados.xbook.service.BaseService;
import com.ados.xbook.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SaleOrderServiceImpl extends BaseService implements SaleOrderService {

    @Autowired
    private SaleOrderRepo saleOrderRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private DeliveryRepo deliveryRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Override
    public BaseResponse findAll(SessionEntity info, Integer page, Integer size) {

        GetArrayResponse<SaleOrder> response = new GetArrayResponse<>();
        PagingInfo pagingInfo = PagingInfo.parse(page, size);
        Pageable paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize());
        Page<SaleOrder> p = saleOrderRepo.findAllByCreateByOrderByCreateAtDesc(info.getUsername(), paging);
        List<SaleOrder> saleOrders = p.getContent();

        response.setTotalPage(p.getTotalPages());
        response.setTotalItem(p.getTotalElements());
        response.setCurrentPage(pagingInfo.getPage() + 1);
        response.setData(saleOrders);
        response.setSuccess();

        return response;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse addToCard(SessionEntity info, AddToCardRequest request) {
        GetSingleResponse<SaleOrder> response = new GetSingleResponse<>();

        SaleOrder saleOrder = saleOrderRepo.findFirstByStatus(0);

        if (saleOrder == null) {
            saleOrder = new SaleOrder();
            saleOrder.setDelivery(deliveryRepo.findFirstByIndex(EDelivery.MUA_HANG.toString()));
            saleOrder.setUser(userRepo.findFirstByUsername(info.getUsername()));
            saleOrder.setCreateBy(info.getUsername());

            saleOrderRepo.save(saleOrder);
        }

        User user = saleOrder.getUser();

        Optional<Product> optional = productRepo.findById(request.getProductId());

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find product has id " + request.getProductId());
        }

        Product product = optional.get();

        if (product.getCurrentNumber() < request.getQuantity()) {
            throw new InvalidException("Your order quantity is greater than the quantity that is currently product");
        }

        if (request.getQuantity() * product.getPrice() > user.getAmount()) {
            throw new InvalidException("Your money do not enough to purchase");
        }

        user.setAmount(user.getAmount() - request.getQuantity() * product.getPrice());
        product.setCurrentNumber(product.getCurrentNumber() - request.getQuantity());
        userRepo.save(user);
        productRepo.save(product);

        int flag = 0;
        OrderItem item;

        for (OrderItem o : saleOrder.getOrderItems()) {
            if (o.getProduct() == product) {
                o.setQuantity(o.getQuantity() + request.getQuantity());
                orderItemRepo.save(o);
                flag = 1;
                break;
            }
        }

        if (flag == 0) {
            item = new OrderItem();
            item.setSaleOrder(saleOrder);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            item.setCreateBy(user.getUsername());
            orderItemRepo.save(item);
            saleOrder.getOrderItems().add(item);
        }

        response.setItem(saleOrder);
        response.setSuccess();

        return response;
    }

    @Override
    public BaseResponse getCurrentCart(SessionEntity info) {
        GetSingleResponse<SaleOrder> response = new GetSingleResponse<>();

        SaleOrder saleOrder = saleOrderRepo.findFirstByStatus(0);

        if (saleOrder == null) {
            saleOrder = new SaleOrder();
            saleOrder.setDelivery(deliveryRepo.findFirstByIndex(EDelivery.MUA_HANG.toString()));
            saleOrder.setUser(userRepo.findFirstByUsername(info.getUsername()));
            saleOrder.setCreateBy(info.getUsername());

            saleOrderRepo.save(saleOrder);
        }

        response.setItem(saleOrder);
        response.setSuccess();

        return response;
    }
}
