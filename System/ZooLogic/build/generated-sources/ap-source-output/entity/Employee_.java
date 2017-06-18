package entity;

import entity.EmployeeSchedule;
import entity.Holiday;
import entity.MedExamination;
import entity.OrderFeed;
import entity.Sector;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(Employee.class)
public class Employee_ { 

    public static volatile SingularAttribute<Employee, String> lastName;
    public static volatile CollectionAttribute<Employee, MedExamination> medExaminationCollection;
    public static volatile CollectionAttribute<Employee, Sector> sectorCollection;
    public static volatile SingularAttribute<Employee, Date> employmentDate;
    public static volatile SingularAttribute<Employee, String> login;
    public static volatile SingularAttribute<Employee, BigDecimal> salary;
    public static volatile SingularAttribute<Employee, Short> holiday;
    public static volatile CollectionAttribute<Employee, EmployeeSchedule> employeeScheduleCollection;
    public static volatile SingularAttribute<Employee, Integer> idEmployee;
    public static volatile SingularAttribute<Employee, String> firstName;
    public static volatile SingularAttribute<Employee, String> password;
    public static volatile SingularAttribute<Employee, String> condition;
    public static volatile SingularAttribute<Employee, Date> endingDate;
    public static volatile CollectionAttribute<Employee, OrderFeed> orderFeedCollection;
    public static volatile SingularAttribute<Employee, String> bankAccountNumber;
    public static volatile SingularAttribute<Employee, String> position;
    public static volatile SingularAttribute<Employee, String> email;
    public static volatile CollectionAttribute<Employee, Holiday> holidayCollection;

}