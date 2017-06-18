/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Xaoo
 */
@Entity
@Table(name = "ORDERPOSITION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrderPosition.findAll", query = "SELECT o FROM OrderPosition o"),
    @NamedQuery(name = "OrderPosition.findByIdOrderPosition", query = "SELECT o FROM OrderPosition o WHERE o.idOrderPosition = :idOrderPosition"),
    @NamedQuery(name = "OrderPosition.findByAmount", query = "SELECT o FROM OrderPosition o WHERE o.amount = :amount"),
    @NamedQuery(name = "OrderPosition.findMaxId", query = "SELECT max(o.idOrderPosition) FROM OrderPosition o"),
    @NamedQuery(name = "OrderPosition.findMaxId", query = "SELECT max(o.idOrderPosition) FROM OrderPosition o"),
    @NamedQuery(name = "OrderPosition.findByPrice", query = "SELECT o FROM OrderPosition o WHERE o.price = :price")})
public class OrderPosition implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDORDERPOSITION")
    private Integer idOrderPosition;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "PRICE")
    private BigDecimal price;
    @JoinColumn(name = "FEED", referencedColumnName = "IDFEED")
    @ManyToOne(optional = false)
    private Feed feed;
    @JoinColumn(name = "ORDERFEED", referencedColumnName = "IDORDER")
    @ManyToOne(optional = false)
    private OrderFeed orderFeed;

    public OrderPosition() {
    }

    public OrderPosition(Integer idOrderPosition) {
        this.idOrderPosition = idOrderPosition;
    }

    public OrderPosition(Integer idOrderPosition, BigDecimal amount) {
        this.idOrderPosition = idOrderPosition;
        this.amount = amount;
    }

    public Integer getIdOrderPosition() {
        return idOrderPosition;
    }

    public void setIdOrderPosition(Integer idOrderPosition) {
        this.idOrderPosition = idOrderPosition;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public OrderFeed getOrderFeed() {
        return orderFeed;
    }

    public void setOrderFeed(OrderFeed orderFeed) {
        this.orderFeed = orderFeed;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOrderPosition != null ? idOrderPosition.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderPosition)) {
            return false;
        }
        OrderPosition other = (OrderPosition) object;
        if ((this.idOrderPosition == null && other.idOrderPosition != null) || (this.idOrderPosition != null && !this.idOrderPosition.equals(other.idOrderPosition))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OrderPosition[ idorderposition=" + idOrderPosition + " ]";
    }
    
}
