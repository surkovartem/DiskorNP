package ru.rzd.discor.diskorReportConstReportBack.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.rzd.discor.diskorReportConstReportBack.Main;
import ru.rzd.discor.diskorReportConstReportBack.models.currentProject.chooseObjGroupController.ChooseObjGroupControllerObserverOutput;
import ru.rzd.discor.diskorReportConstReportBack.models.currentProject.mode.ProjectMode;
import ru.rzd.discor.diskorReportConstReportBack.models.reportRepo.ObjGroup;
import rx.Observable;
import rx.subjects.PublishSubject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static ru.rzd.discor.diskorReportConstReportBack.Main.*;

public class ChooseObjGroupController extends AnchorPane {
    @FXML
    private TextField NameForm;

    @FXML
    private TreeView<ObjGroup> TrAllCentralGroup;
    TreeItem<ObjGroup> selectedTreeItem = null;
    Stage stage;

    //@Output
    private final PublishSubject<ChooseObjGroupControllerObserverOutput> chooseObjGroupControllerEventEmitter = PublishSubject.create();

    public Observable<ChooseObjGroupControllerObserverOutput> getChooseObjGroupControllerEventEmitter() {
        return this.chooseObjGroupControllerEventEmitter.asObservable();
    }

    public ChooseObjGroupController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ChooseObjGroup.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    //Отрисовка treeView
    private void setTreeElementChildren(
            ArrayList<ObjGroup> listChildrenObjGroup,
            TreeItem<ObjGroup> parentElement,
            ObjGroup oldObjGroup
    ) {
        if (listChildrenObjGroup == null) {
            return;
        }
        for (ObjGroup childObjGroup : listChildrenObjGroup) {
            if (childObjGroup.getCorTip().equals("D")) {
                continue;
            }
            TreeItem<ObjGroup> childRoot = new TreeItem<ObjGroup>(childObjGroup);
            if (oldObjGroup != null && childObjGroup.getId().equals(oldObjGroup.getId())) {
                selectedTreeItem = childRoot;
            }
            parentElement.getChildren().add(childRoot);
            setTreeElementChildren(childObjGroup.getListChildrenObjGroup(), childRoot, oldObjGroup);
        }
    }

    @FXML
    void BtOpenPressed(ActionEvent event) {
        if (TrAllCentralGroup.getSelectionModel().getSelectedItem() == null) {
            //createAlertInfo(this.stage, "Выберите группу форм");
            createInfoNotification("Выберите группу форм");
            return;
        }
        if (NameForm.getText() == null || NameForm.getText().length() == 0) {
            //createAlertInfo(this.stage, "Ввведите имя формы");
            createInfoNotification("Ввведите имя формы");
            return;
        }
        ChooseObjGroupControllerObserverOutput chooseObjGroupControllerObserverOutput = new ChooseObjGroupControllerObserverOutput();
        chooseObjGroupControllerObserverOutput.setObjGroup(TrAllCentralGroup.getSelectionModel().getSelectedItem().getValue());
        chooseObjGroupControllerObserverOutput.setFormName(NameForm.getText());
        chooseObjGroupControllerEventEmitter.onNext(chooseObjGroupControllerObserverOutput);
        stage.close();
    }

    @FXML
    void initialize() {
        TrAllCentralGroup.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<ObjGroup>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<ObjGroup>> observable, TreeItem<ObjGroup> oldValue, TreeItem<ObjGroup> newValue) {

            }
        });
        TrAllCentralGroup.setCellFactory((TreeView<ObjGroup> treeView) -> {
            return new TextFieldTreeCell<ObjGroup>() {
                @Override
                public void updateItem(ObjGroup objGroup, boolean isEmpty) {
                    super.updateItem(objGroup, isEmpty);
                    if (objGroup != null) {
                        setText("[" + objGroup.getId() + "] " + objGroup.getName());
                    }
                }
            };
        });
    }

    //Вызывается из ConstructorModeController, чтобы передать параметры
    //В любом случаем вызывается после initialize, так как controller и initialize вызываются при загрузке компоненты
    public void setInitialParams(
            ObjGroup oldObjGroup,
            ProjectMode projectMode
    ) {
        //Получение дерева групп
        ArrayList<ObjGroup> listObjGroup;
        ObjGroup rootObjGroup = null;
        try {
            rootObjGroup = Main.reportRepoService.getGroupTree("false", new SimpleDateFormat("yyyy-MM-dd").format(new Date()), "form", projectMode.getObjGroupType());
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "Ошибка при получении дерева групп форм\n" + e.getMessage());
            this.stage.close();
        }
        TreeItem<ObjGroup> rootTreeItem = new TreeItem<ObjGroup>(rootObjGroup);
        TrAllCentralGroup.setRoot(rootTreeItem);
        setTreeElementChildren(rootTreeItem.getValue().getListChildrenObjGroup(), rootTreeItem, oldObjGroup);
        // Выделение выбранного до этого элемента
        if (selectedTreeItem != null) {
            TrAllCentralGroup.getSelectionModel().select(selectedTreeItem);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
