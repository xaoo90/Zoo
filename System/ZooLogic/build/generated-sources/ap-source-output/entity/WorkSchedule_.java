package entity;

import entity.EmployeeSchedule;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(WorkSchedule.class)
public class WorkSchedule_ { 

    public static volatile SingularAttribute<WorkSchedule, Date> workDate;
    public static volatile SingularAttribute<WorkSchedule, Integer> idWorkSchedule;
    public static volatile SingularAttribute<WorkSchedule, String> workShifts;
    public static volatile CollectionAttribute<WorkSchedule, EmployeeSchedule> employeeScheduleCollection;

}