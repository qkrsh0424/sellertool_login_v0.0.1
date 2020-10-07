package com.sellertl.sellertool_v1_login.model.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.mail.MessagingException;

import com.sellertl.sellertool_v1_login.model.entity.UserEntity;
import com.sellertl.sellertool_v1_login.model.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FindService {

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    ConvertService convert;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder encoder;

    public String searchIdByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            return "USER_NON";
        }

        String formatedUsername = getIdEncodeFormat(user.getUsername());

        try {
            emailService.sendFindId(email, formatedUsername);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "SUCCESS";
    }

    public String searchPwByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            return "USER_NON";
        }

        Optional<UserEntity> selectedUser = userRepository.findById(user.getId());
        String formatedNewPassword = getNewPassword();

        selectedUser.ifPresent(u -> {
            String salt = UUID.randomUUID().toString();
            String encPassword = encoder.encode(formatedNewPassword + salt);
            u.setPassword(encPassword);
            u.setSalt(salt);
            userRepository.save(u);
        });

        try {
            emailService.sendFindPw(email, formatedNewPassword);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "SUCCESS";
    }

    public String getIdEncodeFormat(String username){
        String formatUsername = "";
        for(int i = 0 ; i < username.length(); i++){
            if(i==0 || i==1 || i==2 || i==username.length()-1){
                formatUsername += username.charAt(i);
            }else{
                formatUsername += "*";
            }
        }
        return formatUsername;
    }

    public String getNewPassword(){
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 20; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
            case 0:
                // a-z
                temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                break;
            case 1:
                // A-Z
                temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                break;
            case 2:
                // 0-9
                temp.append((rnd.nextInt(10)));
                break;
            }
        }
        return temp.toString();
    }
}
