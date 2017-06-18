package entity;

import entity.Animal;
import entity.Feed;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(AnimalFeed.class)
public class AnimalFeed_ { 

    public static volatile SingularAttribute<AnimalFeed, Feed> feed;
    public static volatile SingularAttribute<AnimalFeed, String> feedTime;
    public static volatile SingularAttribute<AnimalFeed, BigDecimal> portion;
    public static volatile SingularAttribute<AnimalFeed, Integer> idAnimalFeed;
    public static volatile SingularAttribute<AnimalFeed, Animal> animal;
    public static volatile SingularAttribute<AnimalFeed, String> frequency;

}