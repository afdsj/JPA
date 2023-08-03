package com.ohgiraffers.section01.entity;

import org.junit.jupiter.api.*;

import javax.persistence.*;
import java.util.Date;

public class EntityMappingTests {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory(){
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest"); // JPA의 Persistence 환경설정
    }
    @BeforeEach
    public void initManager(){
        entityManager = entityManagerFactory.createEntityManager();
    }
    @AfterAll
    public static void closeFactory(){
        entityManagerFactory.close();
    }
    @AfterEach
    public void closeManager(){
        entityManager.close();
    }

    @Test
    public void 테이블_만들기_테스트(){

        Member member = new Member(); // 비영속
        member.setMemberNo(1);
        member.setMemberId("user01");
        member.setMemberPwd("pass");
        member.setNickname("홍길동");
        member.setAddress("경기도 김포");
        member.setEnrollDate(new Date());
        member.setStatus("Y");

        entityManager.persist(member); // 영속, 쓰기 지연상태

//        EntityTransaction tr = entityManager.getTransaction();
//        tr.begin();


        Member foundMember = entityManager.find(Member.class, member.getMemberNo());

        Assertions.assertEquals(member.getMemberNo(), foundMember.getMemberNo());
    }

}
