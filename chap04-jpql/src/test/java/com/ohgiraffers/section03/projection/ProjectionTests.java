package com.ohgiraffers.section03.projection;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectionTests {

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
    void 단일_엔티티_프로젝션_테스트(){

        String jpql = "SELECT m FROM menu_section03 m";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        Assertions.assertNotNull(menuList);
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        menuList.get(1).setMenuName("test");
        entityTransaction.commit();
    }

    @Test
    public void 양방향_연관관계_엔티티_프로젝션_테스트(){
        int menuCode = 3;
        /* 카테고리에 대한 메뉴만 조회!
        *  category : 메뉴의 필드 (bidirection category 필드가 기술 됌*/
        String jpql = "SELECT m.category FROM bidirection_menu m WHERE m.menuCode = :menuCode";

        /* 영속성 컨텍스트에서 관리중 */
        BiDirectionCategory categoryOfMenu = entityManager.createQuery(jpql, BiDirectionCategory.class)
                        .setParameter("menuCode", menuCode).getSingleResult();

        Assertions.assertNotNull(categoryOfMenu);
        System.out.println(categoryOfMenu);
        /* 양방향 연관관계 menu.category(연관관계의 주인 객체), category.menu(연관관계의 주인이 아닌 객체) */
        categoryOfMenu.getMenuList().forEach(System.out::println);
    }

    @Test
    void 임베디드_타입_프로젝션_테스트(){
        /* 프로젝션 : 조회할 대상
        *  1. Embeddable 타입을 프로젝션 대상으로 지정할 수 있다 */
        String jpql = "SELECT m.menuInfo FROM embedded_menu m";
        /* 2. Embeddable 타입을 반환 타입으로 지정할 수 있다*/
        List<MenuInfo> menuInfoList = entityManager.createQuery(jpql, MenuInfo.class).getResultList();

        Assertions.assertNotNull(menuInfoList);
        menuInfoList.forEach(System.out::println);

        /* 3. Embeddable 타입은 영속성 컨텍스트에서 관리하지 않는다 */
    }

    @Test
    public void typeQuery를_이용한_스칼라_타입_프로젝션_테스트(){

        String jpql = "SELECT c.categoryName FROM category_section03 c";
        List<String> categoryNameList = entityManager.createQuery(jpql,String.class).getResultList();

        Assertions.assertNotNull(categoryNameList);
        categoryNameList.forEach(System.out::println);
    }

    @Test
    void query를_이용한_스칼라_타입_프로젝션_테스트(){

        String jpql = "SELECT c.categoryCode, c.categoryName FROM category_section03 c";
        List<Object[]> categoryList = entityManager.createQuery(jpql).getResultList();
        // List<object[categoryCode, categoryName]>
        Assertions.assertNotNull(categoryList);

        // 람다 형식의 스트림
        categoryList.forEach(row -> {
            Arrays.stream(row).forEach(System.out::println);
        });
    }

    @Test // 에러 발생
    void new_명령어를_활용한_프로젝션_테스트(){

        String jpql = "SELECT new com.ohgiraffers.section03.projection.CategoryInfo(c.categoryCode,c.categoryName)"+"FROM category_section03";
        List<CategoryInfo> categoryInfoList = entityManager.createQuery(jpql, CategoryInfo.class).getResultList();

        Assertions.assertNotNull(categoryInfoList);
        categoryInfoList.forEach(System.out::println);
    }
}
