package com.sellertl.sellertool_v1_login.model.repository;

import java.util.Optional;

import com.sellertl.sellertool_v1_login.model.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String>{
    public UserEntity findByUsername(String username);
    public UserEntity findByEmail(String email);
}
