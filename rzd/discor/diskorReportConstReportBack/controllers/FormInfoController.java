package ru.rzd.discor.diskorReportConstReportBack.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.rzd.discor.diskorReportConstReportBack.customElements.LabelWithTooltip;
import ru.rzd.discor.diskorReportConstReportBack.models.reportRepo.Form;

import java.io.IOException;

@Getter
@Setter
@ToString
public class FormInfoController extends AnchorPane {
    Stage stage;
    @FXML
    private TextField name,
            //назван не id, так как AnchorPane имеет метод getId()
            idForm,
            idFormChain,
            fkObjGroup;

    public FormInfoController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/FormInfo.fxml"));
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
        setElementsStyle();
        setElementsAction();
    }

    public void setInitialParams(Form form) throws Exception {
        setFormInfo(form);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setElementsStyle() {

    }

    public void setElementsAction() {

    }

    private void setFormInfo(Form form) throws Exception {
        if (form == null) {
            this.stage.close();
            throw new Exception("Форма не должна быть равна null");
        }
        name.setText(form.getName() != null ? form.getName() : "");
        idForm.setText(form.getId() != null ? form.getId() : "");
        idFormChain.setText(form.getIdFormChain() != null ? form.getIdFormChain() : "");
        fkObjGroup.setText(form.getFkObjGroup() != null ? form.getFkObjGroup() : "");
    }
}
