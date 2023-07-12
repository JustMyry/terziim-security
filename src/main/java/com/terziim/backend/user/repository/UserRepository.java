package com.terziim.backend.user.repository;

import com.terziim.backend.user.model.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {


    @Query(
            value = "SELECT * FROM AppUser ORDER BY id",
            countQuery = "SELECT count(*) FROM Users",
            nativeQuery = true)
    Page<AppUser> findAllUsersWithPagination(Pageable pageable);

    AppUser findAppUserByUserId(String id);
    AppUser findAppUserById(Long id);
    AppUser findAppUserByUsername(String username);
    AppUser findAppUserByEmail(String email);



}