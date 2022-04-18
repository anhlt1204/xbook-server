package com.ados.xbook.domain.response;

import com.ados.xbook.domain.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private Integer status;
    private Date createAt;
    private String createBy;
    private Date updateAt;
    private String updateBy;
    private String name;
    private String description;
    private String slug;
    private Long parentsId;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.status = category.getStatus();
        this.createAt = category.getCreateAt();
        this.createBy = category.getCreateBy();
        this.updateAt = category.getUpdateAt();
        this.updateBy = category.getUpdateBy();
        this.name = category.getName();
        this.description = category.getDescription();
        this.slug = category.getSlug();
        if (category.getParentsCategory() != null) {
            this.parentsId = category.getParentsCategory().getId();
        }
    }
}
