package com.terziim.backend.user.service.impl;


import com.terziim.backend.security.jwt.JwtProvider;
import com.terziim.backend.user.model.AppUser;
import com.terziim.backend.user.repository.UserRepository;
import com.terziim.backend.user.service.UserExternalService;
import org.springframework.stereotype.Service;

@Service
public class UserExternalServiceImpl implements UserExternalService {


    private final UserRepository repository;
    private final JwtProvider jwtProvider;
    public UserExternalServiceImpl(UserRepository repository, JwtProvider jwtProvider){
        this.repository = repository;
        this.jwtProvider=jwtProvider;
    }


    @Override
    public AppUser findAppUserByUserId(String userId) {
        return repository.findAppUserByUserId(userId);
    }

    @Override
    public void saveUser(AppUser user){
        repository.save(user);
        return;
    }

    @Override
    public AppUser findUserByUsername(String username) {
        return repository.findAppUserByUsername(username);
    }

    @Override
    public AppUser findAppUserByEmail(String email) {
        return repository.findAppUserByEmail(email);
    }


    @Override
    public boolean isJwtBelongToUser(String userId, String jwt){
        return jwtProvider.getSubject(jwt).equals(userId);
    }

    @Override
    public boolean isUserBlocked(String blocked, String blockedBy) {
        return false;
    }


    @Override
    public AppUser getUserWithJwt(String jwt){
        return repository.findAppUserByUserId(jwtProvider.getSubject(jwt));
    }
}
