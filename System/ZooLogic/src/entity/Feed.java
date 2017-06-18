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
@Table(name = "FEED")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Feed.findAll", query = "SELECT f FROM Feed f"),
    @NamedQuery(name = "Feed.findByIdFeed", query = "SELECT f FROM Feed f WHERE f.idFeed = :idFeed"),
    @NamedQuery(name = "Feed.findByName", query = "SELECT f FROM Feed f WHERE f.name = :name"),
    @NamedQuery(name = "Feed.findByAvailability", query = "SELECT f FROM Feed f WHERE f.availability = :availability"),
    @NamedQuery(name = "Feed.findMaxId", query = "SELECT max(f.idFeed) FROM Feed f"),
    @NamedQuery(name = "Feed.findByUnit", query = "SELECT f FROM Feed f WHERE f.unit = :unit")})
public class Feed implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDFEED")
    private Integer idFeed;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AVAILABILITY")
    private BigDecimal availability;
    @Column(name = "UNIT")
    private String unit;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "feed")
    private Collection<AnimalFeed> animalFeedCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "feed")
    private Collection<OrderPosition> orderPositionCollection;
    @JoinColumn(name = "WAREHOUSE", referencedColumnName = "IDWAREHOUSE")
    @ManyToOne(optional = false)
    private Warehouse warehouse;
    
    public Feed() {
    }

    public Feed(Integer idFeed) {
        this.idFeed = idFeed;
    }

    public Feed(Integer idFeed, String name) {
        this.idFeed = idFeed;
        this.name = name;
    }

    public Integer getIdFeed() {
        return idFeed;
    }

    public void setIdFeed(Integer idFeed) {
        this.idFeed = idFeed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAvailability() {
        return availability;
    }

    public void setAvailability(BigDecimal availability) {
        this.availability = availability;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @XmlTransient
    public Collection<AnimalFeed> getAnimalFeedCollection() {
        return animalFeedCollection;
    }

    public void setAnimalFeedCollection(Collection<AnimalFeed> animalFeedCollection) {
        this.animalFeedCollection = animalFeedCollection;
    }

    @XmlTransient
    public Collection<OrderPosition> getOrderPositionCollection() {
        return orderPositionCollection;
    }

    public void setOrderPositionCollection(Collection<OrderPosition> orderPositionCollection) {
        this.orderPositionCollection = orderPositionCollection;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFeed != null ? idFeed.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Feed)) {
            return false;
        }
        Feed other = (Feed) object;
        if ((this.idFeed == null && other.idFeed != null) || (this.idFeed != null && !this.idFeed.equals(other.idFeed))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }
    
}
