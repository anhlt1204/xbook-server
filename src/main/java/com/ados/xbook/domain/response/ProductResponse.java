package com.ados.xbook.domain.response;

import com.ados.xbook.domain.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private Integer status;
    private Date createAt;
    private String createBy;
    private Date updateAt;
    private String updateBy;
    private String title;
    private String shortDescription;
    private String longDescription;
    private Long categoryId;
    private Double price;
    private String author;
    private Integer currentNumber;
    private Integer numberOfPage;
    private String slug;
    private Integer quantitySelled;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.status = product.getStatus();
        this.createAt = product.getCreateAt();
        this.createBy = product.getCreateBy();
        this.updateAt = product.getUpdateAt();
        this.updateBy = product.getUpdateBy();
        this.title = product.getTitle();
        this.shortDescription = product.getShortDescription();
        this.longDescription = product.getLongDescription();
        if (product.getCategory() != null) {
            this.categoryId = product.getCategory().getId();
        }
        this.price = product.getPrice();
        this.author = product.getAuthor();
        this.currentNumber = product.getCurrentNumber();
        this.numberOfPage = product.getNumberOfPage();
        this.slug = product.getSlug();
        this.quantitySelled = product.getQuantitySelled();
    }
}
