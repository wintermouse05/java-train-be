package com.example.javatrainbe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

/**
 * Doma Entity cho bảng user (authentication)
 */
@Entity(immutable = true)
@Table(name = "user")
@Data
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private final Integer userId;

    @Column(name = "user_name")
    private final String userName;

    @Column(name = "password")
    private final String password;

    /**
     * Convenience constructor (userId auto-generated)
     */
    public User(String userName, String password) {
        this.userId = null;
        this.userName = userName;
        this.password = password;
    }
}
