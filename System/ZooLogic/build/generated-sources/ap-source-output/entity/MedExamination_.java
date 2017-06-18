package entity;

import entity.Animal;
import entity.Employee;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-18T13:48:24")
@StaticMetamodel(MedExamination.class)
public class MedExamination_ { 

    public static volatile SingularAttribute<MedExamination, String> condition;
    public static volatile SingularAttribute<MedExamination, Integer> idMedExamination;
    public static volatile SingularAttribute<MedExamination, Employee> veterinarian;
    public static volatile SingularAttribute<MedExamination, Date> medDate;
    public static volatile SingularAttribute<MedExamination, String> description;
    public static volatile SingularAttribute<MedExamination, Animal> animal;

}