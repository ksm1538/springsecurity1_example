package com.cos.security1.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity(name="tb_user")
@Data
public class User {
    @Id @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String email;
    private String role;

    // OAuth2 구분자
    private String provider;
    private String providerId;

    @CreationTimestamp
    private Timestamp createDate;
}
