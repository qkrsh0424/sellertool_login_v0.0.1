package com.example.seller_tool_login.model.service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.example.seller_tool_login.model.DTO.UserLoginDTO;
import com.example.seller_tool_login.model.DTO.UserLoginSessionDTO;
import com.example.seller_tool_login.model.DTO.UserSignupDTO;
import com.example.seller_tool_login.model.entity.UserEntity;
import com.example.seller_tool_login.model.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

enum ROLE {
    ROLE_ADMIN, ROLE_USER, ROLE_MANAGER, ROLE_USERA, ROLE_USERB, ROLE_USERC, ROLE_USERP
}

@Service
public class UserAuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CredentialExtendService credentialExtendService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RedisTemplate redisTemplate;

    public String insertUserOne(UserSignupDTO userSignupDto) {
        UserEntity user = new UserEntity();

        // Create DB user identity UUID
        UUID uuid = UUID.randomUUID();
        // Create password salt UUID
        UUID uuidSalt = UUID.randomUUID();

        Calendar currentCalendar = Calendar.getInstance();
        Date currentDate = currentCalendar.getTime();

        String salt = uuidSalt.toString();
        // Encode password is String (input password string data of signup user +
        // created password of salt UUID).
        String encPassword = encoder.encode(userSignupDto.getPassword() + salt);

        user.setId(uuid.toString());
        user.setUsername(userSignupDto.getUsername());
        user.setPassword(encPassword);
        user.setSalt(salt);
        user.setEmail(userSignupDto.getEmail());
        user.setName(userSignupDto.getName());
        user.setRole(ROLE.ROLE_USER.toString());
        user.setCreatedAt(currentDate);
        user.setUpdatedAt(currentDate);
        user.setCredentialCreatedAt(currentDate);
        user.setCredentialExpireAt(currentDate);

        if (userRepository.save(user).getId() != null) {
            return "{\"message\":\"success\"}";
        } else {
            return "{\"message\":\"failure\"}";
        }
    }

    public Boolean checkUserLogin(UserLoginDTO userLoginDto, HttpServletRequest request){
        UserEntity user = userRepository.findByUsername(userLoginDto.getUsername());
        if(user==null){
            return false;
        }

        String mergePassword = userLoginDto.getPassword() + user.getSalt();
        if(encoder.matches(mergePassword, user.getPassword())){
            UserLoginSessionDTO sessionDataSet = setUserEntityToSessionDTO(user);
            redisTemplate.opsForValue().set("spring:session:sessions:expires:"+request.getSession().getId(), sessionDataSet);
            return true;
        }
        return false;
    }

    public Boolean isUserExist(String username){
        UserEntity user = userRepository.findByUsername(username);
        if(user != null){
            return true;
        }else {
            return false;
        }
    }

    public Boolean isUserSessionValid(HttpServletRequest request){
        Object session = redisTemplate.opsForValue().get("spring:session:sessions:expires:" + request.getSession().getId());
        
        if( session != null && session.getClass().getSimpleName().equals("UserLoginSessionDTO") ){
            UserLoginSessionDTO sessionData = (UserLoginSessionDTO) session;
            if(sessionData.getStatus().equals("loged")){
                return true;
            }
            return false;
        }
        return false;
    }

    // Handler
    private UserLoginSessionDTO setUserEntityToSessionDTO(UserEntity entity){
        UserLoginSessionDTO sessionData = new UserLoginSessionDTO();
        sessionData.setStatus("loged");
        sessionData.setId(entity.getId());
        sessionData.setUsername(entity.getUsername());
        sessionData.setEmail(entity.getEmail());
        sessionData.setName(entity.getName());
        sessionData.setRole(entity.getRole());
        sessionData.setCreatedAt(entity.getCreatedAt());
        sessionData.setUpdatedAt(entity.getUpdatedAt());
        sessionData.setCredentialCreatedAt(entity.getCredentialCreatedAt());
        sessionData.setCredentialExpireAt(entity.getCredentialExpireAt());
        sessionData.setDeleted(entity.getDeleted());
        return sessionData;
    }

    public void logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        redisTemplate.delete("spring:session:sessions:" + session.getId());
    }
}
