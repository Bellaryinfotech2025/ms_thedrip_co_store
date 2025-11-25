package com.thedripcostore.demo.entity;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "drip_co_users")
public class UserEntity {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Column(name = "name", nullable = false)
 private String name;

 @Column(name = "phone_number", nullable = false)
 private String phoneNumber;

 @Column(name = "user_id", unique = true, nullable = false, length = 6)
 private String userId;

 @Column(name = "registered_date_time", nullable = false)
 private LocalDateTime registeredDateTime;

 @Column(name = "status", nullable = false, length = 20)
 private String status;

 public Long getId() {
     return id;
 }

 public void setId(Long id) {
     this.id = id;
 }

 public String getName() {
     return name;
 }

 public void setName(String name) {
     this.name = name;
 }

 public String getPhoneNumber() {
     return phoneNumber;
 }

 public void setPhoneNumber(String phoneNumber) {
     this.phoneNumber = phoneNumber;
 }

 public String getUserId() {
     return userId;
 }

 public void setUserId(String userId) {
     this.userId = userId;
 }

 public LocalDateTime getRegisteredDateTime() {
     return registeredDateTime;
 }

 public void setRegisteredDateTime(LocalDateTime registeredDateTime) {
     this.registeredDateTime = registeredDateTime;
 }

 public String getStatus() {
     return status;
 }

 public void setStatus(String status) {
     this.status = status;
 }
}