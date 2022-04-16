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
    public BaseResponse findAll(@RequestHeader Map<String, String> headers) {

        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>findAll info: {}", info);

        response = userService.findAll();

        log.info("<=findAll info: {}, res: {}", info, response);

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
            request.validate(false);
            request.setCreateBy(info.getUsername());
            response = userService.create(request);
        }

        log.info("<=create info: {}, req: {}, res: {}", info, request, response);

        return response;
    }

}
