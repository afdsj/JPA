package com.ohgiraffers.section01.simple;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Stream;

public class NativeQueryTests {

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

    /* 타입을 지정할 수 있는 경우 */
    @Test
    void 결과_타입을_정의한_네이티브_쿼리_사용_테스트() {
        int menuCodeParameter = 15;
        /* 데이터 베이스에 커넥션이 직접 연결 되기 때문에 DB에 지정된 컬럼명을 기술해야 하며
         *  이때는 위치기반 파라미터 방식으로 파라미터를 넘기는 것이 가능하다
         *  일부 컬럼만 조회하는 것은 불가능 하다 */
        String query = "SELECT menu_code, menu_name, menu_price, category_code, orderable_status FROM tbl_menu WHERE menu_code = ? ";

        Query nativeQuery = entityManager.createNativeQuery(query, Menu.class).setParameter(1, menuCodeParameter);
        /* Query = Object 타입이기 때문에 형변환 해줌 */
        Menu findMenu = (Menu) nativeQuery.getSingleResult();

        Assertions.assertNotNull(findMenu);
        /* 영속성 컨텍스트에서도 관리 하고 있는지 확인하기 위함 */
        Assertions.assertTrue(entityManager.contains(findMenu));

        System.out.println(entityManager.contains(findMenu));
        System.out.println(findMenu);
    }

    /* 타입을 지정할 수 없는 경우 */
    @Test
    void 결과_타입을_정의할_수_없는_경우_조회_테스트() {
        String query = "SELECT menu_name, menu_price FROM tbl_menu";
        /* List : 목록들 */
        List<Object[]> menuList = entityManager.createNativeQuery(query).getResultList();

        Assertions.assertNotNull(menuList);

        menuList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.println(col + " "));
            System.out.println();
        });
    }

    @Test// 에러 발생
    void 자동_결과_매핑을_사용한_조회_테스트() {
        String query = "SELECT " +
                " a.category_code, a.category_name, a.ref_category_code, COALESCE(v.menu_count, 0) menu_count" +
                " FROM tbl_category a" +
                /*뷰 만들기*/
                " LEFT JOIN (SELECT COUNT(*) AS menu_count, b.category_code " +
                "              FROM tbl_menu b " +
                "             GROUP BY b.category_code) v on (a.category_code = v.category_code) " +
                " ORDER BY 1";

        Query nativeQuery = entityManager.createNativeQuery(query, "categoryCountAutoMapping");
        List<Object[]> categoryList = nativeQuery.getResultList();

        Assertions.assertTrue(entityManager.contains(categoryList.get(0)[0]));
        Assertions.assertNotNull(categoryList);

        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.println(col + " "));
            System.out.println();
        });
    }

    @Test // 에러 발생
    void 수동_결과_매핑을_사용한_조회_테스트(){
        String query = "SELECT " +
                " a.category_code, a.category_name, a.ref_category_code, COALESCE(v.menu_count, 0) menu_count" +
                " FROM tbl_category a " +
                " LEFT JOIN (SELECT COUNT(*) AS menu_count, b.category_code " +
                "              FROM tbl_menu b" +
                "             GROUP BY b.category_code) v on (a.category_code = v.category_code)" +
                " ORDER BY 1";

        Query nativeQuery = entityManager.createNativeQuery(query, "categoryCountManualMapping");
        List<Object[]> categoryList = nativeQuery.getResultList();

        Assertions.assertTrue(entityManager.contains(categoryList.get(0)[0])); // category 인스턴스
        Assertions.assertNotNull(categoryList);

        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.println(col+" "));
            System.out.println();
        });
    }
}
