/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Xaoo
 */
@Entity
@Table(name = "CAGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cage.findAll", query = "SELECT c FROM Cage c"),
    @NamedQuery(name = "Cage.findByIdcage", query = "SELECT c FROM Cage c WHERE c.idCage = :idCage"),
    @NamedQuery(name = "Cage.findByType", query = "SELECT c FROM Cage c WHERE c.type = :type"),
    @NamedQuery(name = "Cage.findByCondition", query = "SELECT c FROM Cage c WHERE c.condition = :condition"),
    @NamedQuery(name = "Cage.findBySpace", query = "SELECT c FROM Cage c WHERE c.space = :space"),
    @NamedQuery(name = "Cage.findMaxId", query = "SELECT max(c.idCage) FROM Cage c"),
    @NamedQuery(name = "Cage.findByDistinctType", query = "SELECT DISTINCT c.type FROM Cage c ORDER BY c.type ASC")})
public class Cage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDCAGE")
    private Integer idCage;
    @Basic(optional = false)
    @Column(name = "TYPE")
    private String type;
    @Basic(optional = false)
    @Column(name = "CONDITION")
    private String condition;
    @Basic(optional = false)
    @Column(name = "SPACE")
    private int space;
    @JoinColumn(name = "SECTOR", referencedColumnName = "IDSECTOR")
    @ManyToOne(optional = false)
    private Sector sector;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cage")
    private Collection<Animal> animalCollection;

    public Cage() {
    }

    public Cage(Integer idCage) {
        this.idCage = idCage;
    }

    public Cage(Integer idCage, String type, String condition, int space) {
        this.idCage = idCage;
        this.type = type;
        this.condition = condition;
        this.space = space;
    }

    public Integer getIdCage() {
        return idCage;
    }

    public void setIdCage(Integer idCage) {
        this.idCage = idCage;
        }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    @XmlTransient
    public Collection<Animal> getAnimalCollection() {
        return animalCollection;
    }

    public void setAnimalCollection(Collection<Animal> animalCollection) {
        this.animalCollection = animalCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCage != null ? idCage.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cage)) {
            return false;
        }
        Cage other = (Cage) object;
        if ((this.idCage == null && other.idCage != null) || (this.idCage != null && !this.idCage.equals(other.idCage))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Cage[ idcage=" + idCage + " ]";
    }
   
}
