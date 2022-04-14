package com.ados.xbook.domain.request;

import com.ados.xbook.domain.entity.Product;
import com.ados.xbook.exception.InvalidException;
import com.google.common.base.Strings;
import lombok.Data;

@Data
public class ProductRequest {
    private String title;
    private String shortDescription;
    private String longDescription;
    private Long categoryId;
    private Double price;
    private String author;
    private Integer currentNumber;
    private Integer numberOfPage;
    private Integer quantitySelled;

    private String username;

    public void validate() {
        if (Strings.isNullOrEmpty(title)) {
            throw new InvalidException("Title is invalid");
        }


    }

    public Product create() {
        Product product = new Product();

        product.setTitle(title);
        product.setShortDescription(shortDescription);
        product.setLongDescription(longDescription);
        product.setPrice(price);
        product.setAuthor(author);
        product.setCurrentNumber(currentNumber);
        product.setNumberOfPage(numberOfPage);
        product.setQuantitySelled(quantitySelled);

        return product;
    }

    public Product update(Product product) {

        product.setTitle(title);
        product.setShortDescription(shortDescription);
        product.setLongDescription(longDescription);
        product.setPrice(price);
        product.setAuthor(author);
        product.setCurrentNumber(currentNumber);
        product.setNumberOfPage(numberOfPage);
        product.setQuantitySelled(quantitySelled);

        return product;
    }
}
