/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilLogic;

import entity.Employee;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UtilLogic implements Serializable{
    
    public static Employee sessionEmployee;

    public static Employee getSessionEmployee() {
        return sessionEmployee;
    }
    public static void setSessionEmployee(Employee sessionEmployee) {
        UtilLogic.sessionEmployee = sessionEmployee;
    }
    public static void destroyEmployee(){
        setSessionEmployee(null);
    }  
    public static String dateFormat(Date date){
        SimpleDateFormat simpleDateHere = new SimpleDateFormat("yyyy-MM-dd");
        return  simpleDateHere.format(date);
    };
}