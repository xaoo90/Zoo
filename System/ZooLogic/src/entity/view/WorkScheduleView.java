/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.view;

import entity.Employee;
import entity.EmployeeSchedule;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import utilLogic.UtilLogic;

public class WorkScheduleView {

    private Integer idEmployeeSchedule;
    private String position;
    private Employee employee;
    private Date workDate;
    private String workShifts;

    public WorkScheduleView() {
    }

    public WorkScheduleView(Integer idEmployeeSchedule, String position, Employee employee, Date workDate, String workShifts) {
        this.idEmployeeSchedule = idEmployeeSchedule;
        this.position = position;
        this.employee = employee;
        this.workDate = workDate;
        this.workShifts = workShifts;
    }

    public Integer getIdEmployeeSchedule() {
        return idEmployeeSchedule;
    }

    public void setIdEmployeeSchedule(Integer idEmployeeSchedule) {
        this.idEmployeeSchedule = idEmployeeSchedule;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getWorkDatee() {
        return workDate;
    }

    public String getWorkDate() {
        return UtilLogic.dateFormat(workDate);
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public String getWorkShifts() {
        return workShifts;
    }

    public void setWorkShifts(String workShifts) {
        this.workShifts = workShifts;
    }

    public ArrayList<WorkScheduleView> workSchedule(List<EmployeeSchedule> employeeSchedule, LocalDate wd) {
        ArrayList<WorkScheduleView> lstWorkSchedule = new ArrayList();
        for (EmployeeSchedule es : employeeSchedule) {
            if (wd == null) {
                lstWorkSchedule.add(new WorkScheduleView(es.getIdEmployeeSchedule(), es.getPosition(),
                        es.getEmployee(), es.getWorkSchedule().getWorkDate(), es.getWorkSchedule().getWorkShifts()));
            } else {
                Date d = Date.from(wd.atStartOfDay(ZoneId.systemDefault()).toInstant());
                if (es.getWorkSchedule().getWorkDate().equals(d)) {
                    lstWorkSchedule.add(new WorkScheduleView(es.getIdEmployeeSchedule(), es.getPosition(),
                            es.getEmployee(), es.getWorkSchedule().getWorkDate(), es.getWorkSchedule().getWorkShifts()));
                }
            }
        }
        return lstWorkSchedule;
    }

    @Override
    public String toString() {
        return "WorkScheduleView{" + "idEmployeeSchedule=" + idEmployeeSchedule + ", position=" + position + ", employee=" + employee + ", workDate=" + workDate + ", workShifts=" + workShifts + '}';
    }

}
