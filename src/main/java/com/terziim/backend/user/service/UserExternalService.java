package com.terziim.backend.user.service;


import com.terziim.backend.user.model.AppUser;

public interface UserExternalService {


    AppUser findAppUserByUserId(String userId);

    void saveUser(AppUser user);

    AppUser findUserByUsername(String username);

    AppUser findAppUserByEmail(String email);

    boolean isJwtBelongToUser(String userId, String jwt);

    boolean isUserBlocked(String blocked, String blockedBy);

    public AppUser getUserWithJwt(String jwt);

}