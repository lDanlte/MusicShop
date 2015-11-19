package com.dantonov.musicstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
public class ResponseMessageDto {

    @JsonProperty("code")
    private Integer code;
    
    @JsonProperty("msg")
    private String msg;

    
    
    public ResponseMessageDto() {
    }

    public ResponseMessageDto(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    
    
}
