package entity;

import entity.Employee;
import entity.WorkSchedule;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(EmployeeSchedule.class)
public class EmployeeSchedule_ { 

    public static volatile SingularAttribute<EmployeeSchedule, WorkSchedule> workSchedule;
    public static volatile SingularAttribute<EmployeeSchedule, Integer> idEmployeeSchedule;
    public static volatile SingularAttribute<EmployeeSchedule, String> position;
    public static volatile SingularAttribute<EmployeeSchedule, Employee> employee;

}