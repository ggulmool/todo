package me.ggulmool.todo.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "user")
@ToString
public class User {

    @Id
    @Column(name = "user_id", length = 20, nullable = false)
    private String userId;

    @Getter
    @Column(length = 20, nullable = false)
    private String name;

    @Getter
    @Column(length = 60, nullable = false)
    private String password;

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
}
