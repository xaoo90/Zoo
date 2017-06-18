/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.view;

import entity.Employee;
import entity.Holiday;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import utilLogic.UtilLogic;
import org.joda.time.Days;
import org.joda.time.DateTime;

/**
 *
 * @author Xaoo
 */
public class HolidayView implements Serializable {

    private Integer idHoliday;
    private Date startDate;
    private Date endDate;
    private String type;
    private Employee employee;
    private int day;

    public HolidayView() {

    }

    public HolidayView(Integer idHoliday, Date startDate, Date endDate, String type, Employee employee, int day) {
        this.idHoliday = idHoliday;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.employee = employee;
        this.day = day;
    }

    public Integer getIdHoliday() {
        return idHoliday;
    }

    public void setIdHoliday(Integer idHoliday) {
        this.idHoliday = idHoliday;
    }

    public Date getStartDatee() {
        return startDate;
    }

    public String getStartDate() {
        return UtilLogic.dateFormat(startDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDatee() {
        return endDate;
    }

    public String getEndDate() {
        return UtilLogic.dateFormat(endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ArrayList<HolidayView> holiday(List<Holiday> holiday) {
        ArrayList<HolidayView> lstUrlop = new ArrayList();
        for (Holiday h : holiday) {
            DateTime d1 = new DateTime(h.getEndDatee());
            DateTime d2 = new DateTime(h.getStartDatee());

            lstUrlop.add(new HolidayView(h.getIdHoliday(), h.getStartDatee(), h.getEndDatee(),
                    h.getType(), h.getEmployee(),
                    Days.daysBetween(d2, d1).getDays()));
        }
        return lstUrlop;
    }

    @Override
    public String toString() {
        return startDate + " " + endDate + " " + type + " " + employee + " " + day;
    }

}
