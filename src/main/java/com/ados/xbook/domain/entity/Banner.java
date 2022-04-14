package com.ados.xbook.domain.entity;

import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tbl_banner")
public class Banner extends BaseEntity{

    @Nationalized
    private String title;

    private String imageUrl;

    private Integer type;

}
