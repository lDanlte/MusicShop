
package com.dantonov.musicstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
public class ActionDto {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("desc")
    private String desc;
    
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

}
