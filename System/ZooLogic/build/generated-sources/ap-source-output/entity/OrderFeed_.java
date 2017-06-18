package entity;

import entity.Employee;
import entity.OrderPosition;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(OrderFeed.class)
public class OrderFeed_ { 

    public static volatile SingularAttribute<OrderFeed, Integer> idOrder;
    public static volatile SingularAttribute<OrderFeed, String> condition;
    public static volatile SingularAttribute<OrderFeed, Employee> warehouseMen;
    public static volatile SingularAttribute<OrderFeed, Date> orderDate;
    public static volatile CollectionAttribute<OrderFeed, OrderPosition> orderPositionCollection;

}