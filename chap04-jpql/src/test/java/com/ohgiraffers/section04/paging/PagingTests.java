package com.ohgiraffers.section04.paging;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class PagingTests {

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
    void 페이징_API를_이용한_조회_테스트(){
        int offset = 10;
        int limit = 5;

        String jpql = "SELECT m FROM menu_section04 m ORDER BY m.menuCode DESC";
        /* 2번 페이지 offset 20 limit 10*/
        List<Menu> menuList = entityManager.createQuery(jpql,Menu.class)
                .setFirstResult(offset) // 데이터 조회를 시작할 index
                .setMaxResults(limit) // 데이터 조회할 개수
                .getResultList();
        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }

}
