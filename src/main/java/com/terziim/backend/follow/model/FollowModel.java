package com.terziim.backend.follow.model;


import com.terziim.backend.icpmodel.BaseModel;
import jakarta.persistence.Entity;

@Entity
public class FollowModel extends BaseModel {


    private String followerId;
    private String followingId;
    private String isActive;



    //Getters and Setters
    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getFollowingId() {
        return followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
