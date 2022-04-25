package com.ados.xbook.domain.request;

import com.ados.xbook.domain.entity.Category;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.helper.StringHelper;
import com.google.common.base.Strings;
import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private String description;
    private Long parentsId;

    public void validate() {

        if (Strings.isNullOrEmpty(name)) {
            throw new InvalidException("Name is invalid");
        }

        if (Strings.isNullOrEmpty(description)) {
            throw new InvalidException("Description is invalid");
        }

        if (parentsId == null || parentsId < 0) {
            throw new InvalidException("ParentId is invalid");
        }

    }

    public Category create() {
        Category category = new Category();

        category.setName(name);
        category.setDescription(description);
        category.setSlug(StringHelper.toSlug(name));

        return category;
    }

    public Category update(Category category) {

        category.setName(name);
        category.setDescription(description);
        category.setSlug(StringHelper.toSlug(name));

        return category;
    }
}
