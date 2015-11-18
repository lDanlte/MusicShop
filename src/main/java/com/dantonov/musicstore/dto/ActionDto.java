
package com.dantonov.musicstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
public class ActionDto {
    
    @JsonProperty("desc")
    private String desc;

    
    
    public ActionDto() {
    }

    public ActionDto(String desc) {
        this.desc = desc;
    }
    
    
    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    
}
