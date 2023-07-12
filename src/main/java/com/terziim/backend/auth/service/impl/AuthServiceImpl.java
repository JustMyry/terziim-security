package com.terziim.backend.auth.service.impl;

import com.terziim.backend.auth.ActivationCode;
import com.terziim.backend.auth.constants.SignConstantsCode;
import com.terziim.backend.auth.constants.SignConstantsMessage;
import com.terziim.backend.auth.dto.IcpSignIn;
import com.terziim.backend.auth.dto.IcpSignUp;
import com.terziim.backend.auth.repository.ActivationCodeRepository;
import com.terziim.backend.auth.service.AuthService;
import com.terziim.backend.authority.model.Authority;
import com.terziim.backend.authority.repository.AuthRepository;
import com.terziim.backend.email.EmailProvider;
import com.terziim.backend.exception.CustomException;
import com.terziim.backend.exception.StatusCode;
import com.terziim.backend.exception.StatusMessage;
import com.terziim.backend.icpcommication.response.IcpResponseModel;
import com.terziim.backend.icpcommication.response.IcpResponseStatus;
import com.terziim.backend.security.jwt.JwtProvider;
import com.terziim.backend.security.model.AppUserPrincipal;
import com.terziim.backend.user.model.AppUser;
import com.terziim.backend.user.service.impl.UserExternalServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.terziim.backend.auth.constants.SignConstantsCode.AUTH_SERVICE_INTERNAL_ERROR;
import static com.terziim.backend.auth.constants.SignConstantsMessage.*;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserExternalServiceImpl userService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final ActivationCodeRepository activationCodeRepository;
    private final EmailProvider emailProvider;
    public AuthServiceImpl(UserExternalServiceImpl userService, ModelMapper modelMapper, PasswordEncoder passwordEncoder, AuthRepository authRepository,
                           JwtProvider jwtProvider, AuthenticationManager authenticationManager, ActivationCodeRepository activationCodeRepository,
                           EmailProvider emailProvider){
        this.userService=userService;
        this.modelMapper=modelMapper;
        this.passwordEncoder=passwordEncoder;
        this.authRepository=authRepository;
        this.jwtProvider=jwtProvider;
        this.authenticationManager=authenticationManager;
        this.activationCodeRepository=activationCodeRepository;
        this.emailProvider=emailProvider;
    }





    @Override
    public IcpResponseModel<String> signUpUser(IcpSignUp request) throws MessagingException {
        if(!isRequestUnique(request.getUsername(), request.getEmail())){
            return IcpResponseModel.<String>builder()
                    .response(null)
                    .status(IcpResponseStatus.builder().message(USERNAME_ALREADY_EXISTS).code(AUTH_SERVICE_INTERNAL_ERROR).build())
                    .build();
        }
        AppUser user = modelMapper.map(request, AppUser.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        setRoleToUser(user, USER);
        user.setUserId(generateUserId());
        user.setNotLocked(true);
        String activationCode = String.valueOf((int)(Math.random()*(10000-1+1)+1));
        user.setVerificationCode(activationCode);
        userService.saveUser(user);
        createActivationCode(activationCode, user.getUserId(), new Date(System.currentTimeMillis() + EXPIRATION_TIME_FOR_ACTIVATION_CODE));
        //emailProvider.sendActivationEmail(user.getUserId(), activationCode, user.getEmail());
        IcpResponseModel<String> build = IcpResponseModel.<String>builder()
                .response(null)
                .status(IcpResponseStatus.builder().message(SignConstantsMessage.EMAIL_SENT).code(SignConstantsCode.EMAIL_SENT).build())
                .build();
        return build;
    }




    @Override
    public IcpResponseModel<String> signUpAdmin(IcpSignUp request) {
        if(!isRequestUnique(request.getUsername(), request.getEmail())){
            return IcpResponseModel.<String>builder()
                    .response(null)
                    .status(IcpResponseStatus.builder().message(USERNAME_ALREADY_EXISTS).code(AUTH_SERVICE_INTERNAL_ERROR).build())
                    .build();
        }
        AppUser user = modelMapper.map(request, AppUser.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        setRoleToUser(user, ADMIN);
        user.setUserId(generateUserId());
        user.setNotLocked(true);
        userService.saveUser(user);
        AppUserPrincipal principal = new AppUserPrincipal(user);
        String token = jwtProvider.generateJWT(principal);
        return IcpResponseModel.<String>builder()
                .response(token)
                .status(IcpResponseStatus.getSuccess())
                .build();
    }



    @Override
    @Transactional
    public IcpResponseModel<String> signInUser(IcpSignIn request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            String response = jwtProvider.generateJWT(getPrincipal(request.getUsername()));
            return IcpResponseModel.<String>builder()
                    .response(response)
                    .status(IcpResponseStatus.getSuccess())
                    .build();
        }catch (Exception ex){
            System.out.println(ex);
            throw new CustomException(StatusMessage.USERNAME_OR_PASSWORD_IS_INVALID, StatusCode.USERNAME_OR_PASSWORD_IS_INVALID);
        }
    }

    @Override
    public IcpResponseModel<String> activateUser(String userid, String code) {
        if(!verifyActivationCode(userid, code)){
            return IcpResponseModel.<String>builder()
                    .response(null)
                    .status(IcpResponseStatus.builder().message(ACTIVATION_CODE_IS_NOT_CORRECT).code(SignConstantsCode.ACTIVATION_CODE_IS_NOT_CORRECT).build())
                    .build();
        }
        AppUserPrincipal principal = getPrincipalwithUserid(userid);
        String token = jwtProvider.generateJWT(principal);
        return IcpResponseModel.<String>builder()
                .response(token)
                .status(IcpResponseStatus.getSuccess())
                .build();
    }

    private boolean verifyActivationCode(String userid, String code) {
        ActivationCode activationCode = activationCodeRepository.findActivationCodeByUserName(userid);
        if(activationCode != null && activationCode.getCode().equals(code))
            return true;
        else
            return false;
    }


    // USER qeydiyyatdan kecerken bir defeye ozel cagirilir.
    // Meqsed qeydiyyatdan kecen USERe DBdan elaqesiz bir ID vermekdir
    // Burdaki meqsed ise USERin DB IDsinin tehlukesizlik meqsedi ile FrontEnd e bildirmemekdir.
    private String generateUserId(){
        return DateTimeFormatter.ISO_INSTANT.format(LocalDateTime.now().toInstant(ZoneOffset.UTC)).substring(2)
                .replace("T", "").replace("-", "").replace(":","").replace(".","");
    }


    // USER obyektinden UserDetails obyekti almaq ucun
    private AppUserPrincipal getPrincipal(String username){
        return new AppUserPrincipal(userService.findUserByUsername(username));
    }


    // USER obyektinin IDsinden UserDetails obyekti almaq ucun
    private AppUserPrincipal getPrincipalwithUserid(String userId){
        return new AppUserPrincipal(userService.findAppUserByUserId(userId));
    }


    //Qeydiyyat isteyinde gelen 'username' in uygun olub olmadigini yoxlayiriq
    private boolean isRequestUnique(String username, String email) {
        System.out.println(userService.findUserByUsername(username));
        return userService.findUserByUsername(username)==null && userService.findAppUserByEmail(email)==null;
    }


    //Gonderilen 'User'e gonderilen adda 'Role'u DBdan cekib verir
    private void setRoleToUser(AppUser user, String role) {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authRepository.findByAuthority(role));
        user.setAuthorities(authorities);
    }


    //Istifadeci hesabini aktiv etsin deye aktivasiya kodu hazirlayir
    private void createActivationCode(String activationCode, String userid, Date date) {
        ActivationCode code = new ActivationCode(activationCode, userid, date);
        activationCodeRepository.save(code);
    }

}
