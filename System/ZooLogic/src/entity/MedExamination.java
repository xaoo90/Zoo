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
import utilLogic.UtilLogic;

/**
 *
 * @author Xaoo
 */
@Entity
@Table(name = "MEDEXAMINATION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MedExamination.findAll", query = "SELECT m FROM MedExamination m"),
    @NamedQuery(name = "MedExamination.findByIdMedExamination", query = "SELECT m FROM MedExamination m WHERE m.idMedExamination = :idmedexamination"),
    @NamedQuery(name = "MedExamination.findByMedDate", query = "SELECT m FROM MedExamination m WHERE m.medDate = :medDate"),
    @NamedQuery(name = "MedExamination.findByCondition", query = "SELECT m FROM MedExamination m WHERE m.condition = :condition"),
    @NamedQuery(name = "MedExamination.findByDescription", query = "SELECT m FROM MedExamination m WHERE m.description = :description"),
    @NamedQuery(name = "MedExamination.findMaxId", query = "SELECT max(m.idMedExamination) FROM MedExamination m"),
    @NamedQuery(name = "MedExamination.findCountTodayPlanMed", query = "SELECT count(m.idMedExamination) FROM MedExamination m WHERE m.medDate = :medDate AND m.condition = 'Zaplanowane'")})
public class MedExamination implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDMEDEXAMINATION")
    private Integer idMedExamination;
    @Basic(optional = false)
    @Column(name = "MEDDATE")
    @Temporal(TemporalType.DATE)
    private Date medDate;
    @Basic(optional = false)
    @Column(name = "CONDITION")
    private String condition;
    @Column(name = "DESCRIPTION")
    private String description;
    @JoinColumn(name = "ANIMAL", referencedColumnName = "IDANIMAL")
    @ManyToOne(optional = false)
    private Animal animal;
    @JoinColumn(name = "VETERINARIAN", referencedColumnName = "IDEMPLOYEE")
    @ManyToOne(optional = false)
    private Employee veterinarian;

    public MedExamination() {
    }

    public MedExamination(Integer idMedExamination) {
        this.idMedExamination = idMedExamination;
    }

    public MedExamination(Integer idMedExamination, Date medDate, String condition) {
        this.idMedExamination = idMedExamination;
        this.medDate = medDate;
        this.condition = condition;
    }

    public Integer getIdMedExamination() {
        return idMedExamination;
    }

    public void setIdMedExamination(Integer idMedExamination) {
        this.idMedExamination = idMedExamination;
    }

    public String getMedDate() {
        return UtilLogic.dateFormat(medDate);
    }
    
    public Date getMedDatee() {
        return medDate;
    }

    public void setMedDate(Date medDate) {
        this.medDate = medDate;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Employee getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(Employee veterinarian) {
        this.veterinarian = veterinarian;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMedExamination != null ? idMedExamination.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedExamination)) {
            return false;
        }
        MedExamination other = (MedExamination) object;
        if ((this.idMedExamination == null && other.idMedExamination != null) || (this.idMedExamination != null && !this.idMedExamination.equals(other.idMedExamination))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MedExamination[ idmedexamination=" + idMedExamination + " ]";
    }
    
}
