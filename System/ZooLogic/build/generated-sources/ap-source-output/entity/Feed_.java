package entity;

import entity.AnimalFeed;
import entity.OrderPosition;
import entity.Warehouse;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(Feed.class)
public class Feed_ { 

    public static volatile SingularAttribute<Feed, String> unit;
    public static volatile SingularAttribute<Feed, Integer> idFeed;
    public static volatile SingularAttribute<Feed, String> name;
    public static volatile CollectionAttribute<Feed, AnimalFeed> animalFeedCollection;
    public static volatile SingularAttribute<Feed, BigDecimal> availability;
    public static volatile SingularAttribute<Feed, Warehouse> warehouse;
    public static volatile CollectionAttribute<Feed, OrderPosition> orderPositionCollection;

}