package me.ggulmool.todo.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Getter
    @Column(name = "NAME", length = 20, nullable = false)
    private String name;
}
