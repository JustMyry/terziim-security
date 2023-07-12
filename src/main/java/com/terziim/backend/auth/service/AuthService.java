package com.terziim.backend.auth.service;

import com.terziim.backend.auth.dto.IcpSignIn;
import com.terziim.backend.auth.dto.IcpSignUp;
import com.terziim.backend.icpcommication.response.IcpResponseModel;
import jakarta.mail.MessagingException;

public interface AuthService {

    IcpResponseModel<String> signUpUser(IcpSignUp request) throws MessagingException;

    IcpResponseModel<String> signUpAdmin(IcpSignUp request);

    IcpResponseModel<String> signInUser(IcpSignIn request);

    IcpResponseModel<String> activateUser(String userid, String code);
}

