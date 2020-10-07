package com.sellertl.sellertool_v1_login.model.DTO;

import lombok.Data;

@Data
public class UserSignupDTO {
    private String username;
    private String password;
    private String name;
    private String email;
}