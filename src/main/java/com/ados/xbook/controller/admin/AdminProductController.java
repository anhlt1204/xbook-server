package com.ados.xbook.controller.admin;

import com.ados.xbook.controller.BaseController;
import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.request.ProductRequest;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.helper.StringHelper;
import com.ados.xbook.repository.SessionEntityRepo;
import com.ados.xbook.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SessionEntityRepo repo;

    @PostMapping
    public BaseResponse create(@RequestHeader Map<String, String> headers,
                               @RequestBody ProductRequest request) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>create info: {}, req: {}", info, request);

        if (request == null) {
            throw new InvalidException("Params invalid");
        } else {
//            request.validate();
            response = productService.create(request, info);
        }

        log.info("<=create info: {}, req: {}, res: {}", info, request, response);

        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse update(@RequestHeader Map<String, String> headers,
                               @PathVariable(name = "id") Long id,
                               @RequestBody ProductRequest request) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>update info: {}, id:{}, req: {}", info, id, request);

        if (id == null || id <= 0 || request == null) {
            throw new InvalidException("Params invalid");
        } else {
//            request.validate();
            response = productService.update(id, request, info);
        }

        log.info("<=update info: {}, id:{}, req: {}, res: {}", info, id, request, response);

        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse delete(@RequestHeader Map<String, String> headers,
                               @PathVariable(name = "id") Long id) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>delete info: {}, id: {}", info, id);

        if (id == null || id <= 0) {
            throw new InvalidException("Params invalid");
        } else {
            response = productService.deleteById(info.getUsername(), id);
        }

        log.info("<=delete info: {}, id: {}, res: {}", info, id, response);

        return response;
    }

}
