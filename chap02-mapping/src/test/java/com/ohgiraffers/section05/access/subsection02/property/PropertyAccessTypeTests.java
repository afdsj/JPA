package com.ohgiraffers.section05.access.subsection02.property;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PropertyAccessTypeTests {
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

    /* JPA 접근 방식
    *  필드 :
    *  GET : */
    @Test
    void 프로퍼티_접근_테스트(){
        Member newMember = new Member();
        newMember.setMemberNo(1);
        newMember.setMemberName("이상우");
        newMember.setNickName("마동석");
        newMember.setAddress("동석이형 집");

        entityManager.persist(newMember); // 쓰기지연 상태 sql 쿼리를 생성함 해당 시점에서 필드에 접근을 하게된다

        Member findMember = entityManager.find(Member.class, 1);

        System.out.println("newMember : " + newMember);
        System.out.println("findMember : " + findMember);

    }
}
