package com.sellertl.sellertool_v1_login.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

@Service
public class ConvertService {
    ObjectMapper om = new ObjectMapper();

    public Object jsonString2ObjectClassConvert(String str, Class<?> type) {
        try {
            return om.readValue(str, type);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String objectClass2JsonStringConvert(Object ob){
        String str = null;
        try {
            str = om.writeValueAsString(ob);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }
}
