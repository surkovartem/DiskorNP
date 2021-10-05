package ru.rzd.discor.diskorReportConstReportBack.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.rzd.discor.diskorReportConstReportBack.Main;
import ru.rzd.discor.diskorReportConstReportBack.models.currentProject.chooseFormController.ChooseFormControllerObserverOutput;
import ru.rzd.discor.diskorReportConstReportBack.models.currentProject.mode.ProjectMode;
import ru.rzd.discor.diskorReportConstReportBack.models.reportRepo.Form;
import ru.rzd.discor.diskorReportConstReportBack.models.reportRepo.ObjGroup;
import rx.Observable;
import rx.subjects.PublishSubject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import static ru.rzd.discor.diskorReportConstReportBack.Main.*;

public class ChooseFormController extends AnchorPane {
    @FXML
    private TreeView<ObjGroup> TrAllCentralGroup;
    @FXML
    private TreeTableView<Form> TrTableForms;
    @FXML
    private TextField FilterFormNameElement;

    Stage stage;
    TreeItem<ObjGroup> selectedTreeItem = null;
    private final PublishSubject<ChooseFormControllerObserverOutput> chooseFormControllerEventEmitter = PublishSubject.create();

    public Observable<ChooseFormControllerObserverOutput> getChooseFormControllerEventEmitter() {
        return chooseFormControllerEventEmitter.asObservable();
    }

    Form formToSelectOnOpenComponent = null;
    ArrayList<Form> listForm = null;
    ArrayList<Form> listFilteredForm = null;

    public ChooseFormController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ChooseForm.fxml"));
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
        if (TrTableForms.getSelectionModel().getSelectedItem() == null) {
            //createAlertInfo(this.stage, "Выберите форму");
            createInfoNotification("Выберите форму");
            return;
        }
        ChooseFormControllerObserverOutput chooseFormControllerOutput = new ChooseFormControllerObserverOutput();
        chooseFormControllerOutput.setObjGroup(TrAllCentralGroup.getSelectionModel().getSelectedItem().getValue());
        chooseFormControllerOutput.setForm(TrTableForms.getSelectionModel().getSelectedItem().getValue());
        chooseFormControllerEventEmitter.onNext(chooseFormControllerOutput);
        stage.close();
    }

    @FXML
    void initialize() {
        TrAllCentralGroup.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<ObjGroup>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<ObjGroup>> observable, TreeItem<ObjGroup> oldValue, TreeItem<ObjGroup> newValue) {
                getForms(newValue.getValue().getId());
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

        TrTableForms.setShowRoot(false);
        TrTableForms.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Form>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Form>> observable, TreeItem<Form> oldValue, TreeItem<Form> newValue) {
                System.out.println("form is selected");
            }
        });
        TreeTableColumn<Form, String> colId
                = new TreeTableColumn<Form, String>("id");
        colId.setText("ИД");
        colId.setPrefWidth(220);
        colId.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Form, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Form, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getId());
            }
        });
        TreeTableColumn<Form, String> colName
                = new TreeTableColumn<Form, String>("name");
        colName.setText("Имя");
        colName.setPrefWidth(100);
        colName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Form, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Form, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getName());
            }
        });
        TreeTableColumn<Form, String> colDescription
                = new TreeTableColumn<Form, String>("description");
        colDescription.setText("Описание");
        colDescription.setPrefWidth(150);
        colDescription.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Form, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Form, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getDescription());
            }
        });
        TreeTableColumn<Form, String> colTypeForm
                = new TreeTableColumn<Form, String>("typeForm");
        colTypeForm.setText("Тип формы");
        colTypeForm.setPrefWidth(100);
        colTypeForm.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Form, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Form, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getTypeForm());
            }
        });
        TreeTableColumn<Form, String> colActive
                = new TreeTableColumn<Form, String>("activeTip");
        colActive.setText("Признак публикации");
        colActive.setPrefWidth(100);
        colActive.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Form, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Form, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getActiveTip() ? "+" : "-");
            }
        });
        TrTableForms.getColumns().addAll(colId, colName, colDescription, colTypeForm, colActive);

        FilterFormNameElement.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setTrTableForms();
            }
        });
    }

    //Вызывается из ConstructorModeController, чтобы передать параметры
    //В любом случаем вызывается после initialize, так как controller и initialize вызываются при загрузке компоненты
    public void setInitialParams(
            ObjGroup oldObjGroup,
            Form oldForm,
            ProjectMode projectMode
    ) {
        this.formToSelectOnOpenComponent = oldForm;

        //Получение дерева групп
        ArrayList<ObjGroup> listObjGroup;
        ObjGroup rootObjGroup = null;
        try {
            rootObjGroup = Main.reportRepoService.getGroupTree("false", new SimpleDateFormat("yyyy-MM-dd").format(new Date()), "form", projectMode.getObjGroupType());
        } catch (Exception e) {
            e.printStackTrace();
            chooseFormControllerEventEmitter.onError(new Exception("Ошибка при получении дерева групп форм\n" + e.getMessage()));
            this.stage.close();
            return;
        }
        TreeItem<ObjGroup> rootTreeItem = new TreeItem<ObjGroup>(rootObjGroup);
        TrAllCentralGroup.setRoot(rootTreeItem);
        setTreeElementChildren(rootTreeItem.getValue().getListChildrenObjGroup(), rootTreeItem, oldObjGroup);
        // Выделение выбранного до этого элемента
        if (selectedTreeItem != null) {
            TrAllCentralGroup.getSelectionModel().select(selectedTreeItem);
        }
    }

    public void getForms(String objGroupId) {
        try {
            listForm = Main.reportRepoService.getListByObjGroupId(objGroupId, false);
        } catch (Exception e) {
            createAlertWarning(this.stage, "Ошибка при получении форм\n" + e.getMessage());
            return;
        }
        setTrTableForms();
    }

    private void setTrTableForms() {
        filterListFormByFilterFormName();
        TreeItem<Form> itemRoot = new TreeItem<Form>(new Form());
        ObservableList<TreeItem<Form>> listChildrenRoot = itemRoot.getChildren();
        for (Form form : listFilteredForm) {
            listChildrenRoot.add(new TreeItem<Form>(form));
        }
        TrTableForms.setRoot(itemRoot);
        if (this.formToSelectOnOpenComponent != null) {
            for (TreeItem<Form> itemTree : itemRoot.getChildren()) {
                if (Form.equals(this.formToSelectOnOpenComponent, itemTree.getValue())) {
                    TrTableForms.getSelectionModel().select(itemTree);
                }
            }
            this.formToSelectOnOpenComponent = null;
        }
    }

    private void filterListFormByFilterFormName() {
        if (listForm == null) {
            listFilteredForm = new ArrayList<>();
            return;
        }
        listFilteredForm = listForm.stream().filter((v) -> v.getName().indexOf(FilterFormNameElement.getText()) != -1).collect(Collectors.toCollection(ArrayList::new));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
