package com.ohgiraffers.section02.named;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Stream;

public class NamedNativeQueryTests {
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
    void NamedNativeQuery를_이용한_조회_테스트(){

        Query nativeQuery = entityManager.createNamedQuery("Category.menuCountOfCategory");
        List<Object[]> categoryList = nativeQuery.getResultList();

        Assertions.assertNotNull(categoryList);
        Assertions.assertTrue(entityManager.contains(categoryList.get(0)[0]));

        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.println(col+" "));
            System.out.println();
        });
    }
}
