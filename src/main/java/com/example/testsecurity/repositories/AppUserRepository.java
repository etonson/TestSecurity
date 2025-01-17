package com.example.testsecurity.repositories;

import com.example.testsecurity.entities.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
    @author : Eton.lin
    @description TODO
    @date 2025-01-12 下午 02:43
*/
@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {
    Optional<AppUserEntity> findByEmail(String email);

}