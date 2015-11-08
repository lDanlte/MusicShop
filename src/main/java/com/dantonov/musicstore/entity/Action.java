
package com.dantonov.musicstore.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
@Entity
@Table(name = "Actions")
public class Action {

    @Id
    @Column(name = "action_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Byte id;
    
    @Column(name = "description", nullable = false, length = 32)
    private String desc;
    
    @OneToMany(mappedBy = "action", targetEntity = TradeHistory.class)
    @OrderBy("datetime desc")
    private List<TradeHistory> historys;

    
    
    public Action() {
    }

    public Action(Byte id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public Byte getId() { return id; }
    public void setId(Byte id) { this.id = id; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public List<TradeHistory> getHistorys() { return historys; }
    public void setHistorys(List<TradeHistory> historys) { this.historys = historys; }
    
    
    
    
}
