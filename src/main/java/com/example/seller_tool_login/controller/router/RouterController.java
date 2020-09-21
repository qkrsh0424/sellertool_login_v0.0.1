package com.example.seller_tool_login.controller.router;

import javax.servlet.http.HttpServletRequest;

import com.example.seller_tool_login.model.service.UserAuthService;
import com.example.seller_tool_login.model.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RouterController {
    @Autowired
    UserAuthService userAuthService;

    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;
    
    @GetMapping(value = "/")
    public String HomePage(HttpServletRequest request, Model model){
        model.addAttribute("data", userService.getUserInfo(request));
        return "view/index";
    }

    @GetMapping(value = "/login")
    public String LoginPage(HttpServletRequest request){
        System.out.println(request.getSession().getId());
        if(userAuthService.isUserSessionValid(request)){
            return "redirect:/";
        }
        return "view/login";
    }

    @GetMapping(value = "/signup")
    public String SignupPage(HttpServletRequest request){
        if(userAuthService.isUserSessionValid(request)){
            return "redirect:/";
        }
        return "view/signup";
    }
}
