/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import utilLogic.UtilLogic;

/**
 *
 * @author Xaoo
 */
@Entity
@Table(name = "TICKET")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ticket.findAll", query = "SELECT t FROM Ticket t"),
    @NamedQuery(name = "Ticket.findByIdTicket", query = "SELECT t FROM Ticket t WHERE t.idTicket = :idTicket"),
    @NamedQuery(name = "Ticket.findByIssuedDate", query = "SELECT t FROM Ticket t WHERE t.issuedDate = :issuedDate"),
    @NamedQuery(name = "Ticket.findByIssuedDateYear", query = "SELECT t FROM Ticket t WHERE EXTRACT(year from t.issuedDate) = :year"),
    @NamedQuery(name = "Ticket.findByIssuedDateMonth", query = "SELECT t FROM Ticket t WHERE EXTRACT(year from t.issuedDate) = :year AND EXTRACT(month from t.issuedDate) = :month"),
    @NamedQuery(name = "Ticket.findByAmount", query = "SELECT t FROM Ticket t WHERE t.amount = :amount"),
    @NamedQuery(name = "Ticket.findByCost", query = "SELECT t FROM Ticket t WHERE t.cost = :cost"),
    @NamedQuery(name = "Ticket.findMaxId", query = "SELECT max(t.idTicket) FROM Ticket t"),
    @NamedQuery(name = "Ticket.findByTicketExist", query = "SELECT t FROM Ticket t WHERE t.ticketType = :ticketType AND t.issuedDate = :issuedDate"),
    @NamedQuery(name = "Ticket.findLastAddEntity", query = "SELECT t FROM Ticket t WHERE t.idTicket = (SELECT max(tic.idTicket) FROM Ticket tic)")})
public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDTICKET")
    private Integer idTicket;
    @Column(name = "ISSUEDDATE")
    @Temporal(TemporalType.DATE)
    private Date issuedDate;
    @Basic(optional = false)
    @Column(name = "AMOUNT")
    private int amount;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "COST")
    private BigDecimal cost;
    @JoinColumn(name = "TICKETTYPE", referencedColumnName = "IDTICKETTYPE")
    @ManyToOne(optional = false)
    private TicketType ticketType;

    public Ticket() {
    }

    public Ticket(Integer idTicket) {
        this.idTicket = idTicket;
    }

    public Ticket(Integer idTicket, int amount, BigDecimal cost) {
        this.idTicket = idTicket;
        this.amount = amount;
        this.cost = cost;
    }

    public Integer getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Integer idTicket) {
        this.idTicket = idTicket;
    }

    public Date getIssuedDatee() {
        return issuedDate;
    }

    public String getIssuedDate() {
        return UtilLogic.dateFormat(issuedDate);
    }
    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTicket != null ? idTicket.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ticket)) {
            return false;
        }
        Ticket other = (Ticket) object;
        if ((this.idTicket == null && other.idTicket != null) || (this.idTicket != null && !this.idTicket.equals(other.idTicket))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Ticket[ idticket=" + idTicket + " ]";
    }
    
}
