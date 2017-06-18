/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import utilLogic.UtilLogic;

/**
 *
 * @author Xaoo
 */
@Entity
@Table(name = "ORDERFEED")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrderFeed.findAll", query = "SELECT o FROM OrderFeed o"),
    @NamedQuery(name = "OrderFeed.findByIdOrder", query = "SELECT o FROM OrderFeed o WHERE o.idOrder = :idOrder"),
    @NamedQuery(name = "OrderFeed.findByOrderDate", query = "SELECT o FROM OrderFeed o WHERE o.orderDate = :orderDate"),
    @NamedQuery(name = "OrderFeed.findHistoryOrder", query = "SELECT o FROM OrderFeed o WHERE o.condition = :condition"),
    @NamedQuery(name = "OrderFeed.findMaxId", query = "SELECT max(o.idOrder) FROM OrderFeed o"),
    @NamedQuery(name = "OrderFeed.findLastAddEntity", query = "SELECT o FROM OrderFeed o WHERE o.idOrder = (SELECT max(ord.idOrder) FROM OrderFeed ord)"),
    @NamedQuery(name = "OrderFeed.findByCondition", query = "SELECT o FROM OrderFeed o WHERE o.condition = :condition")})
public class OrderFeed implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDORDER")
    private Integer idOrder;
    @Basic(optional = false)
    @Column(name = "ORDERDATE")
    @Temporal(TemporalType.DATE)
    private Date orderDate;
    @Basic(optional = false)
    @Column(name = "CONDITION")
    private String condition;
    @JoinColumn(name = "WAREHOUSEMEN", referencedColumnName = "IDEMPLOYEE")
    @ManyToOne(optional = false)
    private Employee warehouseMen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderFeed")
    private Collection<OrderPosition> orderPositionCollection;

    public OrderFeed() {
    }

    public OrderFeed(Integer idOrder) {
        this.idOrder = idOrder;
    }

    public OrderFeed(Integer idOrder, Date orderDate, String condition) {
        this.idOrder = idOrder;
        this.orderDate = orderDate;
        this.condition = condition;
    }

    public Integer getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Integer idOrder) {
        this.idOrder = idOrder;
    }

    public Date getOrderDatee() {
        return orderDate;
    }

    public String getOrderDate() {
        return UtilLogic.dateFormat(getOrderDatee());
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Employee getWarehouseMen() {
        return warehouseMen;
    }

    public void setWarehouseMen(Employee warehouseMen) {
        this.warehouseMen = warehouseMen;
    }

    @XmlTransient
    public Collection<OrderPosition> getOrderPositionCollection() {
        return orderPositionCollection;
    }

    public void setOrderPositionCollection(Collection<OrderPosition> orderPositionCollection) {
        this.orderPositionCollection = orderPositionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOrder != null ? idOrder.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderFeed)) {
            return false;
        }
        OrderFeed other = (OrderFeed) object;
        if ((this.idOrder == null && other.idOrder != null) || (this.idOrder != null && !this.idOrder.equals(other.idOrder))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OrderFeed[ idorder=" + idOrder + " ]";
    }

}
