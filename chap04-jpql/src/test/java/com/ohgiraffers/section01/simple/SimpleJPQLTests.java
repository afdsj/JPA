package com.ohgiraffers.section01.simple;

import org.junit.jupiter.api.*;

import javax.persistence.*;
import java.util.List;

public class SimpleJPQLTests {

    /* JPQL (Java Persistence Query Language)
    *  jpql은 엔티티 객체를 중심으로 개발할 수 있는 객체 지향 쿼리이다
    *  sql보다 간결하며 특정 dbms에 맞는 sql을 실행하게 된다
    *  jpql은 find() 메소드를 통한 조회와 다르게 항상 데이터베이스에 sql을 실행해서 결과를 조회한다 */

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

    /* jpql 기본 문법
    *  select, update delete 등의 키워드 사용은 sql과 동일하다
    *  insert는 persist() 메소드를 사용하면 된다
    *  키워드(sql 문법을 의미함)는 대소문자를 구분하지 않지만, 엔티티와 속성은 대소문자를 구분함에 유의한다
    *
    *  jpql 사용법
    *  1. 작성한 jpql(문자열)을 entityManager.createQuery() 메소드를 통해 쿼리 객체로 만든다
    *  2. 쿼리 객체는 TypedQuery, Query 두가지가 있다
    *  3. TypedQuery : 반환할 타입을 명확하게 지정하는 방식일 때 사용하며 쿼리 객체의 메소드 실행 결과로 지정한 타입이 반환된다
    *  4. Query : 반환할 타입을 명확하게 지정할 수 없을 때 사용하며 쿼리 객체 메소드의 실행 결과로 object 또는 object[]이 반환된다
    *  5. getSingleResult() : 결과가 정확히 한 행인 경우 사용하며 없거나 많은 경우 예외가 발생된다
    *  6. getResultList() : 결과가 2행 이상인 경우 사용하며 컬렉션을 반환한다 결과가 없으면 빈 컬렉션을 반환한다
    * */

    @Test
    void typeQuery를_이용한_단일메뉴_조회_테스트(){

        /*
           @Entity(name = "menu_section01") //테이블명!!

           @Column(name = "menu_name") //컬럼명X
           private String menuName; //필드명!!
        * */

        // 필드는 소문자 대문자 구분, sql 구문은 대문자로 사용하는게 좋음
        String jpql = "SELECT m.menuName FROM menu_section01 as m WHERE m.menuCode=7 ";
        // 첫번째 인자 :  , 두번째 인자 : 반환된 타입
        TypedQuery<String> query = entityManager.createQuery(jpql, String.class);

        String result = query.getSingleResult();

        Assertions.assertEquals("민트미역국", result);
        System.out.println(result);
    }

    @Test
    void Query를_이용한_단일메뉴_조회_테스트(){

        String jpql = "SELECT m.menuName FROM menu_section01 as m WHERE m.menuCode=7 ";
        Query query = entityManager.createQuery(jpql);

        Object result = query.getSingleResult();

        Assertions.assertEquals("민트미역국", result);
        System.out.println(result);
    }

    @Test
    void TypeQuery를_이용한_단일행_조회_테스트(){

        String query = "select m from menu_section01 as m where m.menuCode=7";
        TypedQuery<Menu> result = entityManager.createQuery(query, Menu.class);

        Menu foundMenu = result.getSingleResult();

        Assertions.assertEquals(7, foundMenu.getMenuCode());
        System.out.println(foundMenu);

        /* 영속성 컨텍스트에서 관리가 되는지 확인하는 코드*/
        Menu found2 = entityManager.find(Menu.class, 7);
        System.out.println("select2 : " + found2);
    }

    @Test
    void TypedQuery를_이용한_다중행_조회_테스트(){

        String jpql = "SELECT m FROM menu_section01 AS m";
        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class);

        List<Menu> foundMenuList = query.getResultList();

        Assertions.assertNotNull(foundMenuList);
        foundMenuList.forEach(System.out::println);
    }

    @Test
    void Query를_이용한_다중행_조회_테스트(){

        String jpql = "SELECT m FROM menu_section01 AS m";
        Query query = entityManager.createQuery(jpql);

        List<Menu> foundMenuList = query.getResultList();

        Assertions.assertNotNull(foundMenuList);
        foundMenuList.forEach(System.out::println);
    }

    @Test
    public void distinct를_활용한_중복제거_여러_행_조회_테스트(){

        String jpql = "SELECT DISTINCT m.categoryCode FROM menu_section01 m";
        TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
        List<Integer> categoryCodeList = query.getResultList();

        Assertions.assertNotNull(categoryCodeList);
        categoryCodeList.forEach(System.out::println);
    }

    @Test
    public void like_연산자를_활용한_조회_테스트(){

        String jpql = "SELECT m FROM menu_section01 m WHERE m.menuName LIKE '%마늘%'";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }


}
