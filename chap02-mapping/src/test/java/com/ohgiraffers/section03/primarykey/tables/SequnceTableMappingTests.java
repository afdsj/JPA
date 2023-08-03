package com.ohgiraffers.section03.primarykey.tables;

import com.ohgiraffers.section03.primarykey.tables.Member;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

public class SequnceTableMappingTests {

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

    @Test
    void 식별자_매핑_테스트() {
        Member member = new Member();
        member.setMemberId("user01");
        member.setMemberPwd("user01");
        member.setMemberNick("길동");
        member.setEmail("email01");
        member.setPhone("010-1234-5678");
        member.setEnrollDate(new Date());
        member.setAddress("경기도 김포");
        member.setMemberRole("ROLE_MEMBER");
        member.setStatus("Y");

        Member member1 = new Member();
        member1.setMemberId("user01");
        member1.setMemberPwd("user01");
        member1.setMemberNick("길동");
        member1.setEmail("email02");
        member1.setPhone("010-1234-5678");
        member1.setEnrollDate(new Date());
        member1.setAddress("경기도 김포");
        member1.setMemberRole("ROLE_MEMBER");
        member1.setStatus("Y");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(member); // 영속성 컨텍스트
        entityManager.persist(member1);
        transaction.commit();

        String jpql = "SELECT A.memberNo from member_section03_subsection02 A";
        List<Integer> memberNoList = entityManager.createQuery(jpql, Integer.class).getResultList();

        memberNoList.forEach(System.out::println);

    }
}
