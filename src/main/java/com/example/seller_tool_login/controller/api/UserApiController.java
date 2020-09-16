package com.example.seller_tool_login.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.seller_tool_login.model.DTO.UserLoginDTO;
import com.example.seller_tool_login.model.DTO.UserSignupDTO;
import com.example.seller_tool_login.model.service.UserAuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserApiController {
    @Autowired
    UserAuthService userAuthService;

    @PostMapping(value = "/signup")
    public String SignupDo(@RequestBody UserSignupDTO userSignupDto, HttpServletRequest request){
        if(userAuthService.isUserExist(userSignupDto.getUsername())){
            return "{\"message\":\"exist\"}";
        }
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

    @PostMapping(value = "/logout")
    public void LogoutDo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        userAuthService.logout(request);
        response.sendRedirect("/");
    }

    @GetMapping(value = "/logout")
    public void LogoutGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect("/");
    }
}
