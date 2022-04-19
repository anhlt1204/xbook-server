package com.ados.xbook.domain.response;

import com.ados.xbook.domain.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class UserResponse {
    private Long id;
    private Integer status;
    private Date createAt;
    private String createBy;
    private Date updateAt;
    private String updateBy;
    private String firstName;
    private String lastName;
    private String username;
    private String address;
    private Double amount;
    private String email;
    private String phone;
    private String role;

    public UserResponse(User user) {
        this.id = user.getId();
        this.status = user.getStatus();
        this.createAt = user.getCreateAt();
        this.createBy = user.getCreateBy();
        this.updateAt = user.getUpdateAt();
        this.updateBy = user.getUpdateBy();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.address = user.getAddress();
        this.amount = user.getAmount();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.role = user.getRole();
    }
}
