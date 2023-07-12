package com.terziim.backend.follow.repository;


import com.terziim.backend.follow.model.FollowModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<FollowModel, Long> {

    FollowModel findFollowTableById(String id);


    @Query(value = "SELECT * FROM follow_model u WHERE u.follower=:followerId AND u.following=:followingId", nativeQuery = true)
    FollowModel findFollowTableByFollowerAndFollowing(String followerId, String followingId);


    @Query(value = "SELECT * FROM follow_model u WHERE u.follower=:followerId AND u.is_active = true", nativeQuery = true)
    List<FollowModel> findFollowTablesByFollower(String followerId);


    @Query(value = "SELECT * FROM follow_model u WHERE u.following=:followingId AND u.is_active = true", nativeQuery = true)
    List<FollowModel> findFollowTablesByFollowing(String followingId);


    @Query(value = "SELECT * FROM follow_model u WHERE u.follower=:followerId AND u.is_active = true limit :limit offset :offset ", nativeQuery = true)
    List<FollowModel> findFollowTablesByFollowerWithLO(@Param("followerId") String userId, @Param("limit") int limit, @Param("offset") int offset);


    @Query(value = "SELECT * FROM follow_model u WHERE u.following=:following AND u.is_active = true limit :limit offset :offset ", nativeQuery = true)
    List<FollowModel> findFollowTablesByFollowingWithLO(@Param("following") String userId, @Param("limit") int limit, @Param("offset") int offset);

}
