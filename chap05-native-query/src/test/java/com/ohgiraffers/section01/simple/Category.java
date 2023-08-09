package com.ohgiraffers.section01.simple;

import javax.persistence.*;

@Entity(name = "category_section01")
@Table(name = "tbl_category")
@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "categoryCountAutoMapping", // 매핑 이름
                        entities = {@EntityResult(entityClass = Category.class)}, // 매핑 엔티티
                        columns = {@ColumnResult(name = "menu_count")} // 결과 매핑
                ),
                @SqlResultSetMapping(
                        name = "categoryCountManualMapping",
                        entities = {
                                @EntityResult(entityClass = Category.class, fields = {
                                        @FieldResult(name = "categoryCode", column = "category_code"),
                                        @FieldResult(name = "categoryName", column = "category_name"),
                                        @FieldResult(name = "refCategoryCode", column = "ref_category_code"),
                                })
                        },
                        /* 영속성 컨텍스트에서 관리하지 않음*/
                        columns = {@ColumnResult(name = "menu_count")}
                )
        }
)
public class Category {

    @Id
    @Column(name = "category_code")
    private int categoryCode;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "ref_category_code")
    private Integer refCategoryCode;

    public Category() {
    }

    public Category(int categoryCode, String categoryName, Integer refCategoryCode) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.refCategoryCode = refCategoryCode;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getRefCategoryCode() {
        return refCategoryCode;
    }

    public void setRefCategoryCode(Integer refCategoryCode) {
        this.refCategoryCode = refCategoryCode;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryCode=" + categoryCode +
                ", categoryName='" + categoryName + '\'' +
                ", refCategoryCode=" + refCategoryCode +
                '}';
    }
}
