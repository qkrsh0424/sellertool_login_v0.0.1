package com.sellertl.sellertool_v1_login.controller.api;

import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sellertl.sellertool_v1_login.model.DTO.EmailDTO;
import com.sellertl.sellertool_v1_login.model.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class MailSendApiController {
    @Autowired
    EmailService emailService;

    @GetMapping(value = "/sendmail")
    public String getMethodName(@RequestParam("email") String email, HttpServletResponse response)
            throws UnsupportedEncodingException, MessagingException {
        // System.out.println(email);
        if(emailService.existUserEmail(email)){
            return "{\"message\":\"exist\"}";
        }
        emailService.send(email, response);
        return "{\"message\":\"success\"}";
    }
    
    @GetMapping(value = "/email/checkcode")
    public String checkEmailCode(@RequestParam("emailCode") String code, HttpServletRequest request,HttpServletResponse response){

        boolean result = emailService.isCodeValid(code, request);

        if(result){
            return "{\"message\":\"success\"}";
        }else{
            return "{\"message\":\"failure\"}";
        }
        
    }
}
