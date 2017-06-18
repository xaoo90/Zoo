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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Xaoo
 */
@Entity
@Table(name = "WORKSCHEDULE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WorkSchedule.findAll", query = "SELECT w FROM WorkSchedule w"),
    @NamedQuery(name = "WorkSchedule.findByIdWorkSchedule", query = "SELECT w FROM WorkSchedule w WHERE w.idWorkSchedule = :idWorkSchedule"),
    @NamedQuery(name = "WorkSchedule.findByWorkDate", query = "SELECT w FROM WorkSchedule w WHERE w.workDate = :workDate"),
    @NamedQuery(name = "WorkSchedule.findMaxId", query = "SELECT max(w.idWorkSchedule) FROM WorkSchedule w"),
    @NamedQuery(name = "WorkSchedule.findByWorkShifts", query = "SELECT w FROM WorkSchedule w WHERE w.workShifts = :workShifts"),
    @NamedQuery(name = "WorkSchedule.findWorkScheduleExist", query = "SELECT w FROM WorkSchedule w WHERE w.workShifts = :workShifts AND w.workDate = :workDate"),
    @NamedQuery(name = "WorkSchedule.findLastAddEntity", query = "SELECT w FROM WorkSchedule w WHERE w.idWorkSchedule = (SELECT max(work.idWorkSchedule) FROM WorkSchedule work)")})
public class WorkSchedule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDWORKSCHEDULE")
    private Integer idWorkSchedule;
    @Basic(optional = false)
    @Column(name = "WORKDATE")
    @Temporal(TemporalType.DATE)
    private Date workDate;
    @Basic(optional = false)
    @Column(name = "WORKSHIFTS")
    private String workShifts;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workSchedule")
    private Collection<EmployeeSchedule> employeeScheduleCollection;

    public WorkSchedule() {
    }

    public WorkSchedule(Integer idWorkSchedule) {
        this.idWorkSchedule = idWorkSchedule;
    }

    public WorkSchedule(Integer idWorkSchedule, Date workDate, String workShifts) {
        this.idWorkSchedule = idWorkSchedule;
        this.workDate = workDate;
        this.workShifts = workShifts;
    }

    public Integer getIdWorkSchedule() {
        return idWorkSchedule;
    }

    public void setIdWorkSchedule(Integer idWorkSchedule) {
        this.idWorkSchedule = idWorkSchedule;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public String getWorkShifts() {
        return workShifts;
    }

    public void setWorkShifts(String workShifts) {
        this.workShifts = workShifts;
    }

    @XmlTransient
    public Collection<EmployeeSchedule> getEmployeeScheduleCollection() {
        return employeeScheduleCollection;
    }

    public void setEmployeeScheduleCollection(Collection<EmployeeSchedule> employeeScheduleCollection) {
        this.employeeScheduleCollection = employeeScheduleCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idWorkSchedule != null ? idWorkSchedule.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkSchedule)) {
            return false;
        }
        WorkSchedule other = (WorkSchedule) object;
        if ((this.idWorkSchedule == null && other.idWorkSchedule != null) || (this.idWorkSchedule != null && !this.idWorkSchedule.equals(other.idWorkSchedule))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.WorkSchedule[ idworkschedule=" + idWorkSchedule + " ]";
    }
    
}
