package com.ados.xbook.controller.admin;

import com.ados.xbook.controller.BaseController;
import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.request.UserRequest;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.helper.StringHelper;
import com.ados.xbook.repository.SessionEntityRepo;
import com.ados.xbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionEntityRepo repo;

    @GetMapping
    public BaseResponse findAll(@RequestHeader Map<String, String> headers,
                                @RequestParam(value = "key", required = false) String key,
                                @RequestParam(value = "value", required = false) String value,
                                @RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "size", required = false) Integer size) {

        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>findAll info: {}, key: {}, value: {}, page: {}, size: {}", info, key, value, page, size);

        response = userService.findAll(key, value, page, size);

        log.info("<=findAll info: {}, key: {}, value: {}, page: {}, size: {}, res: {}", info, key, value, page, size, response);

        return response;

    }

    @PostMapping
    public BaseResponse create(@RequestHeader Map<String, String> headers,
                               @RequestBody UserRequest request) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>create info: {}, req: {}", info, request);

        if (request == null) {
            throw new InvalidException("Params invalid");
        } else {
//            request.validate(false);
            response = userService.create(request, info);
        }

        log.info("<=create info: {}, req: {}, res: {}", info, request, response);

        return response;
    }

}
