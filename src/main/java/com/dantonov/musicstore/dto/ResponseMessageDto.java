package com.dantonov.musicstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
public class ResponseMessageDto {

    @JsonProperty("code")
    private Byte code;
    
    @JsonProperty("msg")
    private String msg;

    public ResponseMessageDto() {
    }

    public ResponseMessageDto(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Byte getCode() { return code; }
    public void setCode(Byte code) { this.code = code; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    
    
}
