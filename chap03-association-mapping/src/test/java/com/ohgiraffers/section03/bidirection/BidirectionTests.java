package com.ohgiraffers.section03.bidirection;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class BidirectionTests {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManeger() {
        entityManager.close();
    }

    @Test
    public void 양방향_연관관계_매핑_조회_테스트() { //stack off flow가 발생한다.

        int menuCode = 10;
        int categoryCode = 10;

        /* 연관관계의 주인, 주인은 속해있는거 다 조회 */
        Menu realKey = entityManager.find(Menu.class, menuCode);
        // 가짜, 이미 관계설정이 되어 있기 때문에 menu에서 category 코드를 조회하지 않음
        Category falseKey = entityManager.find(Category.class, categoryCode);

        Assertions.assertEquals(menuCode, realKey.getMenuCode());
        Assertions.assertEquals(categoryCode, falseKey.getCategoryCode());

        System.out.println(realKey);
        System.out.println(falseKey);
    }

    @Test
    void 양방향_연관관계_주인_객체를_이용한_삽입_테스트() {
        Menu menu = new Menu();
        menu.setMenuCode(126);
        menu.setMenuName("연관관계 주인메뉴");
        menu.setMenuPrice(1000);
        menu.setOrderableStatus("Y");

        menu.setCategory(entityManager.find(Category.class, 4));

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(menu);
        transaction.commit();

        Menu find = entityManager.find(Menu.class, menu.getMenuCode());
        Assertions.assertEquals(menu.getMenuCode(), find.getMenuCode());
        System.out.println(find);
    }
    // 무조건 양방향 관계를 맺을 필요는 없다!

    @Test
    void 양방향_연관관계_주인이_아닌_객체를_이용한_삽입_테스트(){
        Category category = new Category();
        category.setCategoryCode(1004);
        category.setCategoryName("양방향카테고리");
        category.setRefCategoryCode(null);

        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.persist(category);
        transaction.begin();
        transaction.commit();

        Category findCategory = entityManager.find(Category.class, category.getCategoryCode());
        Assertions.assertEquals(category.getCategoryCode(), findCategory.getCategoryCode());

    }

    @Test
    public void test(){
        // 연관관계의 주인
        Menu menu = entityManager.find(Menu.class, 10);
        // 연관관계의 주인이 아님 (카테고리 값만 조회됌)
        Category category = entityManager.find(Category.class, 9);

        List<Menu> menuList = category.getMenuList();
        System.out.println("AS===========");
        System.out.println(menuList); // mappedBy 이때 메뉴 리스트 select 쿼리가 뜸
    }
}
