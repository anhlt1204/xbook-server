package com.ados.xbook.service.impl;

import com.ados.xbook.domain.entity.ERole;
import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.entity.User;
import com.ados.xbook.domain.request.UserRequest;
import com.ados.xbook.domain.response.UserResponse;
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
import org.springframework.transaction.annotation.Transactional;

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

        GetArrayResponse<UserResponse> response = new GetArrayResponse<>();

        List<User> users = userRepo.findAll();
        List<UserResponse> userResponses = new ArrayList<>();

        for (User u : users) {
            userResponses.add(new UserResponse(u));
        }

        response.setTotalItem(users.size());
        response.setData(userResponses);
        response.setSuccess();

        return response;

    }

    @Override
    public BaseResponse findById(Long id) {

        GetSingleResponse<UserResponse> response = new GetSingleResponse<>();

        Optional<User> optional = userRepo.findById(id);
        User user;

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find user has id " + id);
        } else {
            user = optional.get();
            response.setItem(new UserResponse(user));
            response.setSuccess();
        }

        return response;

    }

    @Override
    public BaseResponse findByUsername(String username) {

        GetSingleResponse<UserResponse> response = new GetSingleResponse<>();

        User user = userRepo.findFirstByUsername(username);

        if (user == null) {
            throw new InvalidException("Cannot find user has username " + username);
        } else {
            response.setItem(new UserResponse(user));
            response.setSuccess();
        }

        return response;

    }

    @Override
    public BaseResponse getCurrentUser(SessionEntity info) {
        return findByUsername(info.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse create(UserRequest request) {

        CreateResponse<UserResponse> response = new CreateResponse<>();

        List<User> users = userRepo.findAll();
        List<String> usernames = users.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        List<String> emails = users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());

        List<String> phones = users.stream()
                .map(User::getPhone)
                .collect(Collectors.toList());

        if (usernames.contains(request.getUsername())) {
            throw new InvalidException("Username is already exist");
        }

        if (emails.contains(request.getEmail())) {
            throw new InvalidException("Email is already exist");
        }

        if (phones.contains(request.getPhone())) {
            throw new InvalidException("Phone number is already exist");
        }

        User user = request.create();
        user.setPassword(bcryptEncoder.encode(request.getPassword()));
        user.setCreateBy(request.getCreateBy());

        userRepo.save(user);
        response.setItem(new UserResponse(user));
        response.setSuccess();

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse update(Long id, UserRequest request) {

        GetSingleResponse<UserResponse> response = new GetSingleResponse<>();

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

            List<String> emails = users.stream()
                    .map(User::getEmail)
                    .collect(Collectors.toList());
            emails.remove(user.getEmail());

            List<String> phones = users.stream()
                    .map(User::getPhone)
                    .collect(Collectors.toList());
            phones.remove(user.getPhone());

            if (usernames.contains(request.getUsername())) {
                throw new InvalidException("Username is already exist");
            }

            if (emails.contains(request.getEmail())) {
                throw new InvalidException("Email is already exist");
            }

            if (phones.contains(request.getPhone())) {
                throw new InvalidException("Phone number is already exist");
            }

            user = request.update(user);
            user.setUpdateBy(request.getCreateBy());

            userRepo.save(user);
            response.setItem(new UserResponse(user));
            response.setSuccess();
        }

        return response;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
