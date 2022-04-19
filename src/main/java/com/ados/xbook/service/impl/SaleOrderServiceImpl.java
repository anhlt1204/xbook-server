package com.ados.xbook.service.impl;

import com.ados.xbook.domain.entity.*;
import com.ados.xbook.domain.request.AddToCardRequest;
import com.ados.xbook.domain.response.SaleOrderResponse;
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

import java.util.ArrayList;
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

        List<SaleOrderResponse> saleOrderResponses = new ArrayList<>();

        for (SaleOrder s : saleOrders) {
            saleOrderResponses.add(new SaleOrderResponse(s));
        }

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

        SaleOrder saleOrder = saleOrderRepo.findFirstByStatusAndCreateBy(0, info.getUsername());

        if (saleOrder == null) {
            saleOrder = new SaleOrder();
            saleOrder.setPhone(info.getPhone());
            saleOrder.setCreateBy(info.getUsername());
            saleOrder.setCustomerAddress(info.getAddress());
            saleOrder.setUser(userRepo.findFirstByUsername(info.getUsername()));
            saleOrder.setDelivery(deliveryRepo.findFirstByIndex(EDelivery.MUA_HANG.toString()));

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

        int flag = 0;
        OrderItem item;

        for (OrderItem o : saleOrder.getOrderItems()) {
            if (o.getProduct() == product) {
                Integer quantity = o.getQuantity() + request.getQuantity();
                if (quantity < 0) {
                    throw new InvalidException("Invalid quantity");
                }
                if (quantity == 0) {
                    saleOrder.getOrderItems().remove(o);
                    orderItemRepo.delete(o);
                    flag = 1;
                    break;
                } else {
                    o.setQuantity(quantity);
                    orderItemRepo.save(o);
                    flag = 1;
                    break;
                }
            }
        }

        if (flag == 0) {
            item = new OrderItem();
            item.setSaleOrder(saleOrder);
            item.setProduct(product);
            item.setQuantity(0);
            Integer quantity = item.getQuantity() + request.getQuantity();
            if (quantity < 0) {
                throw new InvalidException("Invalid quantity");
            }
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
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse removeFromCard(SessionEntity info, AddToCardRequest request) {
        request.setQuantity(-1 * request.getQuantity());
        return addToCard(info, request);
    }

    @Override
    public BaseResponse getCurrentCart(SessionEntity info) {
        GetSingleResponse<SaleOrderResponse> response = new GetSingleResponse<>();

        SaleOrder saleOrder = saleOrderRepo.findFirstByStatusAndCreateBy(0, info.getUsername());

        if (saleOrder == null) {
            saleOrder = new SaleOrder();
            saleOrder.setPhone(info.getPhone());
            saleOrder.setCreateBy(info.getUsername());
            saleOrder.setCustomerAddress(info.getAddress());
            saleOrder.setUser(userRepo.findFirstByUsername(info.getUsername()));
            saleOrder.setDelivery(deliveryRepo.findFirstByIndex(EDelivery.MUA_HANG.toString()));

            saleOrderRepo.save(saleOrder);
        }

        response.setItem(new SaleOrderResponse(saleOrder));
        response.setSuccess();

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse payment(SessionEntity info) {
        GetSingleResponse<SaleOrderResponse> response = new GetSingleResponse<>();

        SaleOrder saleOrder = saleOrderRepo.findFirstByStatusAndCreateBy(0, info.getUsername());

        if (saleOrder == null) {
            throw new InvalidException("Cart is empty");
        }

        Double total = total(saleOrder.getOrderItems());

        if (total <= 0) {
            throw new InvalidException("Cart is empty");
        }

        User user = saleOrder.getUser();

        if (user.getAmount() < total) {
            throw new InvalidException("Your money do not enough to purchase");
        }

        user.setAmount(user.getAmount() - total);
        userRepo.save(user);

        for (OrderItem o : saleOrder.getOrderItems()) {
            Product product = o.getProduct();
            product.setCurrentNumber(product.getCurrentNumber() - o.getQuantity());
            productRepo.save(product);
            o.setStatus(1);
            orderItemRepo.save(o);
        }

        saleOrder.setUpdateBy(info.getUsername());
        saleOrder.setDelivery(deliveryRepo.findFirstByIndex(EDelivery.CHO_XAC_NHAN.toString()));
        saleOrder.setStatus(1);

        saleOrderRepo.save(saleOrder);

        response.setItem(new SaleOrderResponse(saleOrder));
        response.setSuccess();

        return response;
    }

    public Double total(List<OrderItem> items) {
        Double sum = 0D;
        for (OrderItem o : items) {
            sum += o.getQuantity() * o.getProduct().getPrice();
        }
        return sum;
    }
}
