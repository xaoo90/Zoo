/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package veterinarian.animal;

import dao.MedExaminationFacade;
import entity.Animal;
import entity.MedExamination;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.UtilView;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class AnimalMedFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    MedExaminationFacade medF = new MedExaminationFacade(emf);

    ObservableList<MedExamination> lstHistoryMed = FXCollections.observableArrayList(medF.getHistoryMed());
    ObservableList<MedExamination> lstPlanMed = FXCollections.observableArrayList(medF.getPlanMed());

    @FXML
    private TableView<MedExamination> tableHistoryMed = new TableView();
    @FXML
    private TableColumn<MedExamination, String> historyAnimal;
    @FXML
    private TableColumn<MedExamination, String> historyVet;
    @FXML
    private TableColumn<MedExamination, String> historyDate;
    @FXML
    private TableColumn<MedExamination, String> historyDesc;

    @FXML
    private TableView<MedExamination> tablePlanMed = new TableView();
    @FXML
    private TableColumn<MedExamination, String> planAnimal;
    @FXML
    private TableColumn<MedExamination, String> planVet;
    @FXML
    private TableColumn<MedExamination, String> planDate;
    @FXML
    private TableColumn<MedExamination, String> planDesc;

    @FXML
    private Tab tabPlan;

    int count = medF.countTodayPlanMed();

    public void showHistoryMed() {
        historyAnimal.setCellValueFactory(new PropertyValueFactory("animal"));
        historyVet.setCellValueFactory(new PropertyValueFactory("veterinarian"));
        historyDate.setCellValueFactory(new PropertyValueFactory("medDate"));
        historyDesc.setCellValueFactory(new PropertyValueFactory("description"));
        tableHistoryMed.setItems(lstHistoryMed);
        tableHistoryMed.getSelectionModel().selectFirst();
    }

    public void showPlanMed() {
        planAnimal.setCellValueFactory(new PropertyValueFactory("animal"));
        planVet.setCellValueFactory(new PropertyValueFactory("veterinarian"));
        planDate.setCellValueFactory(new PropertyValueFactory("medDate"));
        planDesc.setCellValueFactory(new PropertyValueFactory("description"));
        tablePlanMed.setItems(lstPlanMed);
        tablePlanMed.getSelectionModel().selectFirst();
    }

    public void showCountPlanMed() {
        if (count != 0) {
            StackPane countInCircle = new StackPane();
            Circle c = new Circle(10);
            c.setFill(Color.TRANSPARENT);
            c.setStroke(Color.RED);
            c.setStrokeWidth(2);
            Text txt = new Text(String.valueOf(count));
            txt.setFill(Color.RED);
            txt.setFont(Font.font(null, FontWeight.BOLD, 15));
            countInCircle.getChildren().addAll(c, txt);
            tabPlan.setGraphic(countInCircle);
            tabPlan.setText("Zaplanowane badania");
        }
    }
    
    @FXML
    public void makeMed() throws IOException {
        UtilView.setSellectedMed(tablePlanMed.getSelectionModel().getSelectedItem());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/veterinarian/animal/MakeMedAnimalFXML.fxml"));
        BorderPane addEmpl = loader.load();
        Stage addDialogStage = new Stage();
        addDialogStage.setTitle("Badanie");
        addDialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(addEmpl);
        addDialogStage.setScene(scene);
        addDialogStage.show();
    }
    
    @FXML
    public void editMed() throws IOException {
        UtilView.setSellectedMed(tableHistoryMed.getSelectionModel().getSelectedItem());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/veterinarian/animal/EditMedAnimalFXML.fxml"));
        BorderPane addEmpl = loader.load();
        Stage addDialogStage = new Stage();
        addDialogStage.setTitle("Badanie");
        addDialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(addEmpl);
        addDialogStage.setScene(scene);
        addDialogStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tablePlanMed.setPlaceholder(new Label("Brak zaplanowanych zabiegów medycznych"));
        showHistoryMed();
        showPlanMed();
        showCountPlanMed();
    }

}
