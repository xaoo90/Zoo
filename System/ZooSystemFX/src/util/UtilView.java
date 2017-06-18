/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import utilLogic.UtilLogic;
import entity.Animal;
import entity.AnimalFeed;
import entity.Cage;
import entity.Employee;
import entity.EmployeeSchedule;
import entity.Feed;
import entity.Holiday;
import entity.MedExamination;
import entity.OrderFeed;
import entity.OrderPosition;
import entity.Sector;
import entity.TicketType;
import entity.Warehouse;
import entity.view.HolidayView;
import entity.view.WorkScheduleView;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import run.MainWindow;

/**
 *
 * @author Xaoo
 */
public class UtilView {

    private static Employee sellectedEmployee;
    private static Sector sellectedSector;
    private static Cage sellectedCage;
    private static Warehouse sellectedWarehouse;
    private static Feed sellectedFeed;
    private static Animal sellectedAnimal;
    private static TicketType sellectedTicketType;
    private static HolidayView sellectedHolidayView;
    private static AnimalFeed sellectedAnimalFeed;
    private static MedExamination sellectedMed;
    private static OrderPosition sellectedPosition;
    private static OrderFeed sellectedOrderFeed;
    private static EmployeeSchedule sellectedEmployeeSchedule;

    public static EmployeeSchedule getSellectedEmployeeSchedule() {
        return sellectedEmployeeSchedule;
    }

    public static void setSellectedEmployeeSchedule(EmployeeSchedule sellectedEmployeeSchedule) {
        UtilView.sellectedEmployeeSchedule = sellectedEmployeeSchedule;
    }

    public static OrderFeed getSellectedOrderFeed() {
        return sellectedOrderFeed;
    }

    public static void setSellectedOrderFeed(OrderFeed sellectedOrderFeed) {
        UtilView.sellectedOrderFeed = sellectedOrderFeed;
    }

    public static OrderPosition getSellectedPosition() {
        return sellectedPosition;
    }

    public static void setSellectedPosition(OrderPosition sellectedPosition) {
        UtilView.sellectedPosition = sellectedPosition;
    }

    private static int position;    
    private static LocalDate sellectedPlanningDate;
    
    private static List<WorkScheduleView> newSchedule = new ArrayList();

    public static List<WorkScheduleView> getNewSchedule() {
        return newSchedule;
    }

    public static void setNewSchedule(List<WorkScheduleView> newSchedule) {
        UtilView.newSchedule = newSchedule;
    }

    public static MedExamination getSellectedMed() {
        return sellectedMed;
    }

    public static void setSellectedMed(MedExamination sellectedMed) {
        UtilView.sellectedMed = sellectedMed;
    }
    public static LocalDate getSellectedPlanningDate() {
        return sellectedPlanningDate;
    }

    public static void setSellectedPlanningDate(LocalDate sellectedPlanningDate) {
        UtilView.sellectedPlanningDate = sellectedPlanningDate;
    }

    public static int getPosition() {
        return position;
    }

    public static void setPosition(int position) {
        UtilView.position = position;
    }

    public static HolidayView getSellectedHolidayView() {
        return sellectedHolidayView;
    }

    public static void setSellectedHolidayView(HolidayView sellectedHolidayView) {
        UtilView.sellectedHolidayView = sellectedHolidayView;
    }

    public static MainWindow getMain() {
        return main;
    }

    public static void setMain(MainWindow main) {
        UtilView.main = main;
    }

    private static MainWindow main = new MainWindow();

    public static TicketType getSellectedTicketType() {
        return sellectedTicketType;
    }

    public static void setSellectedTicketType(TicketType sellectedTicketType) {
        UtilView.sellectedTicketType = sellectedTicketType;
    }

    public static AnimalFeed getSellectedAnimalFeed() {
        return sellectedAnimalFeed;
    }

    public static void setSellectedAnimalFeed(AnimalFeed sellectedAnimalFeed) {
        UtilView.sellectedAnimalFeed = sellectedAnimalFeed;
    }

    public static Animal getSellectedAnimal() {
        return sellectedAnimal;
    }

    public static void setSellectedAnimal(Animal sellectedAnimal) {
        UtilView.sellectedAnimal = sellectedAnimal;
    }

    public static Feed getSellectedFeed() {
        return sellectedFeed;
    }

    public static void setSellectedFeed(Feed sellectedFeed) {
        UtilView.sellectedFeed = sellectedFeed;
    }

    public static Warehouse getSellectedWarehouse() {
        return sellectedWarehouse;
    }

    public static void setSellectedWarehouse(Warehouse sellectedWarehouse) {
        UtilView.sellectedWarehouse = sellectedWarehouse;
    }

    public static Cage getSellectedCage() {
        return sellectedCage;
    }

    public static void setSellectedCage(Cage sellectedCage) {
        UtilView.sellectedCage = sellectedCage;
    }

    public static Sector getSellectedSector() {
        return sellectedSector;
    }

    public static void setSellectedSector(Sector sellectedSector) {
        UtilView.sellectedSector = sellectedSector;
    }

    public static Employee getSellectedEmployee() {
        return sellectedEmployee;
    }

    public static void setSellectedEmployee(Employee sessionEmployee) {
        UtilView.sellectedEmployee = sessionEmployee;
    }

    public static Holiday convertHolidayView(HolidayView h1) {
        Holiday h = new Holiday();
        h.setIdHoliday(h1.getIdHoliday());
        h.setEmployee(h1.getEmployee());
        h.setStartDate(h1.getStartDatee());
        h.setEndDate(h1.getEndDatee());
        h.setType(h1.getType());
        return h;
    }

    public static int monthToInt(String month) {
        switch (month) {
            case "Styczeń":
                return 1;
            case "Luty":
                return 2;
            case "Marzec":
                return 3;
            case "Kwiecień":
                return 4;
            case "Maj":
                return 5;
            case "Czerwiec":
                return 6;
            case "Lipiec":
                return 7;
            case "Sierpień":
                return 8;
            case "Wrzesień":
                return 9;
            case "Październik":
                return 10;
            case "Listopad":
                return 11;
            case "Grudzień":
                return 12;
            default:
                return 0;
        }
    }

    public static String intToMonth(Integer i) {
        switch (i) {
            case 1:
                return "Styczeń";
            case 2:
                return "Luty";
            case 3:
                return "Marzec";
            case 4:
                return "Kwiecień";
            case 5:
                return "Maj";
            case 6:
                return "Czerwiec";
            case 7:
                return "Lipiec";
            case 8:
                return "Sierpień";
            case 9:
                return "Wrzesień";
            case 10:
                return "Październik";
            case 11:
                return "Listopad";
            case 12:
                return "Grudzień";
            default:
                return "0";
        }
    }

    public static String intToDayOfWeek(Integer i) {
        switch (i) {
            case 1:
                return "Poniedziałek";
            case 2:
                return "Wtorek";
            case 3:
                return "Środa";
            case 4:
                return "Czwartek";
            case 5:
                return "Piątek";
            case 6:
                return "Sobota";
            case 7:
                return "Niedziela";
            default:
                return "0";
        }
    }

    public static String intToPosition(Integer i) {
        switch (i) {
            case 1:
                return "Opiekun Ssaki A1";
            case 2:
                return "Opiekun Ssaki A2/A3";
            case 3:
                return "Opiekun Ptaki B1/B2";
            case 4:
                return "Opiekun Gady C1/C2";
            case 5:
                return "Opiekun Płazy D1/D2";
            case 6:
                return "Opiekun Ryby E1/E2";
            case 7:
                return "Opiekun Bezkręgowce F1";
            case 8:
                return "Weterynarz";
            case 9:
                return "Magazynier";
            case 10:
                return "Kasjer";
            default:
                return "0";
        }
    }

    public static void logout(ActionEvent event) throws IOException {
        (((Node) event.getSource()).getScene()).getWindow().hide();
        UtilLogic.destroyEmployee();
        main.mainWindow();
    }

}
