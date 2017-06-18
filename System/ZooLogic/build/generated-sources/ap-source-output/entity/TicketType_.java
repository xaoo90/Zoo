package entity;

import entity.Ticket;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(TicketType.class)
public class TicketType_ { 

    public static volatile SingularAttribute<TicketType, Integer> idTicketType;
    public static volatile CollectionAttribute<TicketType, Ticket> ticketCollection;
    public static volatile SingularAttribute<TicketType, BigDecimal> price;
    public static volatile SingularAttribute<TicketType, String> name;

}