package com.ohgiraffers.section02.parameter;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ParameterBindingTests {

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
    void 이름_기준_파라미터_바인딩_메뉴_목록_조회_테스트() {

        String menuNameParameter = "한우딸기국밥";
        String jpql = "SELECT m FROM menu_section02 m WHERE m.menuName = :menuName";

        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                /* : menuName이랑 "menuName"이랑 맞춰줘야 함*/
                .setParameter("menuName", menuNameParameter).getResultList();

        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }

    @Test
    void 위치_기반_파라미터_빠인딩_메뉴_목록_조회_테스트() {

        String menuNameParameter = "한우딸기국밥";
        String jpql = "SELECT m FROM menu_section02 m WHERE m.menuName = ?1";

        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setParameter(1, menuNameParameter).getResultList();

        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
}
