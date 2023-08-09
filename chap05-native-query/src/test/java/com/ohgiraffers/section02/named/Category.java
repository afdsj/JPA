package com.ohgiraffers.section02.named;

import javax.persistence.*;

@Entity(name = "category_section02")
@Table(name = "tbl_category")
@SqlResultSetMapping(name = "categoryCountAutoMapping2",
        entities = {@EntityResult(entityClass = Category.class)}, // 반환 타입
        columns = {@ColumnResult(name = "menu_count")} // 반환 되는게 없어서 별도로 기술함
)
// row[0] = category(a.category_code, a.category_name, a.ref_category_code)
// row[1] = menu_count

/* 일회성으로 사용하는 것보다 재사용을 해야 할 경우 사용하는게 더 용이함 */
@NamedNativeQueries(
        value = {
                @NamedNativeQuery(
                        name = "Category.menuCountOfCategory",
                        query = "SELECT" +
                                /* COALESCE(v.menu_count, 0) : null일 경우 0으로 치환한 후 별칭 줌 -> menu_count */
                                " a.category_code, a.category_name, a.ref_category_code, COALESCE(v.menu_count, 0) menu_count" +
                                " FROM tbl_category a" +
                                /* v카테고리를 기준으로 몇개씩 컬럼들이 있는지 group by로 뭉쳐줌
                                 *  v : 뷰 가상의 테이블 ( )*/
                                " LEFT JOIN (SELECT COUNT(*) AS menu_count, b.category_code" +
                                "              FROM tbl_menu b " +
                                "             GROUP BY b.category_code) v ON (a.category_code = v.category_code)" +
                                " ORDER BY 1",
                        /* 반환된 결과를 매핑 시켜줌*/
                        resultSetMapping = "categoryCountAutoMapping2"
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
