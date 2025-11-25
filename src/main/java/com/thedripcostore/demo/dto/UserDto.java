package com.thedripcostore.demo.dto;



import java.time.LocalDateTime;

public class UserDto {

 private Long id;
 private String name;
 private String phoneNumber;
 private String userId;
 private LocalDateTime registeredDateTime;
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