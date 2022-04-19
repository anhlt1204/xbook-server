package com.ados.xbook.controller.admin;

import com.ados.xbook.controller.BaseController;
import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.helper.StringHelper;
import com.ados.xbook.repository.SessionEntityRepo;
import com.ados.xbook.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/saleOrder")
public class AdminSaleOrderController extends BaseController {

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private SessionEntityRepo repo;

    @GetMapping
    public BaseResponse findAll(@RequestHeader Map<String, String> headers,
                                @RequestParam(value = "userId", required = false) Long userId,
                                @RequestParam(value = "key", required = false) String key,
                                @RequestParam(value = "value", required = false) String value,
                                @RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "size", required = false) Integer size) {

        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>findAll info: {}, userId: {}, key: {}, value: {}, page: {}, size: {}",
                info, userId, key, value, page, size);

        response = saleOrderService.findAllAdmin(info, userId, key, value, page, size);

        log.info("<=findAll info: {}, userId: {}, key: {}, value: {}, page: {}, size: {}, res: {}",
                info, userId, key, value, page, size, response);

        return response;

    }

    @PatchMapping("/submit/{saleOrderId}")
    public BaseResponse submit(@RequestHeader Map<String, String> headers,
                               @PathVariable(value = "saleOrderId") Long saleOrderId) {

        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>submit info: {}, saleOrderId: {}", info, saleOrderId);

        if (saleOrderId == null || saleOrderId <= 0) {
            throw new InvalidException("Params invalid");
        }

        response = saleOrderService.submitSaleOrder(info, saleOrderId);

        log.info("<=submit info: {}, saleOrderId: {}, res: {}", info, saleOrderId, response);

        return response;

    }

}
