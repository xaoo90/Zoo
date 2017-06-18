/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.animal.feed;

import dao.AnimalFacade;
import dao.AnimalFeedFacade;
import dao.FeedFacade;
import entity.AnimalFeed;
import entity.Feed;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.Alerts;
import util.UtilView;

public class AddAnimalFeedFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    AnimalFacade aniF = new AnimalFacade(emf);
    AnimalFeedFacade afeF = new AnimalFeedFacade(emf);
    FeedFacade feeF = new FeedFacade(emf);

    ObservableList<Feed> lstFeed = FXCollections.observableList(feeF.findFeedEntities());
    ObservableList<String> lstFeedTime = FXCollections.observableList(feedTimeDate());
    ObservableList<String> lstWeekDay = FXCollections.observableList(feedWeekDay());

    @FXML
    private Label lblSpecies;
    @FXML
    private Label lblSex;
    @FXML
    private ComboBox comboFeed;
    @FXML
    private Label lblUnit;
    @FXML
    private TextField txtPortion;
    @FXML
    private ComboBox comboFeedTime;
    @FXML
    private CheckBox boxEveryDay;
    @FXML
    private CheckBox boxOnceWeek;
    @FXML
    private CheckBox boxOnceMonth;
    @FXML
    private Label txtCheckInfo;
    @FXML
    private ComboBox comboDay;
    @FXML
    private TextField txtMonth;

    @FXML
    public void addAnimalFeed(ActionEvent event) {
        if (isValidate()) {
            AnimalFeed af = new AnimalFeed();
            af.setAnimal(UtilView.getSellectedAnimal());
            af.setFeed((Feed) comboFeed.getValue());
            af.setPortion(new BigDecimal(txtPortion.getText().replaceAll(",", ".")));
            af.setFeedTime((String) comboFeedTime.getValue());
            if (boxEveryDay.isSelected()) {
                af.setFrequency("Codziennie");
            }
            if (boxOnceWeek.isSelected()) {
                af.setFrequency((String) comboDay.getValue());
            }
            if (boxOnceMonth.isSelected()) {
                af.setFrequency(txtMonth.getText());
            }
            if (afeF.addAnimalFeed(af).equals("OK")) {
                Alerts.setAlerts("Operacja dodawania pożywienia zwierzęciu zakończona powodzeniem\n");
                Alerts.returnAlert("Dodawanie wykonana", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja dodawania pożywienia zwierzęciu zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd dodawania", null, AlertType.ERROR);
                Alerts.alertsClear();
            }
        }
    }

    public boolean isValidate() {
        boolean val = true;
        if (Alerts.isNull(txtPortion.getText())) {
            Alerts.setAlerts("Pole Porcja jest wymagane\n");
            val = false;
        } else if (Alerts.isSmaller(Float.parseFloat(txtPortion.getText().replace(",", ".")), (float) 0.01)) {
            Alerts.setAlerts("Wprowadzona wartość zbyt niska (min 0.01)\n");
            val = false;
        }
        if (boxOnceMonth.isSelected()) {
            if (Alerts.isNull(txtMonth.getText())) {
                Alerts.setAlerts("Pole Dzień miesiąca jest wymagane\n");
                val = false;
            } else {
                if (Alerts.isBigger(Float.parseFloat(txtMonth.getText()), 31)) {
                    Alerts.setAlerts("Wprowadzona wartość zbyt wysoka (max 31)\n");
                    val = false;
                }
                if (Alerts.isSmaller(Float.parseFloat(txtMonth.getText()), 1)) {
                    Alerts.setAlerts("Wprowadzona wartość zbyt niska (min 1)\n");
                    val = false;
                }
            }
        }

        Alerts.returnAlert("Dodawanie pożywienia zwierzęciu", "Błąd dodawania pożywienia zwierzęciu", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    public void showAnimalFeedInfo() {
        lblSpecies.setText(UtilView.getSellectedAnimal().getSpecies());
        lblSex.setText(UtilView.getSellectedAnimal().getSex());
        comboFeed.setItems(lstFeed);
        comboFeedTime.setItems(lstFeedTime);
        comboDay.setItems(lstWeekDay);
        boxEveryDay.setSelected(true);
        handleEveryDay();
        comboFeed.setValue(lstFeed.get(0));
        unit();
        comboFeedTime.setValue(lstFeedTime.get(0));
    }

    @FXML
    public void unit() {
        Feed f = (Feed) comboFeed.getSelectionModel().getSelectedItem();
        lblUnit.setText(f.getUnit());
    }

    public ArrayList<String> feedTimeDate() {
        ArrayList a = new ArrayList();
        a.add("Rano");
        a.add("Południe");
        a.add("Wieczór");
        return a;
    }

    public ArrayList<String> feedWeekDay() {
        ArrayList a = new ArrayList();
        a.add("Poniedziałek");
        a.add("Wtorek");
        a.add("Środa");
        a.add("Czwartek");
        a.add("Piatek");
        a.add("Sobota");
        a.add("Niedziela");
        return a;
    }

    @FXML
    public void handleEveryDay() {
        if (boxEveryDay.isSelected()) {
            boxOnceWeek.setSelected(false);
            boxOnceMonth.setSelected(false);
            txtMonth.setDisable(true);
            comboDay.setDisable(true);
            txtCheckInfo.setText(null);
        }
    }

    public void handleOnceWeek() {
        if (boxOnceWeek.isSelected()) {
            boxEveryDay.setSelected(false);
            boxOnceMonth.setSelected(false);
            txtMonth.setDisable(true);
            comboDay.setDisable(false);
            txtCheckInfo.setText("Wybierz dzień tygodnia");
            comboDay.setValue(lstWeekDay.get(0));
        }
    }

    public void handleOnceMonth() {
        if (boxOnceMonth.isSelected()) {
            boxOnceWeek.setSelected(false);
            boxEveryDay.setSelected(false);
            txtMonth.setDisable(false);
            comboDay.setDisable(true);
            txtCheckInfo.setText("Wpisz dzień miesiąca");
        }
    }

    @FXML
    public void clear(ActionEvent event) {
        comboFeed.setValue(lstFeed.get(0));
        unit();
        txtPortion.setText(null);
        comboFeedTime.setValue(lstFeedTime.get(0));
        boxEveryDay.setSelected(true);
        handleEveryDay();
        txtCheckInfo.setText(null);
        comboDay.setValue(null);
        txtMonth.setText(null);
    }

    @FXML
    public void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showAnimalFeedInfo();
    }

}
