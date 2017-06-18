package entity;

import entity.TicketType;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(Ticket.class)
public class Ticket_ { 

    public static volatile SingularAttribute<Ticket, Integer> idTicket;
    public static volatile SingularAttribute<Ticket, Date> issuedDate;
    public static volatile SingularAttribute<Ticket, Integer> amount;
    public static volatile SingularAttribute<Ticket, BigDecimal> cost;
    public static volatile SingularAttribute<Ticket, TicketType> ticketType;

}