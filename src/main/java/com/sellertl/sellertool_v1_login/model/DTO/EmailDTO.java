package com.sellertl.sellertool_v1_login.model.DTO;

import lombok.Data;

@Data
public class EmailDTO {
    private String title;
	private String content;
	private String sender;
    private String receiver;
    
    public EmailDTO(String title, String content, String sender, String receiver){
        this.title = title;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }
}
