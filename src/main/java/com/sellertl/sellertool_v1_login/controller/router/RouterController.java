package com.sellertl.sellertool_v1_login.controller.router;

import javax.servlet.http.HttpServletRequest;

import com.sellertl.sellertool_v1_login.model.service.FindService;
import com.sellertl.sellertool_v1_login.model.service.UserAuthService;
import com.sellertl.sellertool_v1_login.model.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RouterController {
    @Value("${app.environment}")
    private String myEnvironment;

    @Value("${app.environment.development.main.url}")
    private String myEnvDevMainUrl;

    @Value("${app.environment.production.main.url}")
    private String myEnvProdMainUrl;

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    FindService findService;
    
    @GetMapping(value = "/")
    public String HomePage(HttpServletRequest request, Model model){
        model.addAttribute("data", userService.getUserInfo(request));
        if(myEnvironment.equals("production")){
            return "redirect:"+myEnvProdMainUrl;
        }else{
            return "redirect:"+myEnvDevMainUrl;
        }
        // return "view/index";
    }

    @GetMapping(value = "/login")
    public String LoginPage(HttpServletRequest request){
        // System.out.println(request.getSession().getId());
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

    @GetMapping(value = "/find/id")
    public String FindIdPage(HttpServletRequest request){
        if(userAuthService.isUserSessionValid(request)){
            return "redirect:/";
        }
        return "view/findid";
    }

    @GetMapping(value = "/find/pw")
    public String FindPwPage(HttpServletRequest request){
        // System.out.println(findService.getNewPassword());
        if(userAuthService.isUserSessionValid(request)){
            return "redirect:/";
        }
        return "view/findpw";
    }

    @GetMapping(value = "/mail/sended")
    public String SendedMailPage(HttpServletRequest request){
        return "view/mailsended";
    }

    @GetMapping(value = "/error")
    public String Error404Page(){
        return "view/page404";
    }
}
