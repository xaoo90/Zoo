package entity;

import entity.Feed;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(Warehouse.class)
public class Warehouse_ { 

    public static volatile SingularAttribute<Warehouse, Integer> idWarehouse;
    public static volatile SingularAttribute<Warehouse, String> name;
    public static volatile CollectionAttribute<Warehouse, Feed> feedCollection;

}