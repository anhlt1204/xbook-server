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
    private String image;

    public void validate() {
        if (Strings.isNullOrEmpty(title)) {
            throw new InvalidException("Title is invalid");
        }

        if (Strings.isNullOrEmpty(shortDescription)) {
            throw new InvalidException("Short description is invalid");
        }

        if (Strings.isNullOrEmpty(longDescription)) {
            throw new InvalidException("Long description is invalid");
        }

        if (categoryId == null || categoryId <= 0) {
            throw new InvalidException("Category id is invalid");
        }

        if (price == null || price <= 0) {
            throw new InvalidException("Price is invalid");
        }

        if (Strings.isNullOrEmpty(author)) {
            throw new InvalidException("Author is invalid");
        }

        if (currentNumber == null || currentNumber < 0) {
            throw new InvalidException("Current number is invalid");
        }

        if (numberOfPage == null || numberOfPage <= 0) {
            throw new InvalidException("Number of page is invalid");
        }

        if (quantitySelled == null || quantitySelled < 0) {
            throw new InvalidException("Quantity selled is invalid");
        }

        if (Strings.isNullOrEmpty(image)) {
            throw new InvalidException("Image is invalid");
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
