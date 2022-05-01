package com.ados.xbook.controller.user;

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
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionEntityRepo repo;

    @GetMapping("/info")
    public BaseResponse getCurrentUser(@RequestHeader Map<String, String> headers) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>getCurrentUser info: {}", info);

        response = userService.getCurrentUser(info);

        log.info("<=getCurrentUser info: {}, res: {}", info, response);

        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse update(@RequestHeader Map<String, String> headers,
                               @PathVariable(name = "id") Long id,
                               @RequestBody UserRequest request) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>update info: {}, id:{}, req: {}", info, id, request);

        if (id == null || id <= 0 || request == null) {
            throw new InvalidException("Params invalid");
        } else {
//            request.validate(true);
            response = userService.update(id, request, info);
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
            response = userService.deleteById(info.getUsername(), info.getRole(), id);
        }

        log.info("<=delete info: {}, id: {}, res: {}", info, id, response);

        return response;
    }

}
