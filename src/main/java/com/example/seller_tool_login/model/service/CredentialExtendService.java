package com.example.seller_tool_login.model.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class CredentialExtendService {
    public Date setAndGetExtendOneMonth(Date lastExpireTime){
        Calendar extendedExpCalendar = Calendar.getInstance();
        extendedExpCalendar.setTime(lastExpireTime);
        extendedExpCalendar.add(extendedExpCalendar.DATE, 30);

        return extendedExpCalendar.getTime();
    }
}
