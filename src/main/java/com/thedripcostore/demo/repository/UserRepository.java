package com.thedripcostore.demo.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thedripcostore.demo.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
 Optional<UserEntity> findByUserId(String userId);
 
 List<UserEntity> findAll();
}