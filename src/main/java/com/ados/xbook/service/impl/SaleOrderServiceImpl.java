package com.ados.xbook.service.impl;

import com.ados.xbook.domain.entity.*;
import com.ados.xbook.domain.request.AddToCardRequest;
import com.ados.xbook.domain.request.PaymentRequest;
import com.ados.xbook.domain.response.SaleOrderResponse;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.domain.response.base.GetArrayResponse;
import com.ados.xbook.domain.response.base.GetSingleResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.helper.PagingInfo;
import com.ados.xbook.repository.*;
import com.ados.xbook.service.BaseService;
import com.ados.xbook.service.SaleOrderService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse findAll(SessionEntity info, Integer page, Integer size) {

        GetArrayResponse<SaleOrder> response = new GetArrayResponse<>();
        updateStatusSaleOrder();
        PagingInfo pagingInfo = PagingInfo.parse(page, size);
        Pageable paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize());
        Page<SaleOrder> p = saleOrderRepo.findAllByCreateByAndStatusNotOrderByCreateAtDesc(info.getUsername(), -1, paging);
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
    public BaseResponse findAllAdmin(SessionEntity info, Long userId, String key, String value, Integer page, Integer size) {

        GetArrayResponse<SaleOrder> response = new GetArrayResponse<>();
        updateStatusSaleOrder();
        PagingInfo pagingInfo = PagingInfo.parse(page, size);
        Long total = 0L;
        Pageable paging;
        Page<SaleOrder> p;
        List<SaleOrder> saleOrders = new ArrayList<>();

        if (Strings.isNullOrEmpty(key) && Strings.isNullOrEmpty(value) && (userId == null || userId <= 0)) {
            paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
            p = saleOrderRepo.findAllByStatusNot(-1, paging);
            saleOrders = p.getContent();
            total = p.getTotalElements();
        }

        if (userId == null || userId <= 0) {
            if (!Strings.isNullOrEmpty(key)) {
                switch (key.trim().toUpperCase()) {
                    case "STATUS":
                        paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                        p = saleOrderRepo.findAllByStatus(tryParse(value), paging);
                        saleOrders = p.getContent();
                        total = p.getTotalElements();
                        break;
                    case "FILTER":
                        switch (value.trim().toUpperCase()) {
                            case "OLD":
                                paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt"));
                                p = saleOrderRepo.findAllByStatusNot(-1, paging);
                                saleOrders = p.getContent();
                                total = p.getTotalElements();
                                break;
                            default:
                                paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                                p = saleOrderRepo.findAllByStatusNot(-1, paging);
                                saleOrders = p.getContent();
                                total = p.getTotalElements();
                                break;
                        }
                        break;
                    default:
                        paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                        p = saleOrderRepo.findAllByStatusNot(-1, paging);
                        saleOrders = p.getContent();
                        total = p.getTotalElements();
                        break;
                }
            }
        } else {
            Optional<User> optional = userRepo.findById(userId);
            if (!optional.isPresent()) {
                throw new InvalidException("Cannot find user has id " + userId);
            }

            User user = optional.get();

            paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
            p = saleOrderRepo.findAllByUserAndStatusNot(user, -1, paging);
            saleOrders = p.getContent();
            total = p.getTotalElements();

            if (!Strings.isNullOrEmpty(key)) {
                switch (key.trim().toUpperCase()) {
                    case "STATUS":
                        paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                        p = saleOrderRepo.findAllByUserAndStatus(user, tryParse(value), paging);
                        saleOrders = p.getContent();
                        total = p.getTotalElements();
                        break;
                    case "FILTER":
                        switch (value.trim().toUpperCase()) {
                            case "OLD":
                                paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt"));
                                p = saleOrderRepo.findAllByUserAndStatusNot(user, -1, paging);
                                saleOrders = p.getContent();
                                total = p.getTotalElements();
                                break;
                            default:
                                paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                                p = saleOrderRepo.findAllByUserAndStatusNot(user, -1, paging);
                                saleOrders = p.getContent();
                                total = p.getTotalElements();
                                break;
                        }
                        break;
                    default:
                        paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                        p = saleOrderRepo.findAllByUserAndStatusNot(user, -1, paging);
                        saleOrders = p.getContent();
                        total = p.getTotalElements();
                        break;
                }
            }
        }

        response.setTotalItem(total);
        response.setCurrentPage(pagingInfo.getPage() + 1);
        response.setTotalPage(total % pagingInfo.getSize() == 0 ? total / pagingInfo.getSize() : total / pagingInfo.getSize() + 1);
        response.setData(saleOrders);
        response.setSuccess();

        return response;
    }

    @Override
    public BaseResponse findById(SessionEntity info, Long saleOrderId) {
        GetSingleResponse<SaleOrder> response = new GetSingleResponse<>();

        Optional<SaleOrder> optional = saleOrderRepo.findById(saleOrderId);

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find sale order has id " + saleOrderId);
        }

        SaleOrder saleOrder = optional.get();

        response.setSuccess();
        response.setItem(saleOrder);

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
            saleOrder.setTotal(0D);
            saleOrder.setTotalPrice(0D);
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
                if (quantity > product.getCurrentNumber()) {
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

        Double total = total(saleOrder.getOrderItems());
        Double totalItem = totalItem(saleOrder.getOrderItems());

        saleOrder.setTotal(totalItem);
        saleOrder.setTotalPrice(total);

        saleOrder = saleOrderRepo.findById(saleOrder.getId()).orElse(null);

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
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse getCurrentCart(SessionEntity info) {
        GetSingleResponse<SaleOrder> response = new GetSingleResponse<>();

        SaleOrder saleOrder = saleOrderRepo.findFirstByStatusAndCreateBy(0, info.getUsername());

        if (saleOrder == null) {
            saleOrder = new SaleOrder();
            saleOrder.setPhone(info.getPhone());
            saleOrder.setCreateBy(info.getUsername());
            saleOrder.setCustomerAddress(info.getAddress());
            saleOrder.setTotal(0D);
            saleOrder.setTotalPrice(0D);
            saleOrder.setUser(userRepo.findFirstByUsername(info.getUsername()));
            saleOrder.setDelivery(deliveryRepo.findFirstByIndex(EDelivery.MUA_HANG.toString()));

            saleOrderRepo.save(saleOrder);
        }

        response.setItem(saleOrder);
        response.setSuccess();

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse payment(SessionEntity info, PaymentRequest request) {
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

//        if (user.getAmount() < total) {
//            throw new InvalidException("Your money do not enough to purchase");
//        }

        user.setAmount(user.getAmount() - total);
        userRepo.save(user);

        for (OrderItem o : saleOrder.getOrderItems()) {
            Product product = o.getProduct();
            product.setCurrentNumber(product.getCurrentNumber() - o.getQuantity());
            product.setQuantitySelled(product.getQuantitySelled() + o.getQuantity());
            productRepo.save(product);
            o.setStatus(1);
            orderItemRepo.save(o);
        }

        saleOrder.setCustomerAddress(request.getAddress());
        saleOrder.setPhone(request.getPhone());
        saleOrder.setName(request.getLastName() + " " + request.getFirstName());
        saleOrder.setUpdateBy(info.getUsername());
        saleOrder.setDelivery(deliveryRepo.findFirstByIndex(EDelivery.CHO_XAC_NHAN.toString()));
        saleOrder.setStatus(1);

        saleOrderRepo.save(saleOrder);

        response.setItem(new SaleOrderResponse(saleOrder));
        response.setSuccess();

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse submitSaleOrder(SessionEntity info, Long saleOrderId) {
        GetSingleResponse<SaleOrderResponse> response = new GetSingleResponse<>();

        Optional<SaleOrder> optional = saleOrderRepo.findById(saleOrderId);

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find sale order has id " + saleOrderId);
        } else {
            SaleOrder saleOrder = optional.get();

            if (saleOrder.getStatus() < 1) {
                throw new InvalidException("Sale order is not wait submit");
            }

            switch (saleOrder.getStatus()) {
                case 1:
                    saleOrder.setStatus(2);
                    saleOrder.setUpdateBy(info.getUsername());
                    saleOrder.setDelivery(deliveryRepo.findFirstByIndex(EDelivery.DANG_GIAO_HANG.toString()));
                    break;
                case 2:
                    saleOrder.setStatus(3);
                    saleOrder.setUpdateBy(info.getUsername());
                    saleOrder.setDelivery(deliveryRepo.findFirstByIndex(EDelivery.GIAO_HANG_THANH_CONG.toString()));
                    break;
                default:
                    break;
            }

            saleOrderRepo.save(saleOrder);
            response.setItem(new SaleOrderResponse(saleOrder));
            response.setSuccess();
        }

        return response;
    }

    private Double total(List<OrderItem> items) {
        Double sum = 0D;
        for (OrderItem o : items) {
            sum += o.getQuantity() * o.getProduct().getPrice();
        }
        return sum;
    }

    private Double totalItem(List<OrderItem> items) {
        Double sum = 0D;
        for (OrderItem o : items) {
            sum += o.getQuantity();
        }
        return sum;
    }

    private Integer tryParse(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    void updateStatusSaleOrder() {
        List<SaleOrder> saleOrders = saleOrderRepo.findAll();
        for (SaleOrder s : saleOrders) {
            if (s.getOrderItems().size() == 0) {
                s.setStatus(-1);
            }
        }
    }
}
