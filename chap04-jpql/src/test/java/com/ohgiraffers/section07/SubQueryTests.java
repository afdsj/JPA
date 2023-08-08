package com.ohgiraffers.section07;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class SubQueryTests {
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
    public void closeManager() {

        entityManager.close();
    }

    @Test
    void 서브쿼리를_이용한_메뉴_조회_테스트() {
        /* sql 서브쿼리 : select, from, where, having절에 사용 가능
        *  jpa에서는 select, from 여기에 작성하는 것이 사용 불가능*/
        String categoryNameParameter = "한식";
        String jpql = "SELECT m FROM menu_section07 m WHERE m.category"
                + " = (SELECT c.categoryCode FROM category_section07 c WHERE c.categoryName = :categoryName)";

        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setParameter("categoryName", categoryNameParameter)
                .getResultList();

        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
}
