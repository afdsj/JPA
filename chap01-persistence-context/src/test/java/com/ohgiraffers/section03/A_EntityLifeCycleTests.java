package com.ohgiraffers.section03;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class A_EntityLifeCycleTests {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        System.out.println("initFactory");
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }


    @AfterAll
    public static void closeFactory() {
        System.out.println("close factory");
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    @Test
    void 비영속성_테스트() {
        Menu foundMenu = entityManager.find(Menu.class, 11);

        Menu newMenu = new Menu();
        newMenu.setMenuCode(foundMenu.getMenuCode());
        newMenu.setMenuName(foundMenu.getMenuName());
        newMenu.setMenuPrice(foundMenu.getMenuPrice());
        newMenu.setCategoryCode(foundMenu.getCategoryCode());
        newMenu.setOrderStatus(foundMenu.getOrderStatus());

        boolean result = foundMenu == newMenu; // 주소값이 다르다
        // == 동등 비교시 주소값으로 비교한다

        Assertions.assertFalse(result);
    }

    @Test
    public void 영속성_연속_조회_테스트() {
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 11);

        boolean isTrue = (foundMenu1 == foundMenu2); // 주소값이 같다

        Assertions.assertTrue(isTrue);
    }

    @Test
    void 영속성_객체_추가_테스트() {
        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(500);
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(10000);
        menuToRegist.setCategoryCode(1);
        menuToRegist.setOrderStatus("Y");

        entityManager.persist(menuToRegist);
        Menu foundMenu = entityManager.find(Menu.class, 500);
        boolean isTrue = (menuToRegist == foundMenu);

        Assertions.assertTrue(isTrue);
    }

    @Test
    void 영속성_객체_추가_값_변경_테스트() {
        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(500);
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(10000);
        menuToRegist.setCategoryCode(1);
        menuToRegist.setOrderStatus("Y");

        entityManager.persist(menuToRegist);
        menuToRegist.setMenuName("고민영 바보");

        Menu foundMenu = entityManager.find(Menu.class, 500);

        Assertions.assertEquals(menuToRegist, foundMenu);
    }

    @Test
    void 준영속성_detach_테스트() {
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        // 참조를 중단함
        /* 준영속성은 delete 시키지 않는다
         *  remove는 커밋시 delete 시킨다*/
        entityManager.detach(foundMenu2);

        // 엔티티의 값을 변경함
        foundMenu1.setMenuPrice(5000); // 영속성
        foundMenu2.setMenuPrice(5000); // 준영속성
//        entityManager.persist(foundMenu2); // 다시 영속화

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        transaction.commit();


        // 참
        Assertions.assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice()); // 기존에 있는 값을 가지고 온다
        // 거짓
//        Assertions.assertEquals(5000, entityManager.find(Menu.class,12).getMenuPrice()); //
    }

    @Test
    public void 준영속성_clear_테스트() {
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        // persits를 초기화
        entityManager.clear();

        // 엔티티의 값을 변경함
        foundMenu1.setMenuPrice(5000); // 준영속성
        foundMenu2.setMenuPrice(5000); // 준영속성

        // 참으로 하고자 하는 경우 다시 영속화 한다
//        entityManager.persist(foundMenu1);
//        entityManager.persist(foundMenu2);

        // 참
        Assertions.assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());
        // 거짓
        Assertions.assertEquals(5000, entityManager.find(Menu.class,12).getMenuPrice());
    }

    @Test
    void 삭제_remove_테스트(){
        Menu found = entityManager.find(Menu.class, 1);

        entityManager.remove(found);

        // 쿼리 실행 여부 -> 지웠다고 판단해서 null이 나옴
        Menu reFound = entityManager.find(Menu.class, 1);

        Assertions.assertEquals(1, found.getMenuCode());
        Assertions.assertEquals(null, reFound);
    }

    @Test
    void 병합_merge_수정_테스트(){
        Menu menuToDetach = entityManager.find(Menu.class,2);
        entityManager.detach(menuToDetach);
        menuToDetach.setMenuName("달콤 꿀밤");
        Menu refound = entityManager.find(Menu.class,2);

        System.out.println("menuToDetach : " + menuToDetach.hashCode());
        System.out.println("refound : " + refound.hashCode());

        // 다시 병합해주기
        entityManager.merge(menuToDetach);
        // 다시 조회하기
        Menu mergeMenu = entityManager.find(Menu.class,2);
        System.out.println("mergeMenu : " + mergeMenu.hashCode());
        System.out.println("mergeMenu Value : " + mergeMenu);

        Assertions.assertEquals("달콤 꿀밤", mergeMenu.getMenuName());
    }

    // merge를 할때는 준영속 상태일때만 가능
    @Test
    void 병합_merge_삽입_테스트(){
        Menu menuToDetach = entityManager.find(Menu.class, 2);
        // 준영속
        entityManager.detach(menuToDetach);

        menuToDetach.setMenuCode(999);
        menuToDetach.setMenuName("수박죽");

        // 999이기 때문에 병합할 대상이 없어서 삽입이 되었음
        entityManager.merge(menuToDetach);

        Menu merMenu = entityManager.find(Menu.class, 999);

        System.out.println(menuToDetach.hashCode());
        System.out.println(merMenu.hashCode());

        Assertions.assertEquals("수박죽", merMenu.getMenuName());
    }
}
