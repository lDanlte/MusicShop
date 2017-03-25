
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
@Table(name = "actions")
public class Action {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "description", nullable = false, length = 32)
    private String desc;
    
    @OneToMany(mappedBy = "action", targetEntity = TradeHistory.class)
    @OrderBy("datetime desc")
    private List<TradeHistory> historys;

    
    
    public Action() {
    }

    public Action(final Integer id, final String desc) {
        this.id = id;
        this.desc = desc;
    }

    
    public Integer getId() { return id; }
    public void setId(final Integer id) { this.id = id; }

    public String getDesc() { return desc; }
    public void setDesc(final String desc) { this.desc = desc; }

    public List<TradeHistory> getHistorys() { return historys; }
    public void setHistorys(final List<TradeHistory> historys) { this.historys = historys; }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Action action = (Action) o;

        return id.equals(action.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
