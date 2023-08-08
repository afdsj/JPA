package com.ohgiraffers.section08.namedquery;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class NamedQueryTests {
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

    @Test
    public void 동적쿼리를_이용한_조회_테스트() {
        String searchName = "한우";
        int searchCategoryCode = 4;

        StringBuilder jpql = new StringBuilder("SELECT m FROM menu_section08 m ");
        if (searchName != null && !searchName.isEmpty() && searchCategoryCode > 0) {
            jpql.append("WHERE ");
            jpql.append("m.menuName LIKE '%' || :menuName || '%'");
            jpql.append("AND ");
            jpql.append("m.category = :category ");
        } else {
            if (searchName != null && !searchName.isEmpty()) {
                jpql.append("WHERE ");
                jpql.append("m.menuName LIKE '%' || :menuName || '%'");
            } else if (searchCategoryCode > 0) {
                /* SELECT m FROM menu_section08 m 밑에 where + m.category = :category
                 *  SELECT m FROM menu_section08 m WHERE m.category = :category*/
                jpql.append("WHERE ");
                jpql.append("m.category = :category ");
            }
        }

        TypedQuery<Menu> query = entityManager.createQuery(jpql.toString(), Menu.class);

        // true인 경우
        if (searchName != null && !searchName.isEmpty() && searchCategoryCode > 0) {
            query.setParameter("menuName", searchName)
                    .setParameter("category", searchCategoryCode);
        } else {
            if (searchName != null && !searchName.isEmpty()) {
                query.setParameter("menuName", searchName);
            } else if (searchCategoryCode > 0) {

                query.setParameter("category", searchCategoryCode);
            }
        }

        List<Menu> menuList = query.getResultList();
        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }

    @Test
    void 네임드쿼리를_이용한_조회_테스트() {
        /* name에는 메뉴에 있는 쿼리 이름이 들어가야 됌 */
        List<Menu> menuList = entityManager.createNamedQuery("menu_section08.selectMenuList", Menu.class).getResultList();

        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
}
