package entity;

import entity.Animal;
import entity.Sector;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(Cage.class)
public class Cage_ { 

    public static volatile SingularAttribute<Cage, String> condition;
    public static volatile SingularAttribute<Cage, Integer> idCage;
    public static volatile CollectionAttribute<Cage, Animal> animalCollection;
    public static volatile SingularAttribute<Cage, String> type;
    public static volatile SingularAttribute<Cage, Sector> sector;
    public static volatile SingularAttribute<Cage, Integer> space;

}