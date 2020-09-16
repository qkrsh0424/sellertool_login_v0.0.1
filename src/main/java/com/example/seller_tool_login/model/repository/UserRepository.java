package com.example.seller_tool_login.model.repository;

import com.example.seller_tool_login.model.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String>{
    public UserEntity findByUsername(String username);
}
