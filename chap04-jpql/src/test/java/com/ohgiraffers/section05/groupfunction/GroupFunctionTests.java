package com.ohgiraffers.section05.groupfunction;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GroupFunctionTests {

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
    void 특정_카테고리의_등록된_메뉴_수_조회(){
        int categoryCodeParameter = 4;

        String jpql = "SELECT COUNT(m.menuPrice) FROM menu_section05 m WHERE m.categoryCode = :categoryCode";
        long countOfMenu = entityManager.createQuery(jpql, Long.class)
                .setParameter("categoryCode",categoryCodeParameter).getSingleResult();
        Assertions.assertTrue(countOfMenu >= 0);
        System.out.println(countOfMenu);
    }

    @Test
    void count를_제외한_다른_그룹함수의_조회결과가_없는_경우_테스트(){
        int categoryCodeParameter = 2;

        String jpql = "SELECT SUM(m.menuPrice) FROM menu_section05 m WHERE m.categoryCode = :categoryCode";

        // 에러 체크
        Assertions.assertThrows(NullPointerException.class, () ->{
            long sumOfPrice = entityManager.createQuery(jpql, Long.class)
                    .setParameter("categoryCode",categoryCodeParameter).getSingleResult();
        });

        // 에러가 아닌 null을 담는다
        Assertions.assertDoesNotThrow(()->{
            Long sumOfPrice = entityManager.createQuery(jpql, Long.class)
                    .setParameter("categoryCode", categoryCodeParameter).getSingleResult();
        });
    }

    @Test
    void groupby절과_naving절을_사용한_조회_테스트(){
        long minPrice = 50000L;

        String jpql = "SELECT m.categoryCode, SUM(m.menuPrice)" +
                "FROM menu_section05 m" +
                " GROUP BY m.categoryCode" +
                " HAVING SUM(m.menuPrice) >= :minPrice"; // :minPrice : 내가 지정해주는 거

        List<Object[]> sumPriceOfCategoryList = entityManager.createQuery(jpql, Object[].class)
                .setParameter("minPrice", minPrice).getResultList();

        Assertions.assertNotNull(sumPriceOfCategoryList);
        sumPriceOfCategoryList.forEach(row -> {
            Arrays.stream(row).forEach(System.out::println);
        });
    }
}
