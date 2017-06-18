package util;

import java.time.LocalDate;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerts {

    public static boolean isOnlyLetter(String s) {
        return !s.matches("[a-zA-Z]+");
    }

    public static boolean isOnlyNumber(String s) {
        return !s.matches("[0-9]+");
    }

    public static boolean isNull(String s) {
        return s.isEmpty();
    }
    public static boolean isNull(Object o) {
        return o == null;
    }
    
    public static boolean isNull(LocalDate ld) {
        return ld == null;
    }

    public static boolean isLonger(String s, int l) {
        return s.length() > l;
    }

    public static boolean isShorter(String s, int l) {
        return s.length() < l;
    }
    
    public static boolean isBigger(float l1, float l2) {
        return l1 > l2;
    }

    public static boolean isSmaller(float l1, float l2) {
        return l1 < l2;
    }
    
    public static boolean isAfter(LocalDate ld1, LocalDate ld2) {
        return ld1.isAfter(ld2);
    }

    private static String alerts = "";
    public static String getAlerts() {
        return alerts;
    }
    public static void setAlerts(String alerts) {
        Alerts.alerts += alerts;
    }
    public static void alertsClear() {
        Alerts.alerts = "";
    }
    public static Alert returnAlert(String title, String hText, AlertType at) {
        if (!isNull(alerts)) {
            Alert alert = new Alert(at);
            alert.setTitle(title);
            alert.setHeaderText(hText);
            alert.setContentText(alerts);
            alert.showAndWait();
            return alert;
        } else {
            return null;
        }
    }

}
