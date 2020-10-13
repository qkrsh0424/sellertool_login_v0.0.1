package com.sellertl.sellertool_v1_login.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

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

    @Nullable
    @Column(name = "user_url")
    private String userUrl;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    private String role;

    // @Column(name = "created_at", insertable = false, updatable = false)
    @Column(name = "created_at")
    // @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    // @CreationTimestamp
    private Date updatedAt;

    @Column(name = "credential_created_at")
    // @CreationTimestamp
    private Date credentialCreatedAt;

    @Column(name = "credential_expire_at")
    // @CreationTimestamp
    private Date credentialExpireAt;

    @Column(name = "deleted")
    private int deleted;

}
