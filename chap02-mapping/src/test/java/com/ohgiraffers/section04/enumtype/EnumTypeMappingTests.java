package com.ohgiraffers.section04.enumtype;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

public class EnumTypeMappingTests {
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
    void enum타입_매핑_테스트() {

        Member member = new Member();
        member.setMemberId("user01");
        member.setMemberPwd("user01");
        member.setMemberNick("길동");
        member.setEmail("email01");
        member.setPhone("010-1234-5678");
        member.setEnrollDate(new Date());
        member.setAddress("경기도 김포");
        member.setMemberRole(RoleType.ADMIN); // RoleType.MEMBER : 값 자체를 필드로 반환시킴
        member.setStatus("Y");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(member);
        transaction.commit();

    }
}
