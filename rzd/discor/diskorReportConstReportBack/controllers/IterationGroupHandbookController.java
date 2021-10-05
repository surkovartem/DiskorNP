package ru.rzd.discor.diskorReportConstReportBack.controllers;

import com.sun.star.table.CellRangeAddress;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import ru.rzd.discor.diskorReportConstReportBack.Main;
import ru.rzd.discor.diskorReportConstReportBack.SetFiltersException;
import ru.rzd.discor.diskorReportConstReportBack.customElements.CheckBoxWithCallbackOnFire;
import ru.rzd.discor.diskorReportConstReportBack.customElements.ComboBoxAutoComplete;
import ru.rzd.discor.diskorReportConstReportBack.customElements.SimpleObjectPropertyWithChangeListenersArray;
import ru.rzd.discor.diskorReportConstReportBack.models.core.GroupHandbook;
import ru.rzd.discor.diskorReportConstReportBack.models.core.Handbook;
import ru.rzd.discor.diskorReportConstReportBack.models.core.HandbookInnerRecord;
import ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.dto.IterationGroupHandbookDto;
import ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.enum_.*;
import ru.rzd.discor.diskorReportConstReportBack.services.GeneralParamsService;
import rx.Observer;
import rx.Subscription;
import rx.subjects.PublishSubject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static ru.rzd.discor.diskorReportConstReportBack.ConvertColumnNameFunctions.convertNumToColString;
import static ru.rzd.discor.diskorReportConstReportBack.Main.createAlertWarning;
import static ru.rzd.discor.diskorReportConstReportBack.Main.createInfoNotification;

public class IterationGroupHandbookController extends Tab {
    private Stage stage;
    private final GeneralParamsService generalParamsService = Main.generalParamsService;
    @FXML
    private Label groupHandbookLabel;
    @FXML
    private Button groupHandbookButton;
    public SimpleObjectPropertyWithChangeListenersArray<GroupHandbook> groupHandbookProperty =
            new SimpleObjectPropertyWithChangeListenersArray<>();
    @FXML
    private ComboBoxAutoComplete<Handbook> handbookComboBox;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField cellAddressTextField;
    @FXML
    private Button cellAddressButton;
    @FXML
    private ComboBoxAutoComplete<IterationGroupHandbookDisplayedDataType> iterationGroupHandbookDisplayedDataTypeComboBox;
    @FXML
    private ComboBoxAutoComplete<IterationGroupHandbookSortDataType> iterationGroupHandbookSortDataTypeComboBox;
    @FXML
    private ComboBoxAutoComplete<IterationGroupHandbookSortOrder> iterationGroupHandbookSortOrderComboBox;
    @FXML
    private TextArea listSortHandbookRowTextArea;
    @FXML
    private CheckComboBox<HandbookInnerRecord> listSortHandbookRowCheckComboBox;
    @FXML
    private CheckBoxWithCallbackOnFire filterPresenceCheckBox;
    @FXML
    private ComboBoxAutoComplete<IterationGroupHandbookFilterType> iterationGroupHandbookFilterTypeComboBox;
    @FXML
    private TextArea listFilterHandbookRowTextArea;
    @FXML
    private CheckComboBox<HandbookInnerRecord> listFilterHandbookRowCheckComboBox;

    private final PublishSubject<IterationGroupHandbookController> removeIterationGroupHandbookControllerEventEmitter = PublishSubject.create();
    private final List<Subscription> listSubscriptionRemoveIterationGroupHandbookControllerEventEmitter = new ArrayList<>();

    public void subscribeRemoveIterationGroupHandbookControllerEventEmitter(Observer<IterationGroupHandbookController> observer) {
        listSubscriptionRemoveIterationGroupHandbookControllerEventEmitter.add(this.removeIterationGroupHandbookControllerEventEmitter.subscribe(observer));
    }

    public IterationGroupHandbookController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/IterationGroupHandbook.fxml"));
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
    private void initialize() {
        setElementsStyle();
        setElementsAction();
        setElementsValue(null);
    }

    public void destroy() {
        for (Subscription subscription : listSubscriptionRemoveIterationGroupHandbookControllerEventEmitter) {
            subscription.unsubscribe();
        }
    }

    public void setInitialParams(IterationGroupHandbookDto iterationGroupHandbookDtoToSetFilters) {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void selectGroupHandbookProperty() {
        GroupHandbook rootGroupHandbook;
        try {
            rootGroupHandbook = getRootGroupHandbook();
        } catch (Exception e) {
            groupHandbookProperty.setValueCustom(null);
            return;
        }
        openModalToSelectGroupHandbook(rootGroupHandbook);
    }

    @FXML
    void chooseCellAddress() {
        CellRangeAddress cellRangeAddress = generalParamsService.getActiveCells().getRangeAddress();
        cellAddressTextField.setText(convertNumToColString(cellRangeAddress.StartColumn) + (cellRangeAddress.StartRow + 1));
    }

    @FXML
    void removeIterationGroupHandbookController() {
        removeIterationGroupHandbookControllerEventEmitter.onNext(this);
    }

    private void setElementsStyle() {
        handbookComboBox.setCellFactory((ListView<Handbook> listView) -> {
            return new TextFieldListCell<Handbook>() {
                @Override
                public void updateItem(Handbook item, boolean isEmpty) {
                    super.updateItem(item, isEmpty);
                    if (item != null) {
                        setText("[" + item.getId() + "] " + item.getHandbookName());
                    }
                }
            };
        });
        handbookComboBox.setButtonCell(new TextFieldListCell<Handbook>() {
            @Override
            public void updateItem(Handbook item, boolean isEmpty) {
                super.updateItem(item, isEmpty);
                if (item != null) {
                    setText("[" + item.getId() + "] " + item.getHandbookName());
                }
            }
        });
        iterationGroupHandbookDisplayedDataTypeComboBox.setCellFactory((ListView<IterationGroupHandbookDisplayedDataType> listView) -> {
            return new TextFieldListCell<IterationGroupHandbookDisplayedDataType>() {
                @Override
                public void updateItem(IterationGroupHandbookDisplayedDataType item, boolean isEmpty) {
                    super.updateItem(item, isEmpty);
                    if (item != null) {
                        setText("[" + item.getTechName() + "] " + item.getRealName());
                    }
                }
            };
        });
        iterationGroupHandbookDisplayedDataTypeComboBox.setButtonCell(new TextFieldListCell<IterationGroupHandbookDisplayedDataType>() {
            @Override
            public void updateItem(IterationGroupHandbookDisplayedDataType item, boolean isEmpty) {
                super.updateItem(item, isEmpty);
                if (item != null) {
                    setText("[" + item.getTechName() + "] " + item.getRealName());
                }
            }
        });
        iterationGroupHandbookSortDataTypeComboBox.setCellFactory((ListView<IterationGroupHandbookSortDataType> listView) -> {
            return new TextFieldListCell<IterationGroupHandbookSortDataType>() {
                @Override
                public void updateItem(IterationGroupHandbookSortDataType item, boolean isEmpty) {
                    super.updateItem(item, isEmpty);
                    if (item != null) {
                        setText("[" + item.getTechName() + "] " + item.getRealName());
                    }
                }
            };
        });
        iterationGroupHandbookSortDataTypeComboBox.setButtonCell(new TextFieldListCell<IterationGroupHandbookSortDataType>() {
            @Override
            public void updateItem(IterationGroupHandbookSortDataType item, boolean isEmpty) {
                super.updateItem(item, isEmpty);
                if (item != null) {
                    setText("[" + item.getTechName() + "] " + item.getRealName());
                }
            }
        });
        iterationGroupHandbookSortOrderComboBox.setCellFactory((ListView<IterationGroupHandbookSortOrder> listView) -> {
            return new TextFieldListCell<IterationGroupHandbookSortOrder>() {
                @Override
                public void updateItem(IterationGroupHandbookSortOrder item, boolean isEmpty) {
                    super.updateItem(item, isEmpty);
                    if (item != null) {
                        setText("[" + item.getTechName() + "] " + item.getRealName());
                    }
                }
            };
        });
        iterationGroupHandbookSortOrderComboBox.setButtonCell(new TextFieldListCell<IterationGroupHandbookSortOrder>() {
            @Override
            public void updateItem(IterationGroupHandbookSortOrder item, boolean isEmpty) {
                super.updateItem(item, isEmpty);
                if (item != null) {
                    setText("[" + item.getTechName() + "] " + item.getRealName());
                }
            }
        });
        listSortHandbookRowCheckComboBox.setConverter(new StringConverter<HandbookInnerRecord>() {
            @Override
            public String toString(HandbookInnerRecord item) {
                return "[" + item.getHandbookRow().getId() + "] " + item.getHandbookRow().getName();
            }

            @Override
            public HandbookInnerRecord fromString(String string) {
                return null;
            }
        });
        iterationGroupHandbookFilterTypeComboBox.setCellFactory((ListView<IterationGroupHandbookFilterType> listView) -> {
            return new TextFieldListCell<IterationGroupHandbookFilterType>() {
                @Override
                public void updateItem(IterationGroupHandbookFilterType item, boolean isEmpty) {
                    super.updateItem(item, isEmpty);
                    if (item != null) {
                        setText("[" + item.getTechName() + "] " + item.getRealName());
                    }
                }
            };
        });
        iterationGroupHandbookFilterTypeComboBox.setButtonCell(new TextFieldListCell<IterationGroupHandbookFilterType>() {
            @Override
            public void updateItem(IterationGroupHandbookFilterType item, boolean isEmpty) {
                super.updateItem(item, isEmpty);
                if (item != null) {
                    setText("[" + item.getTechName() + "] " + item.getRealName());
                }
            }
        });
        listFilterHandbookRowCheckComboBox.setConverter(new StringConverter<HandbookInnerRecord>() {
            @Override
            public String toString(HandbookInnerRecord item) {
                return "[" + item.getHandbookRow().getId() + "] " + item.getHandbookRow().getName();
            }

            @Override
            public HandbookInnerRecord fromString(String string) {
                return null;
            }
        });
    }

    private void setElementsAction() {
        groupHandbookProperty.setCallBackForSetValueCustom(null, this::onSelectGroupHandbook);
        groupHandbookProperty.addChangeListener(new ChangeListener<GroupHandbook>() {
            @Override
            public void changed(ObservableValue<? extends GroupHandbook> observable, GroupHandbook oldValue, GroupHandbook newValue) {
                onSelectGroupHandbook(null);
            }
        });
        handbookComboBox.setCallBackForSetValueCustom(null, this::onSelectHandbook);
        handbookComboBox.addActionEventHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onSelectHandbook(null);
            }
        });
        filterPresenceCheckBox.setCallBackForSelectCustom(null, this::onChangeFilterPresenceCheckBox);
        filterPresenceCheckBox.addActionEventHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onChangeFilterPresenceCheckBox(null);
            }
        });
        listFilterHandbookRowCheckComboBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<HandbookInnerRecord>() {
            @Override
            public void onChanged(Change<? extends HandbookInnerRecord> c) {
                String text = "";
                for (HandbookInnerRecord handbookInnerRecord : listFilterHandbookRowCheckComboBox.getCheckModel().getCheckedItems()) {
                    text += handbookInnerRecord.getHandbookRow().getCode();
                    if (listFilterHandbookRowCheckComboBox.getCheckModel().getCheckedItems().indexOf(handbookInnerRecord) != listFilterHandbookRowCheckComboBox.getCheckModel().getCheckedItems().size() - 1) {
                        text += ", ";
                    }
                }
                listFilterHandbookRowTextArea.setText(text);
            }
        });
        SimpleStringProperty listFilterHandbookRowTextAreaTextBeforeChange = new SimpleStringProperty();
        listFilterHandbookRowTextArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    listFilterHandbookRowTextAreaTextBeforeChange.setValue(listFilterHandbookRowTextArea.getText());
                    return;
                }
                List<String> listHandbookRowStr = Arrays.asList(listFilterHandbookRowTextArea.getText().split(", "));
                List<Integer> listHandbookRowInt;
                try {
                    listHandbookRowInt = listHandbookRowStr.stream().map((str) -> Integer.parseInt(str)).collect(Collectors.toCollection(ArrayList::new));
                } catch (Exception e) {
                    listFilterHandbookRowTextArea.setText(listFilterHandbookRowTextAreaTextBeforeChange.getValue());
                    return;
                }
                List<Integer> listIndices = new ArrayList<>();
                for (Integer code : listHandbookRowInt) {
                    if (listFilterHandbookRowCheckComboBox.getItems().size() == 0) {
                        //createAlertWarning(stage, "Лист кодов сортировки пуст");
                        createInfoNotification("Лист кодов сортировки пуст");
                        listFilterHandbookRowTextArea.setText(listFilterHandbookRowTextAreaTextBeforeChange.getValue());
                        return;
                    }
                    for (HandbookInnerRecord handbookInnerRecord : listFilterHandbookRowCheckComboBox.getItems()) {
                        if (handbookInnerRecord.getHandbookRow().getCode().equals(code)) {
                            listIndices.add(listFilterHandbookRowCheckComboBox.getItems().indexOf(handbookInnerRecord));
                            break;
                        }
                        if (listFilterHandbookRowCheckComboBox.getItems().indexOf(handbookInnerRecord) == listFilterHandbookRowCheckComboBox.getItems().size() - 1) {
                            //createAlertWarning(stage, "Кода " + code + " нет в списке");
                            createInfoNotification("Кода " + code + " нет в списке");
                            listFilterHandbookRowTextArea.setText(listFilterHandbookRowTextAreaTextBeforeChange.getValue());
                            return;
                        }
                    }
                }
                int[] array = listIndices.stream().mapToInt(i -> i).toArray();
                listFilterHandbookRowCheckComboBox.getCheckModel().clearChecks();
                listFilterHandbookRowCheckComboBox.getCheckModel().checkIndices(array);
            }
        });
        listSortHandbookRowCheckComboBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<HandbookInnerRecord>() {
            @Override
            public void onChanged(Change<? extends HandbookInnerRecord> c) {
                String text = "";
                for (HandbookInnerRecord handbookInnerRecord : listSortHandbookRowCheckComboBox.getCheckModel().getCheckedItems()) {
                    text += handbookInnerRecord.getHandbookRow().getCode();
                    if (listSortHandbookRowCheckComboBox.getCheckModel().getCheckedItems().indexOf(handbookInnerRecord) != listSortHandbookRowCheckComboBox.getCheckModel().getCheckedItems().size() - 1) {
                        text += ", ";
                    }
                }
                listSortHandbookRowTextArea.setText(text);
            }
        });
        SimpleStringProperty listTextSortHandbookRowTextAreaTextBeforeChange = new SimpleStringProperty();
        listSortHandbookRowTextArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    listTextSortHandbookRowTextAreaTextBeforeChange.setValue(listSortHandbookRowTextArea.getText());
                    return;
                }
                List<String> listHandbookRowStr = Arrays.asList(listSortHandbookRowTextArea.getText().split(", "));
                List<Integer> listHandbookRowInt;
                try {
                    listHandbookRowInt = listHandbookRowStr.stream().map((str) -> Integer.parseInt(str)).collect(Collectors.toCollection(ArrayList::new));
                } catch (Exception e) {
                    listSortHandbookRowTextArea.setText(listTextSortHandbookRowTextAreaTextBeforeChange.getValue());
                    return;
                }
                List<Integer> listIndices = new ArrayList<>();
                for (Integer code : listHandbookRowInt) {
                    if (listSortHandbookRowCheckComboBox.getItems().size() == 0) {
                        //createAlertWarning(stage, "Лист кодов сортировки пуст");
                        createInfoNotification("Лист кодов сортировки пуст");
                        listSortHandbookRowTextArea.setText(listTextSortHandbookRowTextAreaTextBeforeChange.getValue());
                        return;
                    }
                    for (HandbookInnerRecord handbookInnerRecord : listSortHandbookRowCheckComboBox.getItems()) {
                        if (handbookInnerRecord.getHandbookRow().getCode().equals(code)) {
                            listIndices.add(listSortHandbookRowCheckComboBox.getItems().indexOf(handbookInnerRecord));
                            break;
                        }
                        if (listSortHandbookRowCheckComboBox.getItems().indexOf(handbookInnerRecord) == listSortHandbookRowCheckComboBox.getItems().size() - 1) {
                            //createAlertWarning(stage, "Кода " + code + " нет в списке");
                            createInfoNotification("Кода " + code + " нет в списке");
                            listSortHandbookRowTextArea.setText(listTextSortHandbookRowTextAreaTextBeforeChange.getValue());
                            return;
                        }
                    }
                }
                int[] array = listIndices.stream().mapToInt(i -> i).toArray();
                listSortHandbookRowCheckComboBox.getCheckModel().clearChecks();
                listSortHandbookRowCheckComboBox.getCheckModel().checkIndices(array);
            }
        });
        groupHandbookLabel.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if (groupHandbookProperty.getValue() == null) {
                    return "";
                }
                return "[" + groupHandbookProperty.getValue().getId() + "]" + groupHandbookProperty.getValue().getName();
            }
        }, groupHandbookProperty));
        textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return nameTextField.getText();
            }
        }, nameTextField.textProperty()));
    }

    private void setElementsValue(IterationGroupHandbookDto iterationGroupHandbookDtoToSetFilters) {
        setGroupHandbookProperty(iterationGroupHandbookDtoToSetFilters);
        setListIterationGroupHandbookDisplayedDataTypeAndSelectElement(iterationGroupHandbookDtoToSetFilters);
        setListIterationGroupHandbookSortDataTypeComboBoxAndSelectElement(iterationGroupHandbookDtoToSetFilters);
        setListIterationGroupHandbookSortOrderComboBoxAndSelectElement(iterationGroupHandbookDtoToSetFilters);
        setListSortHandbookRowCheckComboBoxAndSelectElements(iterationGroupHandbookDtoToSetFilters);
        setListFilterHandbookRowCheckComboBoxAndSelectElements(iterationGroupHandbookDtoToSetFilters);
    }

    private GroupHandbook getRootGroupHandbook() {
        //Получение дерева групп классификаторов
        ArrayList<GroupHandbook> listGroupHandbook;
        try {
            listGroupHandbook = Main.coreService.getGroupHandbookTree();
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "Ошибка при получении дерева групп классификаторов\n" + e.getMessage());
            throw new SetFiltersException("Ошибка при получении дерева групп показателей", e);
        }

        GroupHandbook rootGroupHandbook = new GroupHandbook();
        rootGroupHandbook.setListGroupHandbook(listGroupHandbook);

        return rootGroupHandbook;
    }


    private void setGroupHandbookProperty(IterationGroupHandbookDto iterationGroupHandbookDtoToSetFilters) {
        if (iterationGroupHandbookDtoToSetFilters == null) {
            groupHandbookProperty.setValueCustom(null);
            return;
        }

        GroupHandbook rootGroupHandbook;
        try {
            rootGroupHandbook = getRootGroupHandbook();
        } catch (Exception e) {
            groupHandbookProperty.setValueCustom(null);
            return;
        }

        Handbook handbook;
        try {
            handbook = Main.coreService.getHandbookById(iterationGroupHandbookDtoToSetFilters.getIdHandbook());
        } catch (Exception e) {
            e.printStackTrace();
            throw new SetFiltersException("Ошибка при получении объекта классификатора по ИД классификатора в объекте iterationGroupHandbookDtoToSetFilters", e);
        }
        if (handbook == null) {
            throw new SetFiltersException("Не найден классификатор по ИД классификатора в объекте iterationGroupHandbookDtoToSetFilters");
        }
        GroupHandbook suitableGroup = findSuitableGroupInChildrenElements(rootGroupHandbook.getListGroupHandbook(), handbook.getFkGroupHandbook());
        if (suitableGroup == null) {
            throw new SetFiltersException("Не найдена группа классификаторов из iterationGroupHandbookDtoToSetFilters в дереве групп классификаторов");
        }
        groupHandbookProperty.setValueCustom(suitableGroup, null, iterationGroupHandbookDtoToSetFilters);
    }

    public void openModalToSelectGroupHandbook(GroupHandbook rootGroupHandbook) {
        GroupHandbook oldGroupHandbook = groupHandbookProperty.getValue();
//        deactivateFiltersForSelectGroupPok();
        GroupHandbookController groupHandbookController;
        try {
            groupHandbookController = new GroupHandbookController();
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "Ошибка при открытии модального окна по выбору группы классификаторов\n" + e.getMessage());
            groupHandbookProperty.setValueCustom(null);
            return;
        }
        Stage stage = new Stage();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //Группа показателей не была выбрана, а модальное окно было закрыто
                if (groupHandbookProperty.getValue() == null) {
                    groupHandbookProperty.setValueCustom(null);
                }
            }
        });
        stage.setScene(new Scene(groupHandbookController));
        stage.setTitle("Выбор группы показателей");
        stage.getIcons().add(new javafx.scene.image.Image(ConstructorModeController.class.getResourceAsStream(
                "/assets/icon_32.png")));

//        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        groupHandbookController.setInitialParams(this, rootGroupHandbook, oldGroupHandbook);
        groupHandbookController.setStage(stage);
        stage.showAndWait();
    }

    private GroupHandbook findSuitableGroupInChildrenElements(List<GroupHandbook> listChildrenGroupHandbook, Integer idGroupHandbook) {
        if (listChildrenGroupHandbook == null) {
            return null;
        }
        for (GroupHandbook childGroupHandbook : listChildrenGroupHandbook) {
            if (childGroupHandbook.getCorTip().equals("D")) {
                continue;
            }
            if (childGroupHandbook.getId().equals(idGroupHandbook)) {
                return childGroupHandbook;
            }
            GroupHandbook suitableGroupInChildrenElements = findSuitableGroupInChildrenElements(childGroupHandbook.getListGroupHandbook(), idGroupHandbook);
            if (suitableGroupInChildrenElements != null) {
                return suitableGroupInChildrenElements;
            }
        }
        return null;
    }

    private void setListHandbookAndSelectElement(IterationGroupHandbookDto iterationGroupHandbookDtoToSetFilters) {
        ArrayList<Handbook> listHandbook;
        Integer groupHandbookId = groupHandbookProperty.getValue() != null ? groupHandbookProperty.getValue().getId() : null;
        if (groupHandbookId == null) {
            if (iterationGroupHandbookDtoToSetFilters == null) {
                handbookComboBox.setDisable(true);
                handbookComboBox.clear();
                return;
            }
            throw new SetFiltersException("ИД группы классификатора равен null");
        }
        try {
            listHandbook = Main.coreService.getListHandbook(groupHandbookId);
        } catch (Exception e) {
            e.printStackTrace();
            if (iterationGroupHandbookDtoToSetFilters == null) {
                createAlertWarning(this.stage, "Ошибка при получении списка классификаторов\n" + e.getMessage());
                handbookComboBox.setDisable(true);
                handbookComboBox.clear();
                return;
            }
            throw new SetFiltersException("Ошибка при получении списка классификаторов", e);
        }
        handbookComboBox.setDisable(false);
        handbookComboBox.setItemsCustom(FXCollections.observableArrayList(listHandbook));
        if (listHandbook.size() != 0) {
            if (iterationGroupHandbookDtoToSetFilters == null) {
                handbookComboBox.setValueCustom(handbookComboBox.getItems().get(0));
                return;
            }
            Handbook suitableHandbook = null;
            for (Handbook handbook : listHandbook) {
                if (handbook.getId().equals(iterationGroupHandbookDtoToSetFilters.getIdHandbook())) {
                    suitableHandbook = handbook;
                    break;
                }
            }
            if (suitableHandbook == null) {
                throw new SetFiltersException("Значение классификатора из объекта iterationGroupHandbookDtoToSetFilters не найдено в массиве классификаторов");
            }
            handbookComboBox.setValueCustom(suitableHandbook, null, iterationGroupHandbookDtoToSetFilters);
        } else {
            if (iterationGroupHandbookDtoToSetFilters == null) {
                handbookComboBox.setValueCustom(null);
                return;
            }
            throw new SetFiltersException("Пришел пустой список значений показателя. Значение показателя из объекта numericalPokValue не найдено");
        }
    }

    private void setListIterationGroupHandbookDisplayedDataTypeAndSelectElement(IterationGroupHandbookDto iterationGroupHandbookDtoToSetFilters) {
        List<IterationGroupHandbookDisplayedDataType> listIterationGroupHandbookDisplayedDataType = Arrays.asList(IterationGroupHandbookDisplayedDataType.values());
        iterationGroupHandbookDisplayedDataTypeComboBox.setDisable(false);
        iterationGroupHandbookDisplayedDataTypeComboBox.setItemsCustom(FXCollections.observableArrayList(listIterationGroupHandbookDisplayedDataType));
        if (iterationGroupHandbookDtoToSetFilters == null) {
            iterationGroupHandbookDisplayedDataTypeComboBox.setValueCustom(iterationGroupHandbookDisplayedDataTypeComboBox.getItems().get(0));
            return;
        }
        IterationGroupHandbookDisplayedDataType suitableIterationGroupHandbookDisplayedDataType = null;
        for (IterationGroupHandbookDisplayedDataType iterationGroupHandbookDisplayedDataType : listIterationGroupHandbookDisplayedDataType) {
            if (iterationGroupHandbookDisplayedDataType.equals(iterationGroupHandbookDtoToSetFilters.getDisplayedDataType())) {
                suitableIterationGroupHandbookDisplayedDataType = iterationGroupHandbookDisplayedDataType;
                break;
            }
        }
        if (suitableIterationGroupHandbookDisplayedDataType == null) {
            throw new SetFiltersException("Значение типа отображаемых данных из объекта iterationGroupHandbookDtoToSetFilters не найдено в массиве типов отображаемых данных");
        }
        iterationGroupHandbookDisplayedDataTypeComboBox.setValueCustom(suitableIterationGroupHandbookDisplayedDataType, null, iterationGroupHandbookDtoToSetFilters);
    }

    private void setListIterationGroupHandbookSortDataTypeComboBoxAndSelectElement(IterationGroupHandbookDto iterationGroupHandbookDtoToSetFilters) {
        List<IterationGroupHandbookSortDataType> listIterationGroupHandbookSortDataType = Arrays.asList(IterationGroupHandbookSortDataType.values());
        iterationGroupHandbookSortDataTypeComboBox.setDisable(false);
        iterationGroupHandbookSortDataTypeComboBox.setItemsCustom(FXCollections.observableArrayList(listIterationGroupHandbookSortDataType));
        if (iterationGroupHandbookDtoToSetFilters == null) {
            iterationGroupHandbookSortDataTypeComboBox.setValueCustom(iterationGroupHandbookSortDataTypeComboBox.getItems().get(0));
            return;
        }
        IterationGroupHandbookSortDataType suitableIterationGroupHandbookSortDataType = null;
        for (IterationGroupHandbookSortDataType iterationGroupHandbookSortDataType : listIterationGroupHandbookSortDataType) {
            if (iterationGroupHandbookSortDataType.equals(iterationGroupHandbookDtoToSetFilters.getSortDataType())) {
                suitableIterationGroupHandbookSortDataType = iterationGroupHandbookSortDataType;
                break;
            }
        }
        if (suitableIterationGroupHandbookSortDataType == null) {
            throw new SetFiltersException("Значение типа данных сортировки из объекта iterationGroupHandbookDtoToSetFilters не найдено в массиве типов данных сортировки");
        }
        iterationGroupHandbookSortDataTypeComboBox.setValueCustom(suitableIterationGroupHandbookSortDataType, null, iterationGroupHandbookDtoToSetFilters);
    }

    private void setListIterationGroupHandbookSortOrderComboBoxAndSelectElement(IterationGroupHandbookDto iterationGroupHandbookDtoToSetFilters) {
        List<IterationGroupHandbookSortOrder> listIterationGroupHandbookSortOrder = Arrays.asList(IterationGroupHandbookSortOrder.values());
        iterationGroupHandbookSortOrderComboBox.setDisable(false);
        iterationGroupHandbookSortOrderComboBox.setItemsCustom(FXCollections.observableArrayList(listIterationGroupHandbookSortOrder));
        if (iterationGroupHandbookDtoToSetFilters == null) {
            iterationGroupHandbookSortOrderComboBox.setValueCustom(iterationGroupHandbookSortOrderComboBox.getItems().get(0));
            return;
        }
        IterationGroupHandbookSortOrder suitableIterationGroupHandbookSortOrder = null;
        for (IterationGroupHandbookSortOrder iterationGroupHandbookSortOrder : listIterationGroupHandbookSortOrder) {
            if (iterationGroupHandbookSortOrder.equals(iterationGroupHandbookDtoToSetFilters.getSortOrder())) {
                suitableIterationGroupHandbookSortOrder = iterationGroupHandbookSortOrder;
                break;
            }
        }
        if (suitableIterationGroupHandbookSortOrder == null) {
            throw new SetFiltersException("Значение порядка сортировки из объекта iterationGroupHandbookDtoToSetFilters не найдено в массиве порядков сортировки");
        }
        iterationGroupHandbookSortOrderComboBox.setValueCustom(suitableIterationGroupHandbookSortOrder, null, iterationGroupHandbookDtoToSetFilters);
    }

    private void setListSortHandbookRowCheckComboBoxAndSelectElements(IterationGroupHandbookDto iterationGroupHandbookDtoToSetFilters) {
        ArrayList<HandbookInnerRecord> listHandbookInnerRecord;
        Integer handbookId = handbookComboBox.getValue() != null ? handbookComboBox.getValue().getId() : null;
        if (handbookId == null) {
            if (iterationGroupHandbookDtoToSetFilters == null) {
                listSortHandbookRowCheckComboBox.setDisable(true);
                listSortHandbookRowCheckComboBox.getItems().clear();
                listSortHandbookRowCheckComboBox.getCheckModel().clearChecks();
                return;
            }
            throw new SetFiltersException("ИД классификатора равен null");
        }
        try {
            listHandbookInnerRecord = Main.coreService.getListHandbookInnerRecord(handbookId);
        } catch (Exception e) {
            e.printStackTrace();
            if (iterationGroupHandbookDtoToSetFilters == null) {
                createAlertWarning(this.stage, "Ошибка при получении списка записей классификатора\n" + e.getMessage());
                listSortHandbookRowCheckComboBox.setDisable(true);
                listSortHandbookRowCheckComboBox.getItems().clear();
                listSortHandbookRowCheckComboBox.getCheckModel().clearChecks();
                return;
            }
            throw new SetFiltersException("Ошибка при получении списка записей классификатора", e);
        }
        listSortHandbookRowCheckComboBox.setDisable(false);
        listSortHandbookRowCheckComboBox.getItems().setAll(FXCollections.observableArrayList(listHandbookInnerRecord));
        if (iterationGroupHandbookDtoToSetFilters == null) {
            listSortHandbookRowCheckComboBox.getCheckModel().clearChecks();
        } else {
            String str = "";
            for (Integer code : iterationGroupHandbookDtoToSetFilters.getSortListHandbookRowCode()) {
                str += code;
                if (iterationGroupHandbookDtoToSetFilters.getSortListHandbookRowCode().indexOf(code) != iterationGroupHandbookDtoToSetFilters.getSortListHandbookRowCode().size() - 1) {
                    str += ", ";
                }
            }
            listSortHandbookRowTextArea.setText(str);
        }
    }

    private void setFilterPresenceCheckBox(IterationGroupHandbookDto iterationGroupHandbookDtoToSetFilters) {
        if (iterationGroupHandbookDtoToSetFilters == null) {
            filterPresenceCheckBox.setSelected(false);
            return;
        }
        if (iterationGroupHandbookDtoToSetFilters.getFilterPresence() == null) {
            throw new SetFiltersException("Ошибка при получении значения признака присутствия фильтра");
        }
        filterPresenceCheckBox.setSelected(iterationGroupHandbookDtoToSetFilters.getFilterPresence());
    }

    private void setListIterationGroupHandbookFilterTypeComboBoxAndSelectElement(IterationGroupHandbookDto iterationGroupHandbookDtoToSetFilters) {
        List<IterationGroupHandbookFilterType> listIterationGroupHandbookFilterType = Arrays.asList(IterationGroupHandbookFilterType.values());
        iterationGroupHandbookFilterTypeComboBox.setDisable(false);
        iterationGroupHandbookFilterTypeComboBox.setItemsCustom(FXCollections.observableArrayList(listIterationGroupHandbookFilterType));
        if (iterationGroupHandbookDtoToSetFilters == null) {
            iterationGroupHandbookFilterTypeComboBox.setValueCustom(iterationGroupHandbookFilterTypeComboBox.getItems().get(0));
            return;
        }
        IterationGroupHandbookFilterType suitableIterationGroupHandbookFilterType = null;
        for (IterationGroupHandbookFilterType iterationGroupHandbookFilterType : listIterationGroupHandbookFilterType) {
            if (iterationGroupHandbookFilterType.equals(iterationGroupHandbookDtoToSetFilters.getSortOrder())) {
                suitableIterationGroupHandbookFilterType = iterationGroupHandbookFilterType;
                break;
            }
        }
        if (suitableIterationGroupHandbookFilterType == null) {
            throw new SetFiltersException("Значение порядка сортировки из объекта iterationGroupHandbookDtoToSetFilters не найдено в массиве порядков сортировки");
        }
        iterationGroupHandbookFilterTypeComboBox.setValueCustom(suitableIterationGroupHandbookFilterType, null, iterationGroupHandbookDtoToSetFilters);
    }

    private void setListFilterHandbookRowCheckComboBoxAndSelectElements(IterationGroupHandbookDto iterationGroupHandbookDtoToSetFilters) {
        ArrayList<HandbookInnerRecord> listHandbookInnerRecord;
        Integer handbookId = handbookComboBox.getValue() != null ? handbookComboBox.getValue().getId() : null;
        if (handbookId == null) {
            if (iterationGroupHandbookDtoToSetFilters == null) {
                listFilterHandbookRowCheckComboBox.setDisable(true);
                listFilterHandbookRowCheckComboBox.getItems().clear();
                listFilterHandbookRowCheckComboBox.getCheckModel().clearChecks();
                return;
            }
            throw new SetFiltersException("ИД классификатора равен null");
        }
        try {
            listHandbookInnerRecord = Main.coreService.getListHandbookInnerRecord(handbookId);
        } catch (Exception e) {
            e.printStackTrace();
            if (iterationGroupHandbookDtoToSetFilters == null) {
                createAlertWarning(this.stage, "Ошибка при получении списка записей классификатора\n" + e.getMessage());
                listFilterHandbookRowCheckComboBox.setDisable(true);
                listFilterHandbookRowCheckComboBox.getItems().clear();
                listFilterHandbookRowCheckComboBox.getCheckModel().clearChecks();
                return;
            }
            throw new SetFiltersException("Ошибка при получении списка записей классификатора", e);
        }
        listFilterHandbookRowCheckComboBox.setDisable(false);
        listFilterHandbookRowCheckComboBox.getItems().setAll(FXCollections.observableArrayList(listHandbookInnerRecord));
        if (iterationGroupHandbookDtoToSetFilters == null) {
            listFilterHandbookRowCheckComboBox.getCheckModel().clearChecks();
        } else {
            String str = "";
            for (Integer code : iterationGroupHandbookDtoToSetFilters.getSortListHandbookRowCode()) {
                str += code;
                if (iterationGroupHandbookDtoToSetFilters.getSortListHandbookRowCode().indexOf(code) != iterationGroupHandbookDtoToSetFilters.getSortListHandbookRowCode().size() - 1) {
                    str += ", ";
                }
            }
            listFilterHandbookRowTextArea.setText(str);
        }
    }

    private void onSelectGroupHandbook(IterationGroupHandbookDto iterationGroupHandbookDtoToSetFilters) {
        setListHandbookAndSelectElement(iterationGroupHandbookDtoToSetFilters);
    }

    private void onSelectHandbook(IterationGroupHandbookDto iterationGroupHandbookDtoToSetFilters) {
        setListSortHandbookRowCheckComboBoxAndSelectElements(iterationGroupHandbookDtoToSetFilters);
        if (filterPresenceCheckBox.isSelected()) {
            setListFilterHandbookRowCheckComboBoxAndSelectElements(iterationGroupHandbookDtoToSetFilters);
        }
    }

    private void onChangeFilterPresenceCheckBox(IterationGroupHandbookDto iterationGroupHandbookDtoToSetFilters) {
        if (filterPresenceCheckBox.isSelected()) {
            setListIterationGroupHandbookFilterTypeComboBoxAndSelectElement(iterationGroupHandbookDtoToSetFilters);
            setListFilterHandbookRowCheckComboBoxAndSelectElements(iterationGroupHandbookDtoToSetFilters);
        } else {
            iterationGroupHandbookFilterTypeComboBox.setDisable(true);
            iterationGroupHandbookFilterTypeComboBox.clear();
            listFilterHandbookRowCheckComboBox.setDisable(true);
            listFilterHandbookRowCheckComboBox.getItems().clear();
            listFilterHandbookRowCheckComboBox.getCheckModel().clearChecks();
        }
    }
}
