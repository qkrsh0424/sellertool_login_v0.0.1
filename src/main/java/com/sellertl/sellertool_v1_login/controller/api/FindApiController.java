package com.sellertl.sellertool_v1_login.controller.api;

import com.sellertl.sellertool_v1_login.model.DTO.FindEmailDTO;
import com.sellertl.sellertool_v1_login.model.service.EmailService;
import com.sellertl.sellertool_v1_login.model.service.FindService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FindApiController {
    @Autowired
    EmailService emailService;

    @Autowired
    FindService findService;

    @PostMapping(value = "/find/id")
    public String findIdWithEmail(@RequestBody FindEmailDTO dto){
        String result = findService.searchIdByEmail(dto.getEmail());
        if(result.equals("USER_NON")){
            return "{\"message\":\"user_non\"}";
        }
        return "{\"message\":\"success\"}";
    }

    @PostMapping(value = "/find/pw")
    public String findPwWithEmail(@RequestBody FindEmailDTO dto){
        // System.out.println("hello");
        String result = findService.searchPwByEmail(dto.getEmail());
        
        System.out.println(result);
        if(result.equals("USER_NON")){
            return "{\"message\":\"user_non\"}";
        }
        return "{\"message\":\"success\"}";
    }
}
