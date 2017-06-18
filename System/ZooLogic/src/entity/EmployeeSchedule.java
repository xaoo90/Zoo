/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
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
@Table(name = "EMPLOYEESCHEDULE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmployeeSchedule.findAll", query = "SELECT e FROM EmployeeSchedule e"),
    @NamedQuery(name = "EmployeeSchedule.findByIdEmployeeSchedule", query = "SELECT e FROM EmployeeSchedule e WHERE e.idEmployeeSchedule = :idEmployeeSchedule"),
    @NamedQuery(name = "EmployeeSchedule.findMaxId", query = "SELECT max(es.idEmployeeSchedule) FROM EmployeeSchedule es"),
    @NamedQuery(name = "EmployeeSchedule.findByPosition", query = "SELECT e FROM EmployeeSchedule e WHERE e.position = :position")})
public class EmployeeSchedule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDEMPLOYEESCHEDULE")
    private Integer idEmployeeSchedule;
    @Basic(optional = false)
    @Column(name = "POSITION")
    private String position;
    @JoinColumn(name = "EMPLOYEE", referencedColumnName = "IDEMPLOYEE")
    @ManyToOne(optional = false)
    private Employee employee;
    @JoinColumn(name = "WORKSCHEDULE", referencedColumnName = "IDWORKSCHEDULE")
    @ManyToOne(optional = false)
    private WorkSchedule workSchedule;

    public EmployeeSchedule() {
    }

    public EmployeeSchedule(Integer idemployeeschedule) {
        this.idEmployeeSchedule = idemployeeschedule;
    }

    public EmployeeSchedule(Integer idEmployeeSchedule, String position) {
        this.idEmployeeSchedule = idEmployeeSchedule;
        this.position = position;
    }

    public Integer getIdEmployeeSchedule() {
        return idEmployeeSchedule;
    }

    public void setIdEmployeeSchedule(Integer idEmployeeSchedule) {
        this.idEmployeeSchedule = idEmployeeSchedule;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public WorkSchedule getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(WorkSchedule workSchedule) {
        this.workSchedule = workSchedule;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmployeeSchedule != null ? idEmployeeSchedule.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeSchedule)) {
            return false;
        }
        EmployeeSchedule other = (EmployeeSchedule) object;
        if ((this.idEmployeeSchedule == null && other.idEmployeeSchedule != null) || (this.idEmployeeSchedule != null && !this.idEmployeeSchedule.equals(other.idEmployeeSchedule))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.EmployeeSchedule[ idemployeeschedule=" + idEmployeeSchedule + " ]";
    }
    
}
