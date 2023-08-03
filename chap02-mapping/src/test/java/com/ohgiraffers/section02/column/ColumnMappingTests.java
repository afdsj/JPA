package com.ohgiraffers.section02.column;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

public class ColumnMappingTests {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initEntityFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Test
    void 컬럼에서_사용하는_속성_테스트() {
        Member member = new Member();
        member.setMemberNo(1);
        member.setMemberId("user");
        member.setMemberPwd("pass");
        member.setMemberNick("홍길동");
        member.setAddress("경기도 김포");
        member.setPhone("");
        member.setEnrollDate(new Date());
        member.setMemberRole("ROLE_MEMBER");
        member.setStatus("Y");

        entityManager.persist(member);

        Member findMember = entityManager.find(Member.class, 1);
        System.out.println(findMember.getMemberId() + " : " + findMember.getPhone());
        Assertions.assertEquals(member.getMemberNo(), findMember.getMemberNo());
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

}
