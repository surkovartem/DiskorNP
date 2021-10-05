package ru.rzd.discor.diskorReportConstReportBack.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import ru.rzd.discor.diskorReportConstReportBack.Main;

import java.io.IOException;

public class LogInController extends GridPane {
    @FXML
    private WebView webView;

    public LogInController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/LogIn.fxml"));
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
    void initialize() {
        final WebEngine web = webView.getEngine();
        web.getLoadWorker().stateProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue == Worker.State.SUCCEEDED) {

                    String token = (String) web.executeScript("document.body.innerText");
                    if (token.contains("Bearer")) {
                        System.out.println(token);
                        Main.tokenToSave = token;
                        Stage stage = (Stage) webView.getScene().getWindow();
                        stage.close();
                    }
                }
            }
        });
        web.load(Main.urlWebAuthorization);
    }

    Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
