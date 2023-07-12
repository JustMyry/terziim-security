package com.terziim.backend.user.model;

import com.terziim.backend.authority.model.Authority;
import com.terziim.backend.icpmodel.BaseModel;
import jakarta.persistence.*;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;



@Entity
@ToString
public class AppUser extends BaseModel implements Serializable {


    @Column(nullable = false, updatable = false)
    private String userId;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    private String verificationCode;

    private String bio;
    private String profilePictureUrl;
    private String privacy;
    private String phone;
    private String address;
    private Float star;

    private Date lastLoginDate;
    private boolean isActive;
    private boolean isNotLocked;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "appuser_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Collection<Authority> authorities = new ArrayList<>();

    // Constructors
    public AppUser(){}

    public AppUser(String userId, String username, String email, String password, String verificationCode,
                   String bio, String profilePictureUrl, Date lastLoginDate, boolean isActive,
                   boolean isNotLocked, String phone, String address, Float star, String privacy, Collection<Authority> authorities) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.lastLoginDate = lastLoginDate;
        this.isActive = isActive;
        this.isNotLocked = isNotLocked;
        this.authorities = authorities;
        this.privacy = privacy;
        this.phone = phone;
        this.address = address;
        this.star = star;
    }


    // Getters and Setterss
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getUserBio() {
        return bio;
    }

    public void setUserBio(String bio) { this.bio = bio; }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isNotLocked() {
        return isNotLocked;
    }

    public void setNotLocked(boolean notLocked) {
        isNotLocked = notLocked;
    }


    public Collection<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getPrivacy() { return privacy; }

    public void setPrivacy(String privacy) { this.privacy = privacy; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getStar() {
        return star;
    }

    public void setStar(Float star) {
        this.star = star;
    }
}