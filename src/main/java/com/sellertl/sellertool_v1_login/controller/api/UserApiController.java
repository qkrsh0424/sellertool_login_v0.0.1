package com.sellertl.sellertool_v1_login.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sellertl.sellertool_v1_login.model.DTO.UserLoginDTO;
import com.sellertl.sellertool_v1_login.model.DTO.UserSignupDTO;
import com.sellertl.sellertool_v1_login.model.service.EmailService;
import com.sellertl.sellertool_v1_login.model.service.UserAuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserApiController {
    @Value("${app.environment}")
    private String myEnvironment;

    @Value("${app.environment.development.main.url}")
    private String myEnvDevMainUrl;

    @Value("${app.environment.production.main.url}")
    private String myEnvProdMainUrl;
    
    @Autowired
    UserAuthService userAuthService;

    @Autowired
    EmailService emailService;

    @PostMapping(value = "/signup")
    public String SignupDo(@RequestBody UserSignupDTO userSignupDto, HttpServletRequest request){
        if(emailService.authFailure(request)){
            return "{\"message\":\"accessDenied\"}";
        }
        if(userAuthService.isUserExist(userSignupDto.getUsername())){
            return "{\"message\":\"exist\"}";
        }
        emailService.emailCodeInvalid(request);
        return userAuthService.insertUserOne(userSignupDto);
    }

    @PostMapping(value = "/login")
    public String LoginDo(@RequestBody UserLoginDTO userLoginDto, HttpServletRequest request, HttpServletResponse response){
        if(userAuthService.isUserSessionValid(request)){
            return "{\"message\":\"error\"}";
        }
        if(userAuthService.checkUserLogin(userLoginDto, request)){
            return "{\"message\":\"success\"}";
        }
        return "{\"message\":\"failure\"}";
    }

    // TODO LOGOUT AJAX 로 넘기기
    @PostMapping(value = "/logout")
    public void LogoutDo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        userAuthService.logout(request);
        if(myEnvironment.equals("production")){
            response.sendRedirect(myEnvProdMainUrl);
        } else{
            response.sendRedirect(myEnvDevMainUrl);
        }
    }

    @GetMapping(value = "/logout")
    public void LogoutGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect("/");
    }
}
