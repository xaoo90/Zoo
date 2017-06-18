/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Xaoo
 */
@Entity
@Table(name = "EMPLOYEE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findByIdEmployee", query = "SELECT e FROM Employee e WHERE e.idEmployee = :idEmployee"),
    @NamedQuery(name = "Employee.findByLogin", query = "SELECT e FROM Employee e WHERE e.login = :login"),
    @NamedQuery(name = "Employee.findByPassword", query = "SELECT e FROM Employee e WHERE e.password = :password"),
    @NamedQuery(name = "Employee.findByFirstname", query = "SELECT e FROM Employee e WHERE e.firstName = :firstName"),
    @NamedQuery(name = "Employee.findByLastname", query = "SELECT e FROM Employee e WHERE e.lastName = :lastName"),
    @NamedQuery(name = "Employee.findByPosition", query = "SELECT e FROM Employee e WHERE e.position = :position"),
    @NamedQuery(name = "Employee.findByEmploymentdate", query = "SELECT e FROM Employee e WHERE e.employmentDate = :employmentDate"),
    @NamedQuery(name = "Employee.findByEndingdate", query = "SELECT e FROM Employee e WHERE e.endingDate = :endingDate"),
    @NamedQuery(name = "Employee.findBySalary", query = "SELECT e FROM Employee e WHERE e.salary = :salary"),
    @NamedQuery(name = "Employee.findByBankaccountnumber", query = "SELECT e FROM Employee e WHERE e.bankAccountNumber = :bankAccountNumber"),
    @NamedQuery(name = "Employee.findByHoliday", query = "SELECT e FROM Employee e WHERE e.holiday = :holiday"),
    @NamedQuery(name = "Employee.findByEmail", query = "SELECT e FROM Employee e WHERE e.email = :email"),
    @NamedQuery(name = "Employee.findByCondition", query = "SELECT e FROM Employee e WHERE e.condition = :condition"),
    @NamedQuery(name = "Employee.findDistinctPosition", query = "SELECT DISTINCT e.position FROM Employee e"),
    @NamedQuery(name = "Employee.findMaxId", query = "SELECT max(e.idEmployee) FROM Employee e"),
    @NamedQuery(name = "Employee.findDistinctCondition", query = "SELECT DISTINCT e.condition FROM Employee e")})
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDEMPLOYEE")
    private Integer idEmployee;
    @Basic(optional = false)
    @Column(name = "LOGIN")
    private String login;
    @Basic(optional = false)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @Column(name = "FIRSTNAME")
    private String firstName;
    @Basic(optional = false)
    @Column(name = "LASTNAME")
    private String lastName;
    @Basic(optional = false)
    @Column(name = "POSITION")
    private String position;
    @Basic(optional = false)
    @Column(name = "EMPLOYMENTDATE")
    @Temporal(TemporalType.DATE)
    private Date employmentDate;
    @Column(name = "ENDINGDATE")
    @Temporal(TemporalType.DATE)
    private Date endingDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "SALARY")
    private BigDecimal salary;
    @Basic(optional = false)
    @Column(name = "BANKACCOUNTNUMBER")
    private String bankAccountNumber;
    @Basic(optional = false)
    @Column(name = "HOLIDAY")
    private short holiday;
    @Basic(optional = false)
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @Column(name = "CONDITION")
    private String condition;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "warehouseMen")
    private Collection<OrderFeed> orderFeedCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<EmployeeSchedule> employeeScheduleCollection;
    @OneToMany(mappedBy = "manager")
    private Collection<Sector> sectorCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "veterinarian")
    private Collection<MedExamination> medExaminationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<Holiday> holidayCollection;

    public Employee() {
    }

    public Employee(Integer idemployee) {
        this.idEmployee = idemployee;
    }

    public Employee(Integer idEmployee, String login, String password, String firstName, String lastName, String position, Date employmentDate, BigDecimal salary, String bankAccountNumber, short holiday, String email, String condition) {
        this.idEmployee = idEmployee;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.employmentDate = employmentDate;
        this.salary = salary;
        this.bankAccountNumber = bankAccountNumber;
        this.holiday = holiday;
        this.email = email;
        this.condition = condition;
    }

    public Integer getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Integer idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(Date employmentDate) {
        this.employmentDate = employmentDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public short getHoliday() {
        return holiday;
    }

    public void setHoliday(short holiday) {
        this.holiday = holiday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @XmlTransient
    public Collection<OrderFeed> getOrderFeedCollection() {
        return orderFeedCollection;
    }

    public void setOrderFeedCollection(Collection<OrderFeed> orderFeedCollection) {
        this.orderFeedCollection = orderFeedCollection;
    }

    @XmlTransient
    public Collection<EmployeeSchedule> getEmployeeScheduleCollection() {
        return employeeScheduleCollection;
    }

    public void setEmployeeScheduleCollection(Collection<EmployeeSchedule> employeeScheduleCollection) {
        this.employeeScheduleCollection = employeeScheduleCollection;
    }

    @XmlTransient
    public Collection<Sector> getSectorCollection() {
        return sectorCollection;
    }

    public void setSectorCollection(Collection<Sector> sectorCollection) {
        this.sectorCollection = sectorCollection;
    }

    @XmlTransient
    public Collection<MedExamination> getMedExaminationCollection() {
        return medExaminationCollection;
    }

    public void setMedExaminationCollection(Collection<MedExamination> medExaminationCollection) {
        this.medExaminationCollection = medExaminationCollection;
    }

    @XmlTransient
    public Collection<Holiday> getHolidayCollection() {
        return holidayCollection;
    }

    public void setHolidayCollection(Collection<Holiday> holidayCollection) {
        this.holidayCollection = holidayCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmployee != null ? idEmployee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.idEmployee == null && other.idEmployee != null) || (this.idEmployee != null && !this.idEmployee.equals(other.idEmployee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
    
}
