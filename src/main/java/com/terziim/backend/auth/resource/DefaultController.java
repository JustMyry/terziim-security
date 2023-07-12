package com.terziim.backend.auth.resource;

import com.terziim.backend.auth.dto.IcpSignIn;
import com.terziim.backend.auth.dto.IcpSignUp;
import com.terziim.backend.auth.service.AuthService;
import com.terziim.backend.icpcommication.response.IcpResponseModel;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/")
public class DefaultController {

    private AuthService service;
    public DefaultController(AuthService service){
        this.service=service;
    }

    @PostMapping("/signin")
    IcpResponseModel<String> signIn(@RequestBody IcpSignIn request){
        return service.signInUser(request);
    }


    @PostMapping("/signup/user")
    IcpResponseModel<String> signUpUser(@RequestBody IcpSignUp request) throws MessagingException {
        return service.signUpUser(request);
    }

    @PostMapping("/signup/admin")
    IcpResponseModel<String> signUpAdmin(@RequestBody IcpSignUp request){
        return service.signUpAdmin(request);
    }


    @GetMapping("/activate/{userid}")
    IcpResponseModel<String> activateUser(@PathVariable String userid, @RequestParam String code){
        return service.activateUser(userid, code);
    }

}