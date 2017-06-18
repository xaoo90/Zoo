/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
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

/**
 *
 * @author Xaoo
 */
@Entity
@Table(name = "HOLIDAY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Holiday.findAll", query = "SELECT h FROM Holiday h"),
    @NamedQuery(name = "Holiday.findByIdHoliday", query = "SELECT h FROM Holiday h WHERE h.idHoliday = :idHoliday"),
    @NamedQuery(name = "Holiday.findByIdEmployee", query = "SELECT h FROM Holiday h WHERE h.employee = :employee"),
    @NamedQuery(name = "Holiday.findByStartDate", query = "SELECT h FROM Holiday h WHERE h.startDate = :startDate"),
    @NamedQuery(name = "Holiday.findByEndDate", query = "SELECT h FROM Holiday h WHERE h.endDate = :endDate"),
    @NamedQuery(name = "Holiday.findMaxId", query = "SELECT max(h.idHoliday) FROM Holiday h"),
    @NamedQuery(name = "Holiday.findByType", query = "SELECT h FROM Holiday h WHERE h.type = :type")})
public class Holiday implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDHOLIDAY")
    private Integer idHoliday;
    @Basic(optional = false)
    @Column(name = "STARTDATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Basic(optional = false)
    @Column(name = "ENDDATE")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Basic(optional = false)
    @Column(name = "TYPE")
    private String type;
    @JoinColumn(name = "EMPLOYEE", referencedColumnName = "IDEMPLOYEE")
    @ManyToOne(optional = false)
    private Employee employee;

    public Holiday() {
    }

    public Holiday(Integer idHoliday) {
        this.idHoliday = idHoliday;
    }

    public Holiday(Integer idHoliday, Date startDate, Date endDate, String type) {
        this.idHoliday = idHoliday;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public Integer getIdHoliday() {
        return idHoliday;
    }

    public void setIdHoliday(Integer idHoliday) {
        this.idHoliday = idHoliday;
    }

    public Date getStartDatee() {
        return startDate;
    }
    
    public String getStartDate(){
        return utilLogic.UtilLogic.dateFormat(getStartDatee());
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDatee() {
        return endDate;
    }

    public String getEndDate() {
        return utilLogic.UtilLogic.dateFormat(getEndDatee());
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHoliday != null ? idHoliday.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Holiday)) {
            return false;
        }
        Holiday other = (Holiday) object;
        if ((this.idHoliday == null && other.idHoliday != null) || (this.idHoliday != null && !this.idHoliday.equals(other.idHoliday))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Holiday[ idholiday=" + idHoliday + " ]";
    }
    
}
