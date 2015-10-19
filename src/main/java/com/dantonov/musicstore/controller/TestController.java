
package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.dto.ActionDto;
import com.dantonov.musicstore.entity.Action;
import com.dantonov.musicstore.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private ActionService actionService;
    
    @RequestMapping(value = "/action/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ActionDto getAction(@PathVariable("id") Long id) {
        Action action = actionService.findById(id);
        
        if (action == null) return null;
        
        ActionDto actionDto = new ActionDto();
        
        actionDto.setId(action.getId());
        actionDto.setDesc(action.getDesc());
        
        return actionDto;
    }
    
    @RequestMapping(value = "/action/save", method = RequestMethod.POST)
    public void saveAction(@RequestParam("desc") String desc){
        Action action = new Action();
        action.setDesc(desc);
        actionService.save(action);
    }
    
}
