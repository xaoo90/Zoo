/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Xaoo
 */
@Entity
@Table(name = "TICKETTYPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TicketType.findAll", query = "SELECT t FROM TicketType t"),
    @NamedQuery(name = "TicketType.findByIdTicketType", query = "SELECT t FROM TicketType t WHERE t.idTicketType = :idTicketType"),
    @NamedQuery(name = "TicketType.findByName", query = "SELECT t FROM TicketType t WHERE t.name = :name"),
    @NamedQuery(name = "TicketType.findMaxId", query = "SELECT max(t.idTicketType) FROM TicketType t"),
    @NamedQuery(name = "TicketType.findByPrice", query = "SELECT t FROM TicketType t WHERE t.price = :price")})
public class TicketType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDTICKETTYPE")
    private Integer idTicketType;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "PRICE")
    private BigDecimal price;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticketType")
    private Collection<Ticket> ticketCollection;

    public TicketType() {
    }

    public TicketType(Integer idTicketType) {
        this.idTicketType = idTicketType;
    }

    public TicketType(Integer idTicketType, String name, BigDecimal price) {
        this.idTicketType = idTicketType;
        this.name = name;
        this.price = price;
    }

    public Integer getIdTicketType() {
        return idTicketType;
    }

    public void setIdTicketType(Integer idTicketType) {
        this.idTicketType = idTicketType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @XmlTransient
    public Collection<Ticket> getTicketCollection() {
        return ticketCollection;
    }

    public void setTicketCollection(Collection<Ticket> ticketCollection) {
        this.ticketCollection = ticketCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTicketType != null ? idTicketType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TicketType)) {
            return false;
        }
        TicketType other = (TicketType) object;
        if ((this.idTicketType == null && other.idTicketType != null) || (this.idTicketType != null && !this.idTicketType.equals(other.idTicketType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    
}
