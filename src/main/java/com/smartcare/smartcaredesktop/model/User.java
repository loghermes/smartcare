package com.smartcare.smartcaredesktop.model;

import java.time.LocalDateTime;

public class User {

    private Integer id;
    private String email;
    private String username;
    private String roles;
    private String password;

    private Boolean isActive;
    private Boolean isFirstLogin;

    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer doctorId;   // nullable
    private Integer patientId;  // nullable

    public User() {}

    public User(Integer id, String email, String username, String roles, String password,
                Boolean isActive, Boolean isFirstLogin,
                LocalDateTime lastLoginAt,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,
                Integer doctorId,
                Integer patientId) {

        this.id = id;
        this.email = email;
        this.username = username;
        this.roles = roles;
        this.password = password;
        this.isActive = isActive;
        this.isFirstLogin = isFirstLogin;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getIsFirstLogin() { return isFirstLogin; }
    public void setIsFirstLogin(Boolean isFirstLogin) { this.isFirstLogin = isFirstLogin; }

    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }

    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }
}