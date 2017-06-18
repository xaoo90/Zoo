/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "ANIMALFEED")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnimalFeed.findAll", query = "SELECT a FROM AnimalFeed a"),
    @NamedQuery(name = "AnimalFeed.findByIdanimalfeed", query = "SELECT a FROM AnimalFeed a WHERE a.idAnimalFeed = :idAnimalFeed"),
    @NamedQuery(name = "AnimalFeed.findByPortion", query = "SELECT a FROM AnimalFeed a WHERE a.portion = :portion"),
    @NamedQuery(name = "AnimalFeed.findByFeedtime", query = "SELECT a FROM AnimalFeed a WHERE a.feedTime = :feedTime"),
    @NamedQuery(name = "AnimalFeed.findMaxId", query = "SELECT max(a.idAnimalFeed) FROM AnimalFeed a"),
    @NamedQuery(name = "AnimalFeed.findByFrequency", query = "SELECT a FROM AnimalFeed a WHERE a.frequency = :frequency")})
public class AnimalFeed implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDANIMALFEED")
    private Integer idAnimalFeed;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PORTION")
    private BigDecimal portion;
    @Column(name = "FEEDTIME")
    private String feedTime;
    @Column(name = "FREQUENCY")
    private String frequency;
    @JoinColumn(name = "ANIMAL", referencedColumnName = "IDANIMAL")
    @ManyToOne(optional = false)
    private Animal animal;
    @JoinColumn(name = "FEED", referencedColumnName = "IDFEED")
    @ManyToOne(optional = false)
    private Feed feed;

    public AnimalFeed() {
    }

    public AnimalFeed(Integer idAnimalFeed) {
        this.idAnimalFeed = idAnimalFeed;
    }

    public Integer getIdAnimalFeed() {
        return idAnimalFeed;
    }

    public void setIdAnimalFeed(Integer idAnimalFeed) {
        this.idAnimalFeed = idAnimalFeed;
    }

    public BigDecimal getPortionn() {
        return portion;
    }
    
    public String getPortion() {
        return String.valueOf(portion) + " " + feed.getUnit();
    }

    public void setPortion(BigDecimal portion) {
        this.portion = portion;
    }

    public String getFeedTime() {
        return feedTime;
    }

    public void setFeedTime(String feedTime) {
        this.feedTime = feedTime;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAnimalFeed != null ? idAnimalFeed.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnimalFeed)) {
            return false;
        }
        AnimalFeed other = (AnimalFeed) object;
        if ((this.idAnimalFeed == null && other.idAnimalFeed != null) || (this.idAnimalFeed != null && !this.idAnimalFeed.equals(other.idAnimalFeed))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AnimalFeed[ idanimalfeed=" + idAnimalFeed + " ]";
    }
    
}
