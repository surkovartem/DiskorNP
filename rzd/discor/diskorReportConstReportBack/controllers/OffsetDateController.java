package ru.rzd.discor.diskorReportConstReportBack.controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import javax.swing.text.html.ListView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OffsetDateController extends Parent {

    private ListView OffsetList;
    private CheckBox curYear, lastYear, year2019;

    public OffsetDateController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/OffsetDate.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void ActionBtnFind(){
        System.out.println("Pressed btn");
    }

    @FXML
    void initialize() {
        assert OffsetList != null : "fx:id=\"OffsetList\" was not injected: check your FXML file 'OffsetDate.fxml'.";
        assert year2019 != null : "fx:id=\"year2019\" was not injected: check your FXML file 'OffsetDate.fxml'.";
        assert lastYear != null : "fx:id=\"lastYear\" was not injected: check your FXML file 'OffsetDate.fxml'.";
        assert curYear != null : "fx:id=\"curYear\" was not injected: check your FXML file 'OffsetDate.fxml'.";
    }

    Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
