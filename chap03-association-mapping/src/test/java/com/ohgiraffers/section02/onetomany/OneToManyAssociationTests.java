package com.ohgiraffers.section02.onetomany;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class OneToManyAssociationTests {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager; // 엔티티에 대한 영속성 컨텍스트 관리

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
    public void 일대다_연관관계_객체_그래프_탐색을_이용한_조회_테스트() {

        int categoryCode = 10;

        CategoryAndMenu categoryAndMenu = entityManager.find(CategoryAndMenu.class, categoryCode);

        Assertions.assertNotNull(categoryAndMenu);
        System.out.println(categoryAndMenu);

        /* DB 조회
        *  카테고리 코드 + 메뉴 리스트
        *  */
    }

    // 등록
    @Test
    void 일대다_연관관계_객체_삽입_테스트(){

        CategoryAndMenu categoryAndMenu = new CategoryAndMenu(); // 등록하는 시점에 생김
        categoryAndMenu.setCategoryCode(880);
        categoryAndMenu.setCategoryName("일대다 추가 카테고리");
        categoryAndMenu.setRefCategoryCode(null);

        List<Menu> menuList = new ArrayList<>();
        Menu menu = new Menu();
        menu.setMenuCode(778);
        menu.setMenuName("일대다 아이스크림");
        menu.setMenuPrice(150000);
        menu.setOrderableStatus("Y");
        //
        menu.setCategoryCode(categoryAndMenu.getCategoryCode());

        menuList.add(menu);

        categoryAndMenu.setMenuList(menuList);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(categoryAndMenu);
        transaction.commit();

        CategoryAndMenu findCategoryAndMenu = entityManager.find(CategoryAndMenu.class,880);
        System.out.println(findCategoryAndMenu);
    }
}
