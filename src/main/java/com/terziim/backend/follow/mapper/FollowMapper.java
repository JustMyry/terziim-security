package com.terziim.backend.follow.mapper;

import com.terziim.backend.user.dto.response.IcpUserResponse;
import com.terziim.backend.user.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.factory.Mappers.getMapper;


@Mapper(componentModel = "spring")
public interface FollowMapper {


    FollowMapper INSTANCE = getMapper(FollowMapper.class);



    IcpUserResponse getSelfAccountFromUser(AppUser appUser);


    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "profilePictureUrl", target = "profilePictureUrl")
    @Mapping(target = "relation", expression = "java(\"self\")")
    IcpUserResponse getEpAccountWithEmailFromUser(AppUser appUser);

}
