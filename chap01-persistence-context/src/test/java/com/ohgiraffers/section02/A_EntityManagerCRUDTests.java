package com.ohgiraffers.section02;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class A_EntityManagerCRUDTests {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest"/*환경 등록*/);
    }

    @BeforeEach
    public void intManager() {
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

    // 조회 (select)?
    @Test
    public void 메뉴코드로_메뉴_조회_테스트() {
        int menuCode = 2;

        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        Assertions.assertNotNull(foundMenu);
        Assertions.assertEquals(menuCode, foundMenu.getMenuCode());
        System.out.println("foundMenu : " + foundMenu);
    }

    // 등록 (insert)
    @Test
    void 새로운_메뉴_추가_테스트() {
        Menu menu = new Menu(); // 비영속성
        menu.setMenuName("제로사이다 꿀맛");
        menu.setMenuPrice(840000);
        menu.setCategoryCode(4);
        menu.setOrderStatus("Y");

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {
            entityManager.persist(menu); // 영속성
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Test
    void 메뉴_이름_수정_테스트() {
        // 최초 메뉴의 상태
        Menu menu = entityManager.find(Menu.class, 23);
        System.out.println("menu = " + menu);

        int price = 8400;
        String menuName = "민영 사이다";

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        // 메뉴 이름이 변경된다 (변경 대상 : )
        menu.setMenuName(menuName);
        menu.setMenuPrice(price);

        try {
            // 데이터 베이스에 업데이트 쿼리를 실행 후 저장함
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
        }
        // menu : 변경 후 내용
        // 변경이 잘 되었는지 확인
        Assertions.assertEquals(menu, entityManager.find(Menu.class, 23));
    }

    @Test
    void 메뉴_삭제_테스트() {
        Menu menu = entityManager.find(Menu.class, 23);

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {
            entityManager.remove(menu);
            // 캐시의 상태를 확인해서 처리해줌
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
        }
        Menu removeMenu = entityManager.find(Menu.class, 23);
        Assertions.assertEquals(null, menu);
    }
}
