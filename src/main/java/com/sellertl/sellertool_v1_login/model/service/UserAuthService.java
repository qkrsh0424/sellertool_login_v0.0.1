package com.sellertl.sellertool_v1_login.model.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sellertl.sellertool_v1_login.model.DTO.UserLoginDTO;
import com.sellertl.sellertool_v1_login.model.DTO.UserLoginSessionDTO;
import com.sellertl.sellertool_v1_login.model.DTO.UserSignupDTO;
import com.sellertl.sellertool_v1_login.model.entity.UserEntity;
import com.sellertl.sellertool_v1_login.model.repository.UserRepository;
import com.sellertl.sellertool_v1_login.model.type.DeletedType;

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

    @Autowired
    ConvertService convert;
    
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

    public Boolean checkUserLogin(UserLoginDTO userLoginDto, HttpServletRequest request) {
        // ** OLD TEST_TODO V1
        // UserEntity user = userRepository.findByUsername(userLoginDto.getUsername());
        // if (user == null) {
        //     return false;
        // }

        // String mergePassword = userLoginDto.getPassword() + user.getSalt();

        // if (encoder.matches(mergePassword, user.getPassword())) {
        //     UserLoginSessionDTO sessionDataSet = setUserEntityToSessionDTO(user);
        //     String dto2String = convert.objectClass2JsonStringConvert(sessionDataSet);
        //     redisTemplate.opsForValue().set("spring:session:sessions:expires:" + request.getSession().getId(),dto2String);
        //     return true;
        // }
        // return false;

        // ** NEW V1
        Optional<UserEntity> userOpt = userRepository.findByUsername_Custom(userLoginDto.getUsername(), DeletedType.EXIST);
        if (userOpt.isEmpty()) {
            return false;
        }
        UserEntity user = userOpt.get();

        String mergePassword = userLoginDto.getPassword() + user.getSalt();
        
        if (encoder.matches(mergePassword, user.getPassword())) {
            UserLoginSessionDTO sessionDataSet = setUserEntityToSessionDTO(user);
            String dto2String = convert.objectClass2JsonStringConvert(sessionDataSet);
            redisTemplate.opsForValue().set("spring:session:sessions:expires:" + request.getSession().getId(),dto2String);
            return true;
        }
        return false;
    }

    public Boolean isUserExist(String username) {
        // ** OLD TEST_TODO V1
        // UserEntity user = userRepository.findByUsername(username);
        // if (user != null) {
        //     return true;
        // } else {
        //     return false;
        // }

        // ** NEW V1
        Optional<UserEntity> userOpt = userRepository.findByUsername_Custom(username,DeletedType.EXIST);
        if (userOpt.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isUserSessionValid(HttpServletRequest request){

        // **Original
        // Object session = redisTemplate.opsForValue().get("spring:session:sessions:expires:" + request.getSession().getId());
        
        // if( session != null && session.getClass().getSimpleName().equals("UserLoginSessionDTO") ){
        //     UserLoginSessionDTO sessionData = (UserLoginSessionDTO) session;
        //     if(sessionData.getStatus().equals("loged")){
        //         return true;
        //     }
        //     return false;
        // }
        // return false;

        // **Test OK**
        String session = (String) redisTemplate.opsForValue().get("spring:session:sessions:expires:" + request.getSession().getId());
        // System.out.println(session.isEmpty());
        if( session != null && !session.isEmpty() ){
            // System.out.println("UserAuthService/isUserSessionValid : hello");
            UserLoginSessionDTO sessionData = (UserLoginSessionDTO) convert.jsonString2ObjectClassConvert(session, UserLoginSessionDTO.class);
            // System.out.println(sessionData.getStatus());
            if(sessionData.getStatus().equals("loged")){
                return true;
            }
            return false;
        }
        return false;


        // TestCode
        // ObjectMapper mapper = new ObjectMapper();

        // Object session = redisTemplate.opsForValue().get("spring:session:sessions:expires:" + request.getSession().getId());
        // UserLoginSessionDTO sessionData = (UserLoginSessionDTO) session;
        // System.out.println(sessionData);

        // String str=null;
        // try {
        //     str = mapper.writeValueAsString(sessionData);
        // } catch (JsonProcessingException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // System.out.println(str);
        
        // UserLoginSessionDTO readValue = new UserLoginSessionDTO();
        // try {
        //     readValue = mapper.readValue(str, UserLoginSessionDTO.class);
        // } catch (JsonMappingException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // } catch (JsonProcessingException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }

        // System.out.println(readValue);

        // TestCode2
        // Object session = redisTemplate.opsForValue().get("spring:session:sessions:expires:" + request.getSession().getId());
        // UserLoginSessionDTO sessionData = (UserLoginSessionDTO) session;

        // String str = convert.objectClass2JsonStringConvert(sessionData);
        // System.out.println(str);

        // UserLoginSessionDTO test = (UserLoginSessionDTO) convert.jsonString2ObjectClassConvert(str, UserLoginSessionDTO.class);
        // System.out.println(test);
        // return false;
    }

    // Handler
    private UserLoginSessionDTO setUserEntityToSessionDTO(UserEntity entity){
        UserLoginSessionDTO sessionData = new UserLoginSessionDTO();
        sessionData.setStatus("loged");
        sessionData.setId(entity.getId());
        sessionData.setUsername(entity.getUsername());
        sessionData.setEmail(entity.getEmail());
        sessionData.setUserUrl(entity.getUserUrl());
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
        // **TODO**
        // **Origin
        // HttpSession session = request.getSession();
        // session.invalidate();
        // redisTemplate.delete("spring:session:sessions:" + session.getId());

        // **TEST OK
        HttpSession session = request.getSession();
        session.invalidate();
        redisTemplate.delete("spring:session:sessions:" + session.getId());
    }
}
