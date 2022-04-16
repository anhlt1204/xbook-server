package com.ados.xbook.service.impl;

import com.ados.xbook.domain.entity.ERole;
import com.ados.xbook.domain.entity.User;
import com.ados.xbook.domain.request.UserRequest;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.domain.response.base.CreateResponse;
import com.ados.xbook.domain.response.base.GetArrayResponse;
import com.ados.xbook.domain.response.base.GetSingleResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.repository.UserRepo;
import com.ados.xbook.service.BaseService;
import com.ados.xbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public BaseResponse findAll() {

        GetArrayResponse<User> response = new GetArrayResponse<>();

        List<User> users = new ArrayList<>();

        users = userRepo.findAll();
        response.setTotal(users.size());
        response.setRows(users);
        response.setSuccess();

        return response;

    }

    @Override
    public BaseResponse findById(Long id) {

        GetSingleResponse<User> response = new GetSingleResponse<>();

        Optional<User> optional = userRepo.findById(id);
        User user;

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find user has id " + id);
        } else {
            user = optional.get();
            response.setItem(user);
            response.setSuccess();
        }

        return response;

    }

    @Override
    public BaseResponse findByUsername(String username) {

        GetSingleResponse<User> response = new GetSingleResponse<>();

        User user = userRepo.findFirstByUsername(username);

        if (user == null) {
            throw new InvalidException("Cannot find user has username " + username);
        } else {
            response.setItem(user);
            response.setSuccess();
        }

        return response;

    }

    @Override
    public BaseResponse create(UserRequest request) {

        CreateResponse<User> response = new CreateResponse<>();

        List<User> users = userRepo.findAll();
        List<String> usernames = users.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        if (usernames.contains(request.getUsername())) {
            throw new InvalidException("Username is already exist");
        }

        User user = request.create();
        user.setPassword(bcryptEncoder.encode(request.getPassword()));
        user.setCreateBy(request.getCreateBy());

        userRepo.save(user);
        response.setItem(user);
        response.setSuccess();

        return response;
    }

    @Override
    public BaseResponse update(Long id, UserRequest request) {

        GetSingleResponse<User> response = new GetSingleResponse<>();

        Optional<User> optional = userRepo.findById(id);

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find user has id " + id);
        } else {
            User user = optional.get();

            if (!(user.getUsername().equals(request.getCreateBy()) || request.getCallerRole().equals(ERole.ADMIN.toString()))) {
                throw new InvalidException("You don't have permission to update this user");
            }

            List<User> users = userRepo.findAll();
            List<String> usernames = users.stream()
                    .map(User::getUsername)
                    .collect(Collectors.toList());

            usernames.remove(user.getUsername());

            if (usernames.contains(request.getUsername())) {
                throw new InvalidException("Username is already exist");
            }

            user = request.update(user);
            user.setUpdateBy(request.getCreateBy());

            userRepo.save(user);
            response.setItem(user);
            response.setSuccess();
        }

        return response;

    }

    @Override
    public BaseResponse deleteById(String username, String role, Long id) {

        BaseResponse response = new BaseResponse();

        Optional<User> optional = userRepo.findById(id);

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find user has id " + id);
        } else {
            User user = optional.get();

            if (!(user.getUsername().equals(username) || role.equals(ERole.ADMIN.toString()))) {
                throw new InvalidException("You don't have permission to delete this user");
            }
            userRepo.deleteById(id);
            response.setSuccess();
        }

        return response;

    }
}
