package entity;

import entity.Employee;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(Holiday.class)
public class Holiday_ { 

    public static volatile SingularAttribute<Holiday, Date> endDate;
    public static volatile SingularAttribute<Holiday, Integer> idHoliday;
    public static volatile SingularAttribute<Holiday, String> type;
    public static volatile SingularAttribute<Holiday, Employee> employee;
    public static volatile SingularAttribute<Holiday, Date> startDate;

}