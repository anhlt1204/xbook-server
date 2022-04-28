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
import com.ados.xbook.helper.PagingInfo;
import com.ados.xbook.repository.UserRepo;
import com.ados.xbook.service.BaseService;
import com.ados.xbook.service.UserService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public BaseResponse findAll(String key, String value, Integer page, Integer size) {

        GetArrayResponse<UserResponse> response = new GetArrayResponse<>();
        PagingInfo pagingInfo = PagingInfo.parse(page, size);
        Long total = 0L;
        Pageable paging;
        Page<User> p;
        List<User> users = new ArrayList<>();

        if (Strings.isNullOrEmpty(key) && Strings.isNullOrEmpty(value)) {
            paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
            p = userRepo.findAll(paging);
            users = p.getContent();
            total = p.getTotalElements();
        }

        if (!Strings.isNullOrEmpty(key)) {
            switch (key.trim().toUpperCase()) {
                case "USERNAME":
                    paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                    p = userRepo.findAllByUsernameLike("%" + value + "%", paging);
                    users = p.getContent();
                    total = p.getTotalElements();
                    break;
                case "FILTER":
                    switch (value.trim().toUpperCase()) {
                        case "ZA":
                            paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("username").descending());
                            p = userRepo.findAll(paging);
                            users = p.getContent();
                            total = p.getTotalElements();
                            break;
                        case "AZ":
                            paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("username"));
                            p = userRepo.findAll(paging);
                            users = p.getContent();
                            total = p.getTotalElements();
                            break;
                        case "OLD":
                            paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt"));
                            p = userRepo.findAll(paging);
                            users = p.getContent();
                            total = p.getTotalElements();
                            break;
                        default:
                            paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                            p = userRepo.findAll(paging);
                            users = p.getContent();
                            total = p.getTotalElements();
                            break;
                    }
                    break;
                default:
                    paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                    p = userRepo.findAll(paging);
                    users = p.getContent();
                    total = p.getTotalElements();
                    break;
            }
        }

        List<UserResponse> userResponses = new ArrayList<>();

        for (User u : users) {
            userResponses.add(new UserResponse(u));
        }

        response.setTotalItem(total);
        response.setCurrentPage(pagingInfo.getPage() + 1);
        response.setTotalPage(total % pagingInfo.getSize() == 0 ?
                total / pagingInfo.getSize() : total / pagingInfo.getSize() + 1);
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
    public BaseResponse create(UserRequest request, SessionEntity info) {

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
        user.setCreateBy(info.getUsername());

        userRepo.save(user);
        response.setItem(new UserResponse(user));
        response.setSuccess();

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse update(Long id, UserRequest request, SessionEntity info) {

        GetSingleResponse<UserResponse> response = new GetSingleResponse<>();

        Optional<User> optional = userRepo.findById(id);

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find user has id " + id);
        } else {
            User user = optional.get();

            if (!(user.getUsername().equals(info.getUsername()) || info.getRole().equals(ERole.ADMIN.toString()))) {
                throw new InvalidException("You don't have permission to update this user");
            }

            List<User> users = userRepo.findAll();

            List<String> emails = users.stream()
                    .map(User::getEmail)
                    .collect(Collectors.toList());
            emails.remove(user.getEmail());

            List<String> phones = users.stream()
                    .map(User::getPhone)
                    .collect(Collectors.toList());
            phones.remove(user.getPhone());

            if (emails.contains(request.getEmail())) {
                throw new InvalidException("Email is already exist");
            }

            if (phones.contains(request.getPhone())) {
                throw new InvalidException("Phone number is already exist");
            }

            user = request.update(user);
            user.setUpdateBy(info.getUsername());

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
