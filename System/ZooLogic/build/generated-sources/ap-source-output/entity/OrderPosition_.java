package entity;

import entity.Feed;
import entity.OrderFeed;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(OrderPosition.class)
public class OrderPosition_ { 

    public static volatile SingularAttribute<OrderPosition, Feed> feed;
    public static volatile SingularAttribute<OrderPosition, BigDecimal> amount;
    public static volatile SingularAttribute<OrderPosition, BigDecimal> price;
    public static volatile SingularAttribute<OrderPosition, Integer> idOrderPosition;
    public static volatile SingularAttribute<OrderPosition, OrderFeed> orderFeed;

}