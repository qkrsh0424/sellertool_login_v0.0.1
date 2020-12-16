package com.sellertl.sellertool_v1_login.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sellertl.sellertool_v1_login.model.VO.UserInfoVO;
import com.sellertl.sellertool_v1_login.model.service.EmailService;
import com.sellertl.sellertool_v1_login.model.service.UserAuthService;
import com.sellertl.sellertool_v1_login.model.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authapi")
public class UserAPI2 {
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

    @Autowired
    UserService userService;
    @PostMapping(value = "/logout")
    public String LogoutDo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        userAuthService.logout(request);
        return "{\"message\":\"SUCCESS\"}";
    }

    // test
    @GetMapping(value = "/authenticate")
    public String UserAuthenticate(HttpServletRequest request){
        UserInfoVO user = userService.getUserInfo(request);
        return "test";
    }

    @GetMapping(value = "/logout")
    public void LogoutDoTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        userAuthService.logout(request);
    }
}
