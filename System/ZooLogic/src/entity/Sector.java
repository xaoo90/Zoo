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
@Table(name = "SECTOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sector.findAll", query = "SELECT s FROM Sector s"),
    @NamedQuery(name = "Sector.findByIdSector", query = "SELECT s FROM Sector s WHERE s.idSector = :idSector"),
    @NamedQuery(name = "Sector.findByType", query = "SELECT s FROM Sector s WHERE s.type = :type"),
    @NamedQuery(name = "Sector.findByName", query = "SELECT s FROM Sector s WHERE s.name = :name"),
    @NamedQuery(name = "Sector.findMaxId", query = "SELECT max(s.idSector) FROM Sector s"),
    @NamedQuery(name = "Sector.findManagerNull", query = "SELECT s FROM Sector s WHERE s.manager is null")})
public class Sector implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDSECTOR")
    private Integer idSector;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "NAME")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sector")
    private Collection<Cage> cageCollection;
    @JoinColumn(name = "MANAGER", referencedColumnName = "IDEMPLOYEE")
    @ManyToOne
    private Employee manager;

    public Sector() {
    }

    public Sector(Integer idSector) {
        this.idSector = idSector;
    }

    public Integer getIdSector() {
        return idSector;
    }

    public void setIdSector(Integer idSector) {
        this.idSector = idSector;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<Cage> getCageCollection() {
        return cageCollection;
    }

    public void setCageCollection(Collection<Cage> cageCollection) {
        this.cageCollection = cageCollection;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSector != null ? idSector.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sector)) {
            return false;
        }
        Sector other = (Sector) object;
        if ((this.idSector == null && other.idSector != null) || (this.idSector != null && !this.idSector.equals(other.idSector))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + type;
    }

    
}
