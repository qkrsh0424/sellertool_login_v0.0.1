package com.sellertl.sellertool_v1_login.model.repository;

import java.util.Optional;

import com.sellertl.sellertool_v1_login.model.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, String>{
    public UserEntity findByUsername(String username);
    public UserEntity findByEmail(String email);
    
    @Query(value = "SELECT * FROM user WHERE email=:email AND deleted=:isDeleted", nativeQuery=true)
    public Optional<UserEntity> findByEmail_Custom(String email, int isDeleted);

    @Query(value = "SELECT * FROM user WHERE username=:username AND deleted=:isDeleted", nativeQuery=true)
    public Optional<UserEntity> findByUsername_Custom(String username, int isDeleted);
}
