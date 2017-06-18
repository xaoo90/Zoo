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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Xaoo
 */
@Entity
@Table(name = "ANIMAL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Animal.findAll", query = "SELECT a FROM Animal a"),
    @NamedQuery(name = "Animal.findByIdAnimal", query = "SELECT a FROM Animal a WHERE a.idAnimal = :idAnimal"),
    @NamedQuery(name = "Animal.findBySpecies", query = "SELECT a FROM Animal a WHERE a.species = :species"),
    @NamedQuery(name = "Animal.findBySex", query = "SELECT a FROM Animal a WHERE a.sex = :sex"),
    @NamedQuery(name = "Animal.findByDescription", query = "SELECT a FROM Animal a WHERE a.description = :description"),
    @NamedQuery(name = "Animal.findByBirthDate", query = "SELECT a FROM Animal a WHERE a.birthDate = :birthDate"),
    @NamedQuery(name = "Animal.findByArrivalDate", query = "SELECT a FROM Animal a WHERE a.arrivalDate = :arrivalDate"),
    @NamedQuery(name = "Animal.findMaxId", query = "SELECT max(a.idAnimal) FROM Animal a"),
    @NamedQuery(name = "Animal.findBySource", query = "SELECT a FROM Animal a WHERE a.source = :source")})
public class Animal implements Serializable {

    @Transient
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDANIMAL")
    private Integer idAnimal;
    @Basic(optional = false)
    @Column(name = "SPECIES")
    private String species;
    @Column(name = "SEX")
    private String sex;
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @Column(name = "BIRTHDATE")
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    @Basic(optional = false)
    @Column(name = "ARRIVALDATE")
    @Temporal(TemporalType.DATE)
    private Date arrivalDate;
    @Basic(optional = false)
    @Column(name = "SOURCE")
    private String source;
    @JoinColumn(name = "CAGE", referencedColumnName = "IDCAGE")
    @ManyToOne(optional = false)
    private Cage cage;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "animal")
    private Collection<AnimalFeed> animalFeedCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "animal")
    private Collection<MedExamination> medExaminationCollection;

    public Animal() {
    }

    public Animal(Integer idAnimal) {
        this.idAnimal = idAnimal;
    }

    public Animal(Integer idAnimal, String species, Date birthDate, Date arrivalDate, String source) {
        this.idAnimal = idAnimal;
        this.species = species;
        this.birthDate = birthDate;
        this.arrivalDate = arrivalDate;
        this.source = source;
    }

    public Integer getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(Integer idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Cage getCage() {
        return cage;
    }

    public void setCage(Cage cage) {
        this.cage = cage;
    }

    @XmlTransient
    public Collection<AnimalFeed> getAnimalFeedCollection() {
        return animalFeedCollection;
    }

    public void setAnimalFeedCollection(Collection<AnimalFeed> animalFeedCollection) {
        this.animalFeedCollection = animalFeedCollection;
    }

    @XmlTransient
    public Collection<MedExamination> getMedExaminationCollection() {
        return medExaminationCollection;
    }

    public void setMedExaminationCollection(Collection<MedExamination> medExaminationCollection) {
        this.medExaminationCollection = medExaminationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAnimal != null ? idAnimal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Animal)) {
            return false;
        }
        Animal other = (Animal) object;
        if ((this.idAnimal == null && other.idAnimal != null) || (this.idAnimal != null && !this.idAnimal.equals(other.idAnimal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return species;
    }

}
