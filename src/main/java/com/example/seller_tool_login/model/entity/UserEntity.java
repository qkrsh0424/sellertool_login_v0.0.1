package com.example.seller_tool_login.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class UserEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    private String role;

    // @Column(name = "created_at", insertable = false, updatable = false)
    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @CreationTimestamp
    private Date updatedAt;

    @Column(name = "credential_created_at")
    @CreationTimestamp
    private Date credentialCreatedAt;

    @Column(name = "credential_expire_at")
    @CreationTimestamp
    private Date credentialExpireAt;

    @Column(name = "deleted")
    @GeneratedValue
    private int deleted;

}
