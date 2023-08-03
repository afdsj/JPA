package com.ohgiraffers.section05.access.subsection01.filed;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

// 접근하는 설정 방식 (JPA 주체)
public class FiledAccessTests {

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

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    /* JPA는 엔티티에 접근할때 필드에 직접 접근을 하는 방식과 Get를 이용하여 접근 하는 방식으로 구분된다 */
    @Test
    void Access_테스트() {

        Member member = new Member();
        member.setMemberNo(1);
        member.setMemberId("user01");
        member.setMemberPwd("user01");
        member.setMemberNick("길동");
        member.setEmail("email01");
        member.setPhone("010-1234-5678");
        member.setEnrollDate(new Date());
        member.setAddress("경기도 김포");
        member.setMemberRole(RoleType.ADMIN);
        member.setStatus("Y");

        entityManager.persist(member); // 영속성 컨텍스트 등록

        Member findMember = entityManager.find(Member.class, 1); // 여기서 접근
        System.out.println(findMember);
    }
}
