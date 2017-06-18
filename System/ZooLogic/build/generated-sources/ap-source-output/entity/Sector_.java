package entity;

import entity.Cage;
import entity.Employee;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(Sector.class)
public class Sector_ { 

    public static volatile SingularAttribute<Sector, Employee> manager;
    public static volatile SingularAttribute<Sector, String> name;
    public static volatile SingularAttribute<Sector, Integer> idSector;
    public static volatile SingularAttribute<Sector, String> type;
    public static volatile CollectionAttribute<Sector, Cage> cageCollection;

}