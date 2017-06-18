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
@Table(name = "WAREHOUSE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Warehouse.findAll", query = "SELECT w FROM Warehouse w"),
    @NamedQuery(name = "Warehouse.findMaxId", query = "SELECT max(w.idWarehouse) FROM Warehouse w"),
    @NamedQuery(name = "Warehouse.findByIdWarehouse", query = "SELECT w FROM Warehouse w WHERE w.idWarehouse = :idWarehouse"),
    @NamedQuery(name = "Warehouse.findByName", query = "SELECT w FROM Warehouse w WHERE w.name = :name")})
public class Warehouse implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDWAREHOUSE")
    private Integer idWarehouse;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "warehouse")
    private Collection<Feed> feedCollection;

    public Warehouse() {
    }

    public Warehouse(Integer idWarehouse) {
        this.idWarehouse = idWarehouse;
    }

    public Warehouse(Integer idWarehouse, String name) {
        this.idWarehouse = idWarehouse;
        this.name = name;
    }

    public Integer getIdWarehouse() {
        return idWarehouse;
    }

    public void setIdWarehouse(Integer idWarehouse) {
        this.idWarehouse = idWarehouse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<Feed> getFeedCollection() {
        return feedCollection;
    }

    public void setFeedCollection(Collection<Feed> feedCollection) {
        this.feedCollection = feedCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idWarehouse != null ? idWarehouse.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Warehouse)) {
            return false;
        }
        Warehouse other = (Warehouse) object;
        if ((this.idWarehouse == null && other.idWarehouse != null) || (this.idWarehouse != null && !this.idWarehouse.equals(other.idWarehouse))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
