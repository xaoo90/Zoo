package entity;

import entity.AnimalFeed;
import entity.Cage;
import entity.MedExamination;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(Animal.class)
public class Animal_ { 

    public static volatile SingularAttribute<Animal, Integer> idAnimal;
    public static volatile SingularAttribute<Animal, Cage> cage;
    public static volatile CollectionAttribute<Animal, MedExamination> medExaminationCollection;
    public static volatile SingularAttribute<Animal, String> species;
    public static volatile SingularAttribute<Animal, String> sex;
    public static volatile SingularAttribute<Animal, String> description;
    public static volatile CollectionAttribute<Animal, AnimalFeed> animalFeedCollection;
    public static volatile SingularAttribute<Animal, String> source;
    public static volatile SingularAttribute<Animal, Date> birthDate;
    public static volatile SingularAttribute<Animal, Date> arrivalDate;

}