package com.ados.xbook.controller.user;

import com.ados.xbook.controller.BaseController;
import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.request.AddToCardRequest;
import com.ados.xbook.domain.request.PaymentRequest;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.helper.StringHelper;
import com.ados.xbook.repository.SessionEntityRepo;
import com.ados.xbook.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/saleOrder")
public class SaleOrderController extends BaseController {

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private SessionEntityRepo repo;

    @GetMapping
    public BaseResponse findAll(@RequestHeader Map<String, String> headers,
                                @RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "size", required = false) Integer size) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>findAll info: {}", info);

        response = saleOrderService.findAll(info, page, size);

        log.info("<=findAll info: {}, res: {}", info, response);

        return response;
    }

    @PostMapping("/add")
    public BaseResponse addToCart(@RequestHeader Map<String, String> headers,
                                  @RequestBody AddToCardRequest request) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>addToCart info: {}, req: {}", info, request);

        if (request == null) {
            throw new InvalidException("Params invalid");
        } else {
            request.validate();
            response = saleOrderService.addToCard(info, request);
        }

        log.info("<=addToCart info: {}, req: {}, res: {}", info, request, response);

        return response;
    }

    @PostMapping("/remove")
    public BaseResponse removeFromCart(@RequestHeader Map<String, String> headers,
                                       @RequestBody AddToCardRequest request) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>removeFromCart info: {}, req: {}", info, request);

        if (request == null) {
            throw new InvalidException("Params invalid");
        } else {
            request.validate();
            response = saleOrderService.removeFromCard(info, request);
        }

        log.info("<=removeFromCart info: {}, req: {}, res: {}", info, request, response);

        return response;
    }

    @GetMapping("/current")
    public BaseResponse getCurrentCart(@RequestHeader Map<String, String> headers) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>getCurrentCart info: {}", info);

        response = saleOrderService.getCurrentCart(info);

        log.info("<=getCurrentCart info: {}, res: {}", info, response);

        return response;
    }

    @PostMapping("/payment")
    public BaseResponse payment(@RequestHeader Map<String, String> headers,
                                @RequestBody PaymentRequest request) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>payment info: {}, req: {}", info, request);

//        request.validate();
        response = saleOrderService.payment(info, request);

        log.info("<=payment info: {}, req :{}, res: {}", info, request, response);

        return response;
    }
}
