package com.sellertl.sellertool_v1_login.model.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sellertl.sellertool_v1_login.model.DTO.EmailDTO;
import com.sellertl.sellertool_v1_login.model.DTO.SignupCodeDTO;
import com.sellertl.sellertool_v1_login.model.entity.UserEntity;
import com.sellertl.sellertool_v1_login.model.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${aws.ses.port}")
	private String smtpPort;
	
	@Value("${aws.ses.host}")
	private String sesHost;
	
	@Value("${aws.ses.username}")
	private String sesUsername;
	
	@Value("${aws.ses.password}")
    private String sesPassword;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConvertService convert;
    // ******************
    // TODO ALL***********
    // *******************
    public void sendFindId(String receiveAddress, String username) throws MessagingException, UnsupportedEncodingException {
        Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", smtpPort); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");
    	
        Session session = Session.getDefaultInstance(props);
        
        EmailDTO email = setFindIdEmail(receiveAddress, username);
    	
    	// System.out.println(email.getSender());
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(email.getSender(),"sellertool"));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getReceiver()));
        msg.setSubject(email.getTitle());
        msg.setContent(email.getContent(),"text/html; charset=UTF-8");

        //msg.setHeader("X-SES-CONFIGURATION-SET", "ConfigSet");
        
        Transport transport = session.getTransport();
        try{
            transport.connect(sesHost, sesUsername, sesPassword);	
            transport.sendMessage(msg, msg.getAllRecipients());

        }
        catch (Exception ex) {
            System.out.println("Error message: " + ex.getMessage());
        }
        finally{
            transport.close();
        }
    }

    public void sendFindPw(String receiveAddress, String newPassword) throws MessagingException, UnsupportedEncodingException {
        Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", smtpPort); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");
    	
        Session session = Session.getDefaultInstance(props);
        
        EmailDTO email = setFindPwEmail(receiveAddress, newPassword);
    	
    	// System.out.println(email.getSender());
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(email.getSender(),"sellertool"));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getReceiver()));
        msg.setSubject(email.getTitle());
        msg.setContent(email.getContent(),"text/html; charset=UTF-8");

        //msg.setHeader("X-SES-CONFIGURATION-SET", "ConfigSet");
        
        Transport transport = session.getTransport();
        try{
            transport.connect(sesHost, sesUsername, sesPassword);	
            transport.sendMessage(msg, msg.getAllRecipients());

        }
        catch (Exception ex) {
            System.out.println("Error message: " + ex.getMessage());
        }
        finally{
            transport.close();
        }
    }

    public void send(String receiveAddress, HttpServletResponse response) throws MessagingException, UnsupportedEncodingException {
        Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", smtpPort); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");
    	
        Session session = Session.getDefaultInstance(props);
        
        String code = setRedisCookieAndGetAuthCode(response);
        EmailDTO email = setSignupEmail(receiveAddress, code);
        

    	
    	// System.out.println(email.getSender());
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(email.getSender(),"sellertool"));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getReceiver()));
        msg.setSubject(email.getTitle());
        msg.setContent(email.getContent(),"text/html; charset=UTF-8");

        //msg.setHeader("X-SES-CONFIGURATION-SET", "ConfigSet");
        
        Transport transport = session.getTransport();
        try{
            transport.connect(sesHost, sesUsername, sesPassword);	
            transport.sendMessage(msg, msg.getAllRecipients());

        }
        catch (Exception ex) {
            System.out.println("Error message: " + ex.getMessage());
        }
        finally{
            transport.close();
        }
    }

    private String setRedisCookieAndGetAuthCode(HttpServletResponse response){
        // **Origin**
        // String key = UUID.randomUUID().toString();

        // SignupCodeDTO code = new SignupCodeDTO();
        // code.setCode(getSignupAuthCode());
        // redisTemplate.opsForValue().set(key,code,5,TimeUnit.MINUTES);

        // setCookie(key,response);

        // return code.getCode();

        // **TEST OK**
        String key = UUID.randomUUID().toString();

        SignupCodeDTO code = new SignupCodeDTO();
        code.setCode(getSignupAuthCode());
        String codeDto2Str = convert.objectClass2JsonStringConvert(code);
        redisTemplate.opsForValue().set(key,codeDto2Str,5,TimeUnit.MINUTES);

        setCookie(key,response);

        return code.getCode();
    }

    private String getSignupAuthCode(){
        return String.valueOf((int) ((Math.random()*100000)+100000));
    }
    
    public void setCookie(String key, HttpServletResponse response){
        Cookie myCookie = new Cookie("MAILAUTH", key);
        myCookie.setMaxAge(10*60);
        myCookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(myCookie);
    }

    public EmailDTO setSignupEmail(String receiveAddress, String code){
        String title = "셀러툴 회원가입 인증 메일 입니다.";
        String sendAddress = "sellertool@sellertl.com";
        StringBuilder content = new StringBuilder();
        // content.append(
        //                 "<!DOCTYPE html>"+
        //                 "<html lang=\"ko\">"+
        //                 "<head>"+
        //                     "<meta charset=\"UTF-8\">"+
        //                     "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"+
        //                 "</head>"+
        //                 "<body>"+
        //                     "<div style=\"text-align:center;\">"+
        //                         "<h1>안녕하세요 셀러툴 입니다. 셀러툴을 이용해 주셔서 감사합니다.</h1>"+
        //                         "<h1>회원가입 인증 코드는 </h3>"+
        //                         "<h1 style=\"color:red;\"><strong>"+code+"</strong></h1>"+
        //                         "<h1>입니다.</h1>"+
        //                     "</div>"+
        //                 "</body>"+
        //                 "</html>"
        //             );
        content.append(
            "<!DOCTYPE html>"+
            "<html lang=\"ko\">"+
                "<head>"+
                    "<link href=\"https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;500&display=swap\" rel=\"stylesheet\">"+
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />"+
                "</head>"+
                "<body style=\"background-color: white;\">"+
                    "<main>"+
                        "<div style=\"padding-top: 50px; background-color: white; font-family: 'Noto Sans KR', sans-serif; width: 70%; margin: auto;\">"+
                            "<h1 class=\"mail_header\">Seller Tool</h1>"+
                            "<div style=\"border: 9px solid #164773; padding: 20px 15px; text-align: center;\">"+
                                "<p>안녕하세요, 판매자님</p>"+
                                "<p>셀러툴의 가입을 완료하기 위해 아래의 인증번호를</p>"+
                                "<p>인증번호 입력창에 입력해 주세요.</p>"+
                                "<p style=\"text-align: center; font-size: 28px; font-weight: 600;\">"+code+"</p>"+
                                "<div style=\"opacity: 0.6; font-size: 12px;\">"+
                                    "<p>계정 보호를 위해 이 이메일을 전달하거나 다른 사람에게 공유하지 마세요.</p>"+
                                "</div>"+
                            "</div>"+
                            "<div style=\"opacity: 0.4;\">"+
                                "<p>@2020 SellerTool.</p>"+
                            "</div>"+
                        "</div>"+
                    "</main>"+
                "</body>"+
            "</html>"
        );
        EmailDTO email = new EmailDTO(title,content.toString(),sendAddress,receiveAddress);
        return email;
    }

    public EmailDTO setFindIdEmail(String receiveAddress, String username){
        String title = "셀러툴 아이디 찾기 메일 입니다.";
        String sendAddress = "sellertool@sellertl.com";
        StringBuilder content = new StringBuilder();
        // content.append(
        //                 "<!DOCTYPE html>"+
        //                 "<html lang=\"ko\">"+
        //                 "<head>"+
        //                     "<meta charset=\"UTF-8\">"+
        //                     "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"+
        //                 "</head>"+
        //                 "<body>"+
        //                     "<div style=\"text-align:center;\">"+
        //                         "<h1>안녕하세요 셀러툴 입니다. 셀러툴을 이용해 주셔서 감사합니다.</h1>"+
        //                         "<h1>회원님의 아이디는 </h3>"+
        //                         "<h1 style=\"color:red;\"><strong>"+username+"</strong></h1>"+
        //                         "<h1>입니다.</h1>"+
        //                     "</div>"+
        //                 "</body>"+
        //                 "</html>"
        //             );        
        content.append(
            "<!DOCTYPE html>"+
            "<html lang=\"ko\">"+
                "<head>"+
                    "<link href=\"https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;500&display=swap\" rel=\"stylesheet\">"+
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />"+
                "</head>"+
                "<body style=\"background-color: white;\">"+
                    "<main>"+
                        "<div style=\"padding-top: 50px; background-color: white; font-family: 'Noto Sans KR', sans-serif; width: 70%; margin: auto;\">"+
                            "<h1 class=\"mail_header\">Seller Tool</h1>"+
                            "<div style=\"border: 9px solid #164773; padding: 20px 15px; text-align: center;\">"+
                                "<p>안녕하세요, 회원님</p>"+
                                "<p>셀러툴을 이용해 주셔서 감사합니다.</p>"+
                                "<p>회원님의 아이디는 </p>"+
                                "<p style=\"text-align: center; font-size: 28px; font-weight: 600;\">"+username+"</p>"+
                                "<p>입니다. </p>"+
                                "<div style=\"opacity: 0.6; font-size: 12px;\">"+
                                    "<p>계정 보호를 위해 이 이메일을 전달하거나 다른 사람에게 공유하지 마세요.</p>"+
                                "</div>"+
                            "</div>"+
                            "<div style=\"opacity: 0.4;\">"+
                                "<p>@2020 SellerTool.</p>"+
                            "</div>"+
                        "</div>"+
                    "</main>"+
                "</body>"+
            "</html>"
        );
        EmailDTO email = new EmailDTO(title,content.toString(),sendAddress,receiveAddress);
        return email;
    }

    public EmailDTO setFindPwEmail(String receiveAddress, String newPassword){
        String title = "셀러툴 비밀번호 찾기 메일 입니다.";
        String sendAddress = "sellertool@sellertl.com";
        StringBuilder content = new StringBuilder();
        // content.append(
        //                 "<!DOCTYPE html>"+
        //                 "<html lang=\"ko\">"+
        //                 "<head>"+
        //                     "<meta charset=\"UTF-8\">"+
        //                     "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"+
        //                 "</head>"+
        //                 "<body>"+
        //                     "<div style=\"text-align:center;\">"+
        //                         "<h1>안녕하세요 셀러툴 입니다. 셀러툴을 이용해 주셔서 감사합니다.</h1>"+
        //                         "<h1>회원님의 임시 비밀번호는 </h3>"+
        //                         "<h1 style=\"color:red;\"><strong>"+newPassword+"</strong></h1>"+
        //                         "<h1>입니다.</h1>"+
        //                         "<h1>임시 비밀번호로 로그인을 하신다음 빠른 시일내로 새로운 비밀번호로 변경해 주시기 바랍니다.</h1>"+
        //                     "</div>"+
        //                 "</body>"+
        //                 "</html>"
        //             );        
        content.append(
            "<!DOCTYPE html>"+
            "<html lang=\"ko\">"+
                "<head>"+
                    "<link href=\"https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;500&display=swap\" rel=\"stylesheet\">"+
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />"+
                "</head>"+
                "<body style=\"background-color: white;\">"+
                    "<main>"+
                        "<div style=\"padding-top: 50px; background-color: white; font-family: 'Noto Sans KR', sans-serif; width: 70%; margin: auto;\">"+
                            "<h1 class=\"mail_header\">Seller Tool</h1>"+
                            "<div style=\"border: 9px solid #164773; padding: 20px 15px; text-align: center;\">"+
                                "<p>안녕하세요, 회원님</p>"+
                                "<p>셀러툴을 이용해 주셔서 감사합니다.</p>"+
                                "<p>회원님의 임시 비밀번호는 </p>"+
                                "<p style=\"text-align: center; font-size: 28px; font-weight: 600;\">"+newPassword+"</p>"+
                                "<p>입니다. </p>"+
                                "<div style=\"opacity: 0.6; font-size: 12px;\">"+
                                    "<p>계정 보호를 위해 이 이메일을 전달하거나 다른 사람에게 공유하지 마세요.</p>"+
                                "</div>"+
                            "</div>"+
                            "<div style=\"opacity: 0.4;\">"+
                                "<p>@2020 SellerTool.</p>"+
                            "</div>"+
                        "</div>"+
                    "</main>"+
                "</body>"+
            "</html>"
        );
        EmailDTO email = new EmailDTO(title,content.toString(),sendAddress,receiveAddress);
        return email;
    }

    public Boolean isCodeValid(String code, HttpServletRequest request){
        // **Origin**
        // String key = null;
        // for(int i = 0 ; i < request.getCookies().length; i++){
        //     if(request.getCookies()[i].getName().equals("MAILAUTH")){
        //         key = request.getCookies()[i].getValue();
        //     }
        // }

        // SignupCodeDTO codeDto = (SignupCodeDTO) redisTemplate.opsForValue().get(key);
        // if(codeDto!=null && key !=null && codeDto.getCode().equals(code)){
        //     codeDto.setValid(true);
        //     redisTemplate.opsForValue().set(key, codeDto);
        //     return true;
        // }
        // return false;

        // **Test OK**
        String key = null;
        for(int i = 0 ; i < request.getCookies().length; i++){
            if(request.getCookies()[i].getName().equals("MAILAUTH")){
                key = request.getCookies()[i].getValue();
            }
        }

        String codeStr = (String) redisTemplate.opsForValue().get(key);

        if(codeStr!=null && !codeStr.isEmpty() && key!=null){
            SignupCodeDTO codeDto = (SignupCodeDTO) convert.jsonString2ObjectClassConvert(codeStr, SignupCodeDTO.class);
            if( codeDto.getCode().equals(code)){
                codeDto.setValid(true);
    
                String codeDto2Str = convert.objectClass2JsonStringConvert(codeDto);
    
                redisTemplate.opsForValue().set(key, codeDto2Str);
                return true;
            }
        }
        return false;
    }
    
    public Boolean existUserEmail(String email){
        UserEntity user = userRepository.findByEmail(email);
        if(user != null){
            return true;
        }
        return false;
    }

    // **TEST OK**
    public Boolean authFailure(HttpServletRequest request){
        String key = null;
        for(int i = 0 ; i < request.getCookies().length; i++){
            if(request.getCookies()[i].getName().equals("MAILAUTH")){
                key = request.getCookies()[i].getValue();
            }
        }
        
        if(key!=null && !key.isEmpty()){
            // System.out.println(key);
            String codeStr = (String) redisTemplate.opsForValue().get(key);
            // System.out.println(codeStr);
            if(codeStr!=null && !codeStr.isEmpty()){
                // System.out.println("isNot null");
                SignupCodeDTO codeStr2Dto = (SignupCodeDTO) convert.jsonString2ObjectClassConvert(codeStr, SignupCodeDTO.class);
                if(codeStr2Dto!=null && codeStr2Dto.getValid()==true){
                    return false;
                }
            }
        }
        return true;
    }

    public void emailCodeInvalid(HttpServletRequest request){
        String key = null;
        for(int i = 0 ; i < request.getCookies().length; i++){
            if(request.getCookies()[i].getName().equals("MAILAUTH")){
                key = request.getCookies()[i].getValue();
            }
        }
        if(key!=null){
            redisTemplate.delete(key);
        }
    }
}
