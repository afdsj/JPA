package com.ohgiraffers.test3;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/* 쌤이 실습 내준 문제 보다가 설명해준거!*/
@Entity
@Table(name = "child")
public class Child {

    /* 참조키를 부모 또는 자식에게 넣기
    *  부모가 자식이 여러명 또는 자식이 부모가 여러명일 경우에는 관계를 어떻게 DB로 할거야?
    *  n:n 관계는 사용 안함 */

    @Id
    @Column(name = "child_code")
    private int code;
    @Column(name = "child_name")
    private String name;
    @Column(name = "child_age")
    private String age;

}
