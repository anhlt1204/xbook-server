package com.ados.xbook.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "tbl_session_entity")
public class SessionEntity {

    @Id
    private String id;

    @Nationalized
    private String fullName;

    private String username;

    @Nationalized
    private String address;

    private String role;

    private Long amount;

    private String email;

    private String phone;

    @CreationTimestamp
    private Date createAt;

    @UpdateTimestamp
    private Date updateAt;

    public SessionEntity(String id, String fullName, String username, String address, String role, Long amount, String email, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.address = address;
        this.role = role;
        this.amount = amount;
        this.email = email;
        this.phone = phone;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
