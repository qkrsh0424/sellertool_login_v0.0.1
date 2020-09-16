package com.example.seller_tool_login.model.service;

import javax.servlet.http.HttpServletRequest;

import com.example.seller_tool_login.model.DTO.UserLoginSessionDTO;
import com.example.seller_tool_login.model.VO.UserInfoVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    UserAuthService userAuthService;

    public UserInfoVO getUserInfo(HttpServletRequest request){
        if(userAuthService.isUserSessionValid(request)){
            UserLoginSessionDTO userLoginSessionDTO = (UserLoginSessionDTO) redisTemplate.opsForValue().get("spring:session:sessions:expires:" + request.getSession().getId());
            return getUserSessionDtoToVo(userLoginSessionDTO);
        }
        return null;
    }

    public UserInfoVO getUserSessionDtoToVo(UserLoginSessionDTO userSessionData){
        UserInfoVO user = new UserInfoVO();
        user.setUsername(userSessionData.getUsername());
        user.setEmail(userSessionData.getEmail());
        user.setName(userSessionData.getName());
        user.setRole(userSessionData.getRole());
        user.setCreatedAt(userSessionData.getCreatedAt());
        user.setUpdatedAt(userSessionData.getUpdatedAt());
        user.setCredentialCreatedAt(userSessionData.getCredentialCreatedAt());
        user.setCredentialExpireAt(userSessionData.getCredentialExpireAt());
        return user;
    }
}
