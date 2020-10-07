package com.sellertl.sellertool_v1_login.model.DTO;

import lombok.Data;

@Data
public class SignupCodeDTO {
    private Boolean valid;
    private String code;

    public SignupCodeDTO(){
        this.valid = false;
    }
}
