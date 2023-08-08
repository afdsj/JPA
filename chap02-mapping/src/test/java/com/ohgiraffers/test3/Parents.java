package com.ohgiraffers.test3;

import javax.persistence.*;

@Entity
@Table(name = "parents")
public class Parents {
//    명단
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
//    @JoinColumn(name = "테이블명", joinColumns = "해당 테이블과 관계를 맺을 컬럼", inverseJoinColumns = "반대편")
//    private List<OrderMapping> orderMappings;

    @Id
    @Column(name = "parents_code")
    private int code;

    @Column(name = "parents_name")
    private String name;

    /* 매핑 테이블을 별도로 생성하지 않아도 됌*/
    @ManyToOne
    @JoinTable(name = "Family_Mapping",
    joinColumns = @JoinColumn(name = "parents_code"), inverseJoinColumns = @JoinColumn(name = "child_code"))
    private Child child;
}
