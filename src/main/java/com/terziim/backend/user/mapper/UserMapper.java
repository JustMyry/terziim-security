package com.terziim.backend.user.mapper;

import com.terziim.backend.user.dto.request.IcpEditUser;
import com.terziim.backend.user.dto.request.IcpEditUserPassword;
import com.terziim.backend.user.dto.response.IcpUserResponse;
import com.terziim.backend.user.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.factory.Mappers.getMapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = getMapper(UserMapper.class);



    IcpUserResponse getSelfAccountFromUser(AppUser appUser);


    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "profilePictureUrl", target = "profilePictureUrl")
    @Mapping(target = "relation", expression = "java(\"self\")")
    IcpUserResponse getEpAccountWithEmailFromUser(AppUser appUser);

    @Mapping(source = "bio", target = "bio")
    @Mapping(target = "email", expression = "java(null)")
    @Mapping(source = "profilePictureUrl", target = "profilePictureUrl")
    IcpUserResponse getEpAccountWithoutEmailFromUser(AppUser appUser);


    void transferEditRequestToUser(IcpEditUser icpEditUser, @MappingTarget AppUser appUser);

    @Mapping(source = "newPassword", target = "password")
    void transferPasswordRequestToUser(IcpEditUserPassword userPassword, @MappingTarget AppUser appUser);


}
