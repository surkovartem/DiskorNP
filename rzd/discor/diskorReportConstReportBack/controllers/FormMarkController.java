package ru.rzd.discor.diskorReportConstReportBack.controllers;

import com.sun.star.container.XNamed;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.sheet.XCellAddressable;
import com.sun.star.sheet.XCellRangeAddressable;
import com.sun.star.sheet.XSheetAnnotations;
import com.sun.star.sheet.XSheetAnnotationsSupplier;
import com.sun.star.table.CellAddress;
import com.sun.star.table.CellRangeAddress;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.rzd.discor.diskorReportConstReportBack.Main;
import ru.rzd.discor.diskorReportConstReportBack.SetFiltersException;
import ru.rzd.discor.diskorReportConstReportBack.customElements.ComboBoxAutoComplete;
import ru.rzd.discor.diskorReportConstReportBack.customElements.LabelWithTooltip;
import ru.rzd.discor.diskorReportConstReportBack.customElements.RadioButtonWithCallbackOnFire;
import ru.rzd.discor.diskorReportConstReportBack.customElements.SimpleObjectPropertyWithChangeListenersArray;
import ru.rzd.discor.diskorReportConstReportBack.models.core.*;
import ru.rzd.discor.diskorReportConstReportBack.models.currentProject.cellFormMarkInfo.CellFormMarkInfo;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.*;
import ru.rzd.discor.diskorReportConstReportBack.services.GeneralParamsService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static ru.rzd.discor.diskorReportConstReportBack.ConvertColumnNameFunctions.convertColToNumString;
import static ru.rzd.discor.diskorReportConstReportBack.ConvertColumnNameFunctions.convertNumToColString;
import static ru.rzd.discor.diskorReportConstReportBack.Main.*;

@Getter
@Setter
@ToString
public class FormMarkController extends AnchorPane {
    private final GeneralParamsService generalParamsService = Main.generalParamsService;
    public AttributesForNumericalPokValue identicalFieldsInAttributesForNumericalPokValue = null;
    public SimpleObjectPropertyWithChangeListenersArray<GroupPok> groupPokProperty = new SimpleObjectPropertyWithChangeListenersArray<>();
    public SimpleListProperty<ParamPok> listParamPokProperty = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
    Stage stage;
    @FXML
    private FormMarkMetaInfoController formMarkMetaInfoController;
    @FXML
    private Button ButtonGetValueOnList,
            ButtonCancelMarkUp,
            ButtonMarkUp,
            ButtonSetFilters,
            ButtonSelectGroupPok;
    @FXML
    private CheckBox ParamPok1CheckBoxElement,
            ParamPok2CheckBoxElement,
            ParamPok3CheckBoxElement,
            ParamPok4CheckBoxElement,
            ParamPok5CheckBoxElement,
            ParamPok6CheckBoxElement,
            ParamPok7CheckBoxElement,
            ParamPok8CheckBoxElement,
            ParamPok9CheckBoxElement,
            ParamPok10CheckBoxElement,
            HourReportCheckBoxElement,
            OffsetDateCheckBoxElement,
            PokValueElementCheckBoxElement;
    @FXML
    private DatePicker Calendar;

    @FXML
    private LabelWithTooltip GroupPokTextElement,
            ParamPok1TextElement,
            ParamPok2TextElement,
            ParamPok3TextElement,
            ParamPok4TextElement,
            ParamPok5TextElement,
            ParamPok6TextElement,
            ParamPok7TextElement,
            ParamPok8TextElement,
            ParamPok9TextElement,
            ParamPok10TextElement;
    @FXML
    private ComboBoxAutoComplete<Pok> PokElement;
    @FXML
    private ComboBoxAutoComplete<PokValue> PokValueElement;
    @FXML
    private ComboBoxAutoComplete<HandbookInnerRecord>
            ParamPok1Element,
            ParamPok2Element,
            ParamPok3Element,
            ParamPok4Element,
            ParamPok5Element,
            ParamPok6Element,
            ParamPok7Element,
            ParamPok8Element,
            ParamPok9Element,
            ParamPok10Element;
    @FXML
    private ComboBoxAutoComplete<CompAsToPokValue> CompAsToPokValueForPriorityAsAndCompElement;
    @FXML
    private ComboBoxAutoComplete<OffsetDate> OffsetDateElement;
    @FXML
    private ComboBoxAutoComplete<Integer> HourReportElement;
    @FXML
    private RadioButtonWithCallbackOnFire PriorityAsAndCompElement, PriorityAsElement, WithoutPriorityElement;
    @FXML
    private ComboBoxAutoComplete<AsSourceInformation> AsSourceInformationForPriorityAsAndCompElement, AsSourceInformationForPriorityAsElement;

    public FormMarkController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/FormMark.fxml"));
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
        setElementsValue(null);
        SetToCalendarYesterdayDate();
    }

    public void setInitialParams(ConstructorModeController constructorModeController) {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    void SetToCalendarYesterdayDate(){
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        Calendar.setValue(yesterday);
    }

    @FXML
    void PressedButtonSelectGroupPok() {
        GroupPok rootGroupPok;
        try {
            rootGroupPok = getRootGroupPok();
        } catch (Exception e) {
            groupPokProperty.setValueCustom(null);
            return;
        }
        openModalToSelectGroup(rootGroupPok);
    }

    @FXML
    void PressedButtonMarkUp() {
        if (checkButtonMarkUpIsDisable()) {
            //createAlertWarning(this.stage, "???? ?????? ?????????????????? ???????????? ?????? ???????????????? ????????????");
            createInfoNotification("???? ?????? ?????????????????? ???????????? ?????? ???????????????? ????????????");
            return;
        }
        XNamed xNamed = UnoRuntime.queryInterface(XNamed.class, generalParamsService.getActiveSheet());

        XCellRangeAddressable xRangeAddr = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeAddressable.class, generalParamsService.getActiveCells());
        CellRangeAddress aRangeAddress = xRangeAddr.getRangeAddress();
        NumericalPokValue newNumericalPokValue;

        if (ParamPok1CheckBoxElement.isSelected() || ParamPok2CheckBoxElement.isSelected() || ParamPok3CheckBoxElement.isSelected()
                || ParamPok4CheckBoxElement.isSelected() || ParamPok5CheckBoxElement.isSelected() || ParamPok6CheckBoxElement.isSelected()
                || ParamPok7CheckBoxElement.isSelected() || ParamPok8CheckBoxElement.isSelected() || ParamPok9CheckBoxElement.isSelected()
                || ParamPok10CheckBoxElement.isSelected()
                || HourReportCheckBoxElement.isSelected() || OffsetDateCheckBoxElement.isSelected()
        ) {
            //?????????????? ????????????????, ???????? ???????????????? ???????? ???? ???????? ??????????????, ?????????????? ???????????????? ???? ???????????????????????? ????????????????
            //listSelectedNumericalPokValueProperty - ???????????? ???????????????????????? listNumericalPokValue
            //??????????????????????????, ???? ???????? ?????????????????? listNumericalPokValue, ???????? ???????????? ???????????????? ?????????????????? ?????????? ????????????????????
            for (CellFormMarkInfo cellFormMarkInfo : generalParamsService.getListSelectedCellFormMarkInfo()) {
                NumericalPokValue selectedNumericalPokValue = cellFormMarkInfo.getNumericalPokValue();
                //?????? ?????????????? ???????????????? ???? null, ?????? ?????? ?????? ?????????????????????? ???????????????? ???????? ???? ?????????? ParamPokCheckBoxElement ?????? ???????????????????? ?????????????? ???????????????? ???? null ??????????????????,
                //?????????????? ?????????????????????????????? ????????
                //?????????? ????????????, nullPointerException ???????????? ???? ?????????????????????????? ???????????? ?? ????????, ?????????????? ???????? ?????????? ??????????????????
                if (ParamPok1CheckBoxElement.isSelected()) {
                    selectedNumericalPokValue.getAttributes().getP1().setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok1Element.getValue().getHandbookRow()));
                }
                if (ParamPok2CheckBoxElement.isSelected()) {
                    selectedNumericalPokValue.getAttributes().getP2().setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok2Element.getValue().getHandbookRow()));
                }
                if (ParamPok3CheckBoxElement.isSelected()) {
                    selectedNumericalPokValue.getAttributes().getP3().setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok3Element.getValue().getHandbookRow()));
                }
                if (ParamPok4CheckBoxElement.isSelected()) {
                    selectedNumericalPokValue.getAttributes().getP4().setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok4Element.getValue().getHandbookRow()));
                }
                if (ParamPok5CheckBoxElement.isSelected()) {
                    selectedNumericalPokValue.getAttributes().getP5().setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok5Element.getValue().getHandbookRow()));
                }
                if (ParamPok6CheckBoxElement.isSelected()) {
                    selectedNumericalPokValue.getAttributes().getP6().setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok6Element.getValue().getHandbookRow()));
                }
                if (ParamPok7CheckBoxElement.isSelected()) {
                    selectedNumericalPokValue.getAttributes().getP7().setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok7Element.getValue().getHandbookRow()));
                }
                if (ParamPok8CheckBoxElement.isSelected()) {
                    selectedNumericalPokValue.getAttributes().getP8().setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok8Element.getValue().getHandbookRow()));
                }
                if (ParamPok9CheckBoxElement.isSelected()) {
                    selectedNumericalPokValue.getAttributes().getP9().setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok9Element.getValue().getHandbookRow()));
                }
                if (ParamPok10CheckBoxElement.isSelected()) {
                    selectedNumericalPokValue.getAttributes().getP10().setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok10Element.getValue().getHandbookRow()));
                }
                if (HourReportCheckBoxElement.isSelected()) {
                    selectedNumericalPokValue.getAttributes().setHourReport(HourReportElement.getValue());
                }
                if (OffsetDateCheckBoxElement.isSelected()) {
                    selectedNumericalPokValue.getAttributes().setOffsetDate(OffsetDateForNumericalPokValue.get(OffsetDateElement.getValue()));
                }
                //???????????????????? ?????????????????? ???????????? (???? ?????????? ??????????)
                try {
                    generalParamsService.setCellAnnotation(generalParamsService.getActiveSheet(), cellFormMarkInfo);
                } catch (Exception e) {
                    System.out.println("???????????? ?????? ???????????????????? ?????????????????? ?????? ???????????? " + selectedNumericalPokValue.getCellInfo());
                }
            }
        } else {
            //?????????????? ????????????????, ???????? ???? ???????????????? ???? ?????????? ??????????????, ?????????????? ???????????????? ???? ???????????????????????? ????????????????
            //???????????????? ???????????? ????????????????
            generalParamsService.getListNumericalPokValue().removeAll(
                    generalParamsService.getListSelectedCellFormMarkInfo().stream().map(v -> v.getNumericalPokValue()).collect(Collectors.toCollection(ArrayList::new))
            );
            //???????????????????? ?????????? ????????????????
            for (int i = aRangeAddress.StartColumn; i <= aRangeAddress.EndColumn; i++) {
                for (int j = aRangeAddress.StartRow; j <= aRangeAddress.EndRow; j++) {
                    //?????????????????? ?????????????? numericalPokValue ?????? ????????????
                    newNumericalPokValue = getNewNumericalPokValueForCell(xNamed.getName(), i, j);
                    CellFormMarkInfo newCellFormMarkInfo = new CellFormMarkInfo();
                    newCellFormMarkInfo.setNumericalPokValue(newNumericalPokValue);
                    newCellFormMarkInfo.setIterationGroupDto(null);
                    //???????????????????? ?????????? ?????????????????? ?????? ???????????? ?? ???????????? ??????????????????
                    generalParamsService.getListNumericalPokValue().add(newNumericalPokValue);
                    //?????????????? ?????????????????? ???????????? (???? ?????????? ??????????)
                    try {
                        generalParamsService.setCellAnnotation(generalParamsService.getActiveSheet(), newCellFormMarkInfo);
                    } catch (Exception e) {
                        System.out.println("???????????? ?????? ???????????????????? ?????????????????? ?????? ???????????? " + newNumericalPokValue.getCellInfo());
                    }
                }
            }
        }
        generalParamsService.updateSelectedElements();

        //createAlertInfo(this.stage, "???????????? ?????????????? ??????????????????");
        createSuccessNotification("???????????? ?????????????? ??????????????????");
    }

    @FXML
    void PressedButtonCancelMarkUp() throws Exception {
        if (checkButtonCancelMarkUpIsDisable()) {
            //createAlertWarning(this.stage, "?????? ???????????????? ???????????????? ?????????? ???????????????????? ???????????????? ???? ???????????? ?????????? ?????????????????????? ????????????");
            createInfoNotification("?????? ???????????????? ???????????????? ?????????? ????????????????????\n???????????????? ???? ???????????? ?????????? ?????????????????????? ????????????");
            return;
        }

        XNamed xNamed = UnoRuntime.queryInterface(XNamed.class, generalParamsService.getActiveSheet());

        XCellRangeAddressable xRangeAddr = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeAddressable.class, generalParamsService.getActiveCells());
        CellRangeAddress aRangeAddress = xRangeAddr.getRangeAddress();

        for (int i = aRangeAddress.StartColumn; i <= aRangeAddress.EndColumn; i++) {
            for (int j = aRangeAddress.StartRow; j <= aRangeAddress.EndRow; j++) {
                NumericalPokValue numericalPokValue;
                for (int k = 0; k < generalParamsService.getListNumericalPokValue().size(); k++) {
                    numericalPokValue = generalParamsService.getListNumericalPokValue().get(k);
                    if (
                            numericalPokValue.getCellInfo().getListName().equals(xNamed.getName())
                                    && numericalPokValue.getCellInfo().getRowNumber() == j + 1
                                    && numericalPokValue.getCellInfo().getColumnNumber().equals(convertNumToColString(i))
                    ) {
                        generalParamsService.getListNumericalPokValue().remove(numericalPokValue);
                        k--;
                    }
                }

                //?????????????????? ?? ???????????? ?? ???????????????? ??????????????????
                XCell xCell = generalParamsService.getActiveSheet().getCellByPosition(i, j);
                //?????????????????? ???????????? ????????????
                XCellAddressable xCellAddr = UnoRuntime.queryInterface(XCellAddressable.class, xCell);
                CellAddress aAddress = xCellAddr.getCellAddress();
                //?????????????? ??????????????????
                XSheetAnnotationsSupplier xAnnotationsSupp = UnoRuntime.queryInterface(XSheetAnnotationsSupplier.class, generalParamsService.getActiveSheet());
                XSheetAnnotations xAnnotations = xAnnotationsSupp.getAnnotations();

                String annotation = "";
                xAnnotations.insertNew(aAddress, annotation);
            }
        }
        //?????????????? ???????????????????? ?? ???????????? ?? ?????????????????? ????????
        generalParamsService.updateSelectedElements();

        //createAlertInfo(this.stage, "???????????????? ?????????? ?????????????? ??????????????");
        createSuccessNotification("???????????????? ?????????? ?????????????? ??????????????");
    }

    @FXML
    void OffsetBtnAction(){
        openModalToOffset();
    }

    void openModalToOffset(){
        OffsetDateController offsetDateController;
        try {
            offsetDateController = new OffsetDateController();
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "???????????? ?????? ???????????????? ???????????????????? ???????? ???? ???????????? ????????????????\n" + e.getMessage());
            return;
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(offsetDateController));
        stage.setTitle("?????????? ????????????????");
        stage.getIcons().add(new javafx.scene.image.Image(ConstructorModeController.class.getResourceAsStream(
                "/assets/icon_32.png")));
        offsetDateController.setStage(stage);
        stage.showAndWait();
    }

    @FXML
    void PressedButtonGetValueOnList() {
        if (Calendar.getValue() == null) {
            //createAlertWarning(this.stage, "?????????????????? ???????? ????????");
            createInfoNotification("?????????????????? ???????? ????????");
            return;
        }
        String date = Calendar.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        try {
            setValuesToCells(Main.reportRepoService.getDataReport(generalParamsService.getListNumericalPokValue(), date));
        } catch (Exception e) {
            createAlertWarning(this.stage, "???????????? ?????? ?????????????????? ???????????????? ???? ????????\n" + e.getMessage());
            return;
        }
        //createAlertInfo(this.stage, "???????????????? ???? ???????? ?????????????? ????????????????");
        createSuccessNotification("???????????????? ???? ???????? ?????????????? ????????????????");
    }

    @FXML
    public void PressedButtonSetFilters() {
        if (checkButtonSetFiltersIsDisable()) {
            //createAlertWarning(this.stage, "?????? ?????????????? ???????????????? ???????????????????? ???????????????? ???? ???????????? ?????????? ?????????????????????? ????????????");
            createInfoNotification("?????? ?????????????? ???????????????? ???????????????????? ????????????????\n???? ???????????? ?????????? ?????????????????????? ????????????");
            return;
        }
        PressedButtonSetFilters(generalParamsService.getListSelectedCellFormMarkInfo().get(0).getNumericalPokValue());
    }

    private void setElementsStyle() {
        //?????????????????????? ???????????????? ?????????????????? ??????????????
        HourReportElement.setCellFactory((ListView<Integer> listView) -> {
            return new TextFieldListCell<Integer>() {
                @Override
                public void updateItem(Integer hourReport, boolean isEmpty) {
                    super.updateItem(hourReport, isEmpty);
                    if (hourReport != null) {
                        setText("" + hourReport);
                    }
                }
            };
        });
        HourReportElement.setButtonCell(new TextFieldListCell<Integer>() {
            @Override
            public void updateItem(Integer hourReport, boolean isEmpty) {
                super.updateItem(hourReport, isEmpty);
                if (hourReport != null) {
                    setText("" + hourReport);
                }
            }
        });
        PokElement.setCellFactory((ListView<Pok> listView) -> {
            return new TextFieldListCell<Pok>() {
                @Override
                public void updateItem(Pok pok, boolean isEmpty) {
                    super.updateItem(pok, isEmpty);
                    if (pok != null) {
                        setText("[" + pok.getId() + "] " + pok.getName());
                    }
                }
            };
        });
        PokElement.setButtonCell(new TextFieldListCell<Pok>() {
            @Override
            public void updateItem(Pok pok, boolean isEmpty) {
                super.updateItem(pok, isEmpty);
                if (pok != null) {
                    setText("[" + pok.getId() + "] " + pok.getName());
                }
            }
        });
        PokValueElement.setCellFactory((ListView<PokValue> listView) -> {
            return new TextFieldListCell<PokValue>() {
                @Override
                public void updateItem(PokValue pokValue, boolean isEmpty) {
                    super.updateItem(pokValue, isEmpty);
                    if (pokValue != null) {
                        setText("[" + pokValue.getId() + "] " + pokValue.getPermissibleValuePok().getName() + " (" + pokValue.getEdIzm().getName() + ")");
                    }
                }
            };
        });
        PokValueElement.setButtonCell(new TextFieldListCell<PokValue>() {
            @Override
            public void updateItem(PokValue pokValue, boolean isEmpty) {
                super.updateItem(pokValue, isEmpty);
                if (pokValue != null) {
                    setText("[" + pokValue.getId() + "] " + pokValue.getPermissibleValuePok().getName() + " (" + pokValue.getEdIzm().getName() + ")");
                }
            }
        });
        ParamPok1Element.setCellFactory((ListView<HandbookInnerRecord> listView) -> {
            return new TextFieldListCell<HandbookInnerRecord>() {
                @Override
                public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                    super.updateItem(handbookInnerRecord, isEmpty);
                    if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                        setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                    }
                }
            };
        });
        ParamPok1Element.setButtonCell(new TextFieldListCell<HandbookInnerRecord>() {
            @Override
            public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                super.updateItem(handbookInnerRecord, isEmpty);
                if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                    setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                }
            }
        });
        ParamPok2Element.setCellFactory((ListView<HandbookInnerRecord> listView) -> {
            return new TextFieldListCell<HandbookInnerRecord>() {
                @Override
                public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                    super.updateItem(handbookInnerRecord, isEmpty);
                    if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                        setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                    }
                }
            };
        });
        ParamPok2Element.setButtonCell(new TextFieldListCell<HandbookInnerRecord>() {
            @Override
            public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                super.updateItem(handbookInnerRecord, isEmpty);
                if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                    setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                }
            }
        });
        ParamPok3Element.setCellFactory((ListView<HandbookInnerRecord> listView) -> {
            return new TextFieldListCell<HandbookInnerRecord>() {
                @Override
                public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                    super.updateItem(handbookInnerRecord, isEmpty);
                    if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                        setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                    }
                }
            };
        });
        ParamPok3Element.setButtonCell(new TextFieldListCell<HandbookInnerRecord>() {
            @Override
            public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                super.updateItem(handbookInnerRecord, isEmpty);
                if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                    setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                }
            }
        });
        ParamPok4Element.setCellFactory((ListView<HandbookInnerRecord> listView) -> {
            return new TextFieldListCell<HandbookInnerRecord>() {
                @Override
                public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                    super.updateItem(handbookInnerRecord, isEmpty);
                    if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                        setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                    }
                }
            };
        });
        ParamPok4Element.setButtonCell(new TextFieldListCell<HandbookInnerRecord>() {
            @Override
            public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                super.updateItem(handbookInnerRecord, isEmpty);
                if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                    setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                }
            }
        });
        ParamPok5Element.setCellFactory((ListView<HandbookInnerRecord> listView) -> {
            return new TextFieldListCell<HandbookInnerRecord>() {
                @Override
                public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                    super.updateItem(handbookInnerRecord, isEmpty);
                    if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                        setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                    }
                }
            };
        });
        ParamPok5Element.setButtonCell(new TextFieldListCell<HandbookInnerRecord>() {
            @Override
            public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                super.updateItem(handbookInnerRecord, isEmpty);
                if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                    setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                }
            }
        });
        ParamPok6Element.setCellFactory((ListView<HandbookInnerRecord> listView) -> {
            return new TextFieldListCell<HandbookInnerRecord>() {
                @Override
                public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                    super.updateItem(handbookInnerRecord, isEmpty);
                    if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null && isVisible()) {
                        setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                    }
                }
            };
        });
        ParamPok6Element.setButtonCell(new TextFieldListCell<HandbookInnerRecord>() {
            @Override
            public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                super.updateItem(handbookInnerRecord, isEmpty);
                if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                    setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                }
            }
        });
        ParamPok7Element.setCellFactory((ListView<HandbookInnerRecord> listView) -> {
            return new TextFieldListCell<HandbookInnerRecord>() {
                @Override
                public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                    super.updateItem(handbookInnerRecord, isEmpty);
                    if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null && isVisible()) {
                        setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                    }
                }
            };
        });
        ParamPok7Element.setButtonCell(new TextFieldListCell<HandbookInnerRecord>() {
            @Override
            public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                super.updateItem(handbookInnerRecord, isEmpty);
                if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                    setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                }
            }
        });
        ParamPok8Element.setCellFactory((ListView<HandbookInnerRecord> listView) -> {
            return new TextFieldListCell<HandbookInnerRecord>() {
                @Override
                public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                    super.updateItem(handbookInnerRecord, isEmpty);
                    if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null && isVisible()) {
                        setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                    }
                }
            };
        });
        ParamPok8Element.setButtonCell(new TextFieldListCell<HandbookInnerRecord>() {
            @Override
            public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                super.updateItem(handbookInnerRecord, isEmpty);
                if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                    setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                }
            }
        });
        ParamPok9Element.setCellFactory((ListView<HandbookInnerRecord> listView) -> {
            return new TextFieldListCell<HandbookInnerRecord>() {
                @Override
                public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                    super.updateItem(handbookInnerRecord, isEmpty);
                    if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null && isVisible()) {
                        setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                    }
                }
            };
        });
        ParamPok9Element.setButtonCell(new TextFieldListCell<HandbookInnerRecord>() {
            @Override
            public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                super.updateItem(handbookInnerRecord, isEmpty);
                if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                    setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                }
            }
        });
        ParamPok10Element.setCellFactory((ListView<HandbookInnerRecord> listView) -> {
            return new TextFieldListCell<HandbookInnerRecord>() {
                @Override
                public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                    super.updateItem(handbookInnerRecord, isEmpty);
                    if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null && isVisible()) {
                        setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                    }
                }
            };
        });
        ParamPok10Element.setButtonCell(new TextFieldListCell<HandbookInnerRecord>() {
            @Override
            public void updateItem(HandbookInnerRecord handbookInnerRecord, boolean isEmpty) {
                super.updateItem(handbookInnerRecord, isEmpty);
                if (handbookInnerRecord != null && handbookInnerRecord.getHandbookRow() != null) {
                    setText("[" + handbookInnerRecord.getHandbookRow().getId() + "] " + handbookInnerRecord.getHandbookRow().getName());
                }
            }
        });
        OffsetDateElement.setCellFactory((ListView<OffsetDate> listView) -> {
            return new TextFieldListCell<OffsetDate>() {
                @Override
                public void updateItem(OffsetDate offsetDate, boolean isEmpty) {
                    super.updateItem(offsetDate, isEmpty);
                    if (offsetDate != null) {
                        setText("[" + offsetDate.getId() + "] " + offsetDate.getName());
                    }
                }
            };
        });
        OffsetDateElement.setButtonCell(new TextFieldListCell<OffsetDate>() {
            @Override
            public void updateItem(OffsetDate offsetDate, boolean isEmpty) {
                super.updateItem(offsetDate, isEmpty);
                if (offsetDate != null) {
                    setText("[" + offsetDate.getId() + "] " + offsetDate.getName());
                }
            }
        });
        CompAsToPokValueForPriorityAsAndCompElement.setCellFactory((ListView<CompAsToPokValue> listView) -> {
            return new TextFieldListCell<CompAsToPokValue>() {
                @Override
                public void updateItem(CompAsToPokValue compAsToPokValue, boolean isEmpty) {
                    super.updateItem(compAsToPokValue, isEmpty);
                    if (compAsToPokValue != null) {
                        String text = "[" + compAsToPokValue.getId() + "] " + compAsToPokValue.getCompAsSourceInf().getName();
                        if (compAsToPokValue.getName() != null && compAsToPokValue.getName().length() > 0) {
                            text += " -> " + compAsToPokValue.getName();
                        }
                        setText(text);
                    }
                }
            };
        });
        CompAsToPokValueForPriorityAsAndCompElement.setButtonCell(new TextFieldListCell<CompAsToPokValue>() {
            @Override
            public void updateItem(CompAsToPokValue compAsToPokValue, boolean isEmpty) {
                super.updateItem(compAsToPokValue, isEmpty);
                if (compAsToPokValue != null) {
                    String text = "[" + compAsToPokValue.getId() + "] " + compAsToPokValue.getCompAsSourceInf().getName();
                    if (compAsToPokValue.getName() != null && compAsToPokValue.getName().length() > 0) {
                        text += " -> " + compAsToPokValue.getName();
                    }
                    setText(text);
                }
            }
        });
        AsSourceInformationForPriorityAsAndCompElement.setCellFactory((ListView<AsSourceInformation> listView) -> {
            return new TextFieldListCell<AsSourceInformation>() {
                @Override
                public void updateItem(AsSourceInformation asSourceInformation, boolean isEmpty) {
                    super.updateItem(asSourceInformation, isEmpty);
                    if (asSourceInformation != null) {
                        setText("[" + asSourceInformation.getId() + "] " + asSourceInformation.getName());
                    }
                }
            };
        });
        AsSourceInformationForPriorityAsAndCompElement.setButtonCell(new TextFieldListCell<AsSourceInformation>() {
            @Override
            public void updateItem(AsSourceInformation asSourceInformation, boolean isEmpty) {
                super.updateItem(asSourceInformation, isEmpty);
                if (asSourceInformation != null) {
                    setText("[" + asSourceInformation.getId() + "] " + asSourceInformation.getName());
                }
            }
        });
        AsSourceInformationForPriorityAsElement.setCellFactory((ListView<AsSourceInformation> listView) -> {
            return new TextFieldListCell<AsSourceInformation>() {
                @Override
                public void updateItem(AsSourceInformation asSourceInformation, boolean isEmpty) {
                    super.updateItem(asSourceInformation, isEmpty);
                    if (asSourceInformation != null) {
                        setText("[" + asSourceInformation.getId() + "] " + asSourceInformation.getName());
                    }
                }
            };
        });
        AsSourceInformationForPriorityAsElement.setButtonCell(new TextFieldListCell<AsSourceInformation>() {
            @Override
            public void updateItem(AsSourceInformation asSourceInformation, boolean isEmpty) {
                super.updateItem(asSourceInformation, isEmpty);
                if (asSourceInformation != null) {
                    setText("[" + asSourceInformation.getId() + "] " + asSourceInformation.getName());
                }
            }
        });
    }

    NumericalPokValue getNewNumericalPokValueForCell(String listName, Integer columnIndex, Integer rowIndex) {
        NumericalPokValue numericalPokValue = new NumericalPokValue();
        //numericalPokValueInfo
        NumericalPokValueInfoForNumericalPokValue numericalPokValueInfo = null;
        //attributes
        AttributesForNumericalPokValue attributes = new AttributesForNumericalPokValue();
        PriorityTypeForNumericalPokValue priority = null;
        AsSourceInformationForNumericalPokValue asSourceInformation = null;
        CompAsSourceInfForNumericalPokValue compAsSourceInf = null;
        CompAsToPokValueForNumericalPokValue compAsToPokValue = null;
        if (PriorityAsAndCompElement.isSelected()) {
            priority = PriorityTypeForNumericalPokValue.priorityAsAndComp;
            //asSourceInformation
            asSourceInformation = AsSourceInformationForNumericalPokValue.get(AsSourceInformationForPriorityAsAndCompElement.getValue());
            //compAsSourceInf
            compAsSourceInf = CompAsSourceInfForNumericalPokValue.get(CompAsToPokValueForPriorityAsAndCompElement.getValue().getCompAsSourceInf());
            //compAsToPokValue
            compAsToPokValue = CompAsToPokValueForNumericalPokValue.get(CompAsToPokValueForPriorityAsAndCompElement.getValue());
        } else if (PriorityAsElement.isSelected()) {
            priority = PriorityTypeForNumericalPokValue.priorityAs;
            //asSourceInformation
            asSourceInformation = AsSourceInformationForNumericalPokValue.get(AsSourceInformationForPriorityAsElement.getValue());
        } else if (WithoutPriorityElement.isSelected()) {
            priority = PriorityTypeForNumericalPokValue.withoutPriority;
        }
        //offsetDate
        OffsetDateForNumericalPokValue offsetDate = OffsetDateForNumericalPokValue.get(OffsetDateElement.getValue());
        //pok
        PokForNumericalPokValue pok = PokForNumericalPokValue.get(PokElement.getValue());
        //pokValue
        PokValueForNumericalPokValue pokValue = PokValueForNumericalPokValue.get(PokValueElement.getValue());
        //permissibleValuePok
        PermissibleValuePokForNumericalPokValue permissibleValuePok = PermissibleValuePokForNumericalPokValue.get(PokValueElement.getValue().getPermissibleValuePok());
        //edIzm
        EdIzmForNumericalPokValue edIzm = EdIzmForNumericalPokValue.get(PokValueElement.getValue().getEdIzm());
        //p1
        ParamPokInfoForNumericalPokValue p1 = null;
        if (listParamPokProperty.getValue().size() > 0) {
            p1 = new ParamPokInfoForNumericalPokValue();
            p1.setParamPok(ParamPokForNumericalPokValue.get(listParamPokProperty.getValue().get(0)));
            p1.setHandbook(HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(0).getHandbook()));
            p1.setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok1Element.getValue().getHandbookRow()));
        }
        //p2
        ParamPokInfoForNumericalPokValue p2 = null;
        if (listParamPokProperty.getValue().size() > 1) {
            p2 = new ParamPokInfoForNumericalPokValue();
            p2.setParamPok(ParamPokForNumericalPokValue.get(listParamPokProperty.getValue().get(1)));
            p2.setHandbook(HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(1).getHandbook()));
            p2.setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok2Element.getValue().getHandbookRow()));
        }
        //p3
        ParamPokInfoForNumericalPokValue p3 = null;
        if (listParamPokProperty.getValue().size() > 2) {
            p3 = new ParamPokInfoForNumericalPokValue();
            p3.setParamPok(ParamPokForNumericalPokValue.get(listParamPokProperty.getValue().get(2)));
            p3.setHandbook(HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(2).getHandbook()));
            p3.setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok3Element.getValue().getHandbookRow()));
        }
        //p4
        ParamPokInfoForNumericalPokValue p4 = null;
        if (listParamPokProperty.getValue().size() > 3) {
            p4 = new ParamPokInfoForNumericalPokValue();
            p4.setParamPok(ParamPokForNumericalPokValue.get(listParamPokProperty.getValue().get(3)));
            p4.setHandbook(HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(3).getHandbook()));
            p4.setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok4Element.getValue().getHandbookRow()));
        }
        //p5
        ParamPokInfoForNumericalPokValue p5 = null;
        if (listParamPokProperty.getValue().size() > 4) {
            p5 = new ParamPokInfoForNumericalPokValue();
            p5.setParamPok(ParamPokForNumericalPokValue.get(listParamPokProperty.getValue().get(4)));
            p5.setHandbook(HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(4).getHandbook()));
            p5.setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok5Element.getValue().getHandbookRow()));
        }
        //p6
        ParamPokInfoForNumericalPokValue p6 = null;
        if (listParamPokProperty.getValue().size() > 5) {
            p6 = new ParamPokInfoForNumericalPokValue();
            p6.setParamPok(ParamPokForNumericalPokValue.get(listParamPokProperty.getValue().get(5)));
            p6.setHandbook(HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(5).getHandbook()));
            p6.setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok6Element.getValue().getHandbookRow()));
        }
        //p7
        ParamPokInfoForNumericalPokValue p7 = null;
        if (listParamPokProperty.getValue().size() > 6) {
            p7 = new ParamPokInfoForNumericalPokValue();
            p7.setParamPok(ParamPokForNumericalPokValue.get(listParamPokProperty.getValue().get(6)));
            p7.setHandbook(HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(6).getHandbook()));
            p7.setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok7Element.getValue().getHandbookRow()));
        }
        //p8
        ParamPokInfoForNumericalPokValue p8 = null;
        if (listParamPokProperty.getValue().size() > 7) {
            p8 = new ParamPokInfoForNumericalPokValue();
            p8.setParamPok(ParamPokForNumericalPokValue.get(listParamPokProperty.getValue().get(7)));
            p8.setHandbook(HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(7).getHandbook()));
            p8.setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok8Element.getValue().getHandbookRow()));
        }
        //p9
        ParamPokInfoForNumericalPokValue p9 = null;
        if (listParamPokProperty.getValue().size() > 8) {
            p9 = new ParamPokInfoForNumericalPokValue();
            p9.setParamPok(ParamPokForNumericalPokValue.get(listParamPokProperty.getValue().get(8)));
            p9.setHandbook(HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(8).getHandbook()));
            p9.setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok9Element.getValue().getHandbookRow()));
        }
        //p10
        ParamPokInfoForNumericalPokValue p10 = null;
        if (listParamPokProperty.getValue().size() > 9) {
            p10 = new ParamPokInfoForNumericalPokValue();
            p10.setParamPok(ParamPokForNumericalPokValue.get(listParamPokProperty.getValue().get(9)));
            p10.setHandbook(HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(9).getHandbook()));
            p10.setHandbookRow(HandbookRowForNumericalPokValue.get(ParamPok10Element.getValue().getHandbookRow()));
        }
        //HourReport
        Integer hourReport = this.HourReportElement.getValue();
        //???????????????????? attributes
        attributes.setPriority(priority);
        attributes.setAsSourceInformation(asSourceInformation);
        attributes.setCompAsSourceInf(compAsSourceInf);
        attributes.setCompAsToPokValue(compAsToPokValue);
        attributes.setOffsetDate(offsetDate);
        attributes.setPok(pok);
        attributes.setPokValue(pokValue);
        attributes.setPermissibleValuePok(permissibleValuePok);
        attributes.setEdIzm(edIzm);
        attributes.setP1(p1);
        attributes.setP2(p2);
        attributes.setP3(p3);
        attributes.setP4(p4);
        attributes.setP5(p5);
        attributes.setP6(p6);
        attributes.setP7(p7);
        attributes.setP8(p8);
        attributes.setP9(p9);
        attributes.setP10(p10);
        attributes.setHourReport(hourReport);
        //cellInfo
        CellInfoForNumericalPokValue cellInfo = new CellInfoForNumericalPokValue();
        cellInfo.setListName(listName);
        cellInfo.setColumnNumber(convertNumToColString(columnIndex));
        cellInfo.setRowNumber(rowIndex + 1);
        //???????????????????? ?????????? numericalPokValue
        numericalPokValue.setNumericalPokValueInfo(numericalPokValueInfo);
        numericalPokValue.setAttributes(attributes);
        numericalPokValue.setCellInfo(cellInfo);
        return numericalPokValue;
    }

    void setIdenticalFieldsValuesForListSelectedNumericalPokValues() {
        if (generalParamsService.getListSelectedCellFormMarkInfo().size() == 0) {
            identicalFieldsInAttributesForNumericalPokValue = null;
            return;
        }
        AttributesForNumericalPokValue firstNumericalPokValueAttributes = null;
        for (int i = 0; i < generalParamsService.getListSelectedCellFormMarkInfo().size(); i++) {
            NumericalPokValue numericalPokValue = generalParamsService.getListSelectedCellFormMarkInfo().get(i).getNumericalPokValue();
            if (numericalPokValue == null) {
                identicalFieldsInAttributesForNumericalPokValue = null;
                return;
            }
            AttributesForNumericalPokValue attributes = numericalPokValue.getAttributes();
            if (attributes == null) {
                identicalFieldsInAttributesForNumericalPokValue = null;
                return;
            }
            if (i == 0) {
                try {
                    //?????????????????????? ??????????????
                    identicalFieldsInAttributesForNumericalPokValue = objectMapper.readValue(objectMapper.writeValueAsString(attributes), AttributesForNumericalPokValue.class);
                } catch (IOException e) {
                    identicalFieldsInAttributesForNumericalPokValue = null;
                    throw new SetFiltersException(e);
                }
                continue;
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getPriority() != null) {
                if (!attributes.getPriority().equals(identicalFieldsInAttributesForNumericalPokValue.getPriority())) {
                    identicalFieldsInAttributesForNumericalPokValue.setPriority(null);
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getAsSourceInformation() != null) {
                if (!AsSourceInformationForNumericalPokValue.equals(attributes.getAsSourceInformation(), identicalFieldsInAttributesForNumericalPokValue.getAsSourceInformation())) {
                    identicalFieldsInAttributesForNumericalPokValue.setAsSourceInformation(null);
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getCompAsSourceInf() != null) {
                if (!CompAsSourceInfForNumericalPokValue.equals(attributes.getCompAsSourceInf(), identicalFieldsInAttributesForNumericalPokValue.getCompAsSourceInf())) {
                    identicalFieldsInAttributesForNumericalPokValue.setCompAsSourceInf(null);
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getCompAsToPokValue() != null) {
                if (!CompAsToPokValueForNumericalPokValue.equals(attributes.getCompAsToPokValue(), identicalFieldsInAttributesForNumericalPokValue.getCompAsToPokValue())) {
                    identicalFieldsInAttributesForNumericalPokValue.setCompAsToPokValue(null);
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getPok() != null) {
                if (!PokForNumericalPokValue.equals(attributes.getPok(), identicalFieldsInAttributesForNumericalPokValue.getPok())) {
                    identicalFieldsInAttributesForNumericalPokValue.setPok(null);
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getPokValue() != null) {
                if (!PokValueForNumericalPokValue.equals(attributes.getPokValue(), identicalFieldsInAttributesForNumericalPokValue.getPokValue())) {
                    identicalFieldsInAttributesForNumericalPokValue.setPokValue(null);
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getPermissibleValuePok() != null) {
                if (!PermissibleValuePokForNumericalPokValue.equals(attributes.getPermissibleValuePok(), identicalFieldsInAttributesForNumericalPokValue.getPermissibleValuePok())) {
                    identicalFieldsInAttributesForNumericalPokValue.setPermissibleValuePok(null);
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getEdIzm() != null) {
                if (!EdIzmForNumericalPokValue.equals(attributes.getEdIzm(), identicalFieldsInAttributesForNumericalPokValue.getEdIzm())) {
                    identicalFieldsInAttributesForNumericalPokValue.setEdIzm(null);
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getP1() != null) {
                if (attributes.getP1() == null) {
                    identicalFieldsInAttributesForNumericalPokValue.setP1(null);
                } else {
                    if (identicalFieldsInAttributesForNumericalPokValue.getP1().getHandbook() != null) {
                        if (!HandbookForNumericalPokValue.equals(attributes.getP1(), identicalFieldsInAttributesForNumericalPokValue.getP1())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP1().setHandbook(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP1().getParamPok() != null) {
                        if (!ParamPokForNumericalPokValue.equalsByName(attributes.getP1(), identicalFieldsInAttributesForNumericalPokValue.getP1())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP1().setParamPok(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP1().getHandbookRow() != null) {
                        if (!HandbookRowForNumericalPokValue.equals(attributes.getP1(), identicalFieldsInAttributesForNumericalPokValue.getP1())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP1().setHandbookRow(null);
                        }
                    }
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getP2() != null) {
                if (attributes.getP2() == null) {
                    identicalFieldsInAttributesForNumericalPokValue.setP2(null);
                } else {
                    if (identicalFieldsInAttributesForNumericalPokValue.getP2().getHandbook() != null) {
                        if (!HandbookForNumericalPokValue.equals(attributes.getP2(), identicalFieldsInAttributesForNumericalPokValue.getP2())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP2().setHandbook(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP2().getParamPok() != null) {
                        if (!ParamPokForNumericalPokValue.equalsByName(attributes.getP2(), identicalFieldsInAttributesForNumericalPokValue.getP2())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP2().setParamPok(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP2().getHandbookRow() != null) {
                        if (!HandbookRowForNumericalPokValue.equals(attributes.getP2(), identicalFieldsInAttributesForNumericalPokValue.getP2())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP2().setHandbookRow(null);
                        }
                    }
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getP3() != null) {
                if (attributes.getP3() == null) {
                    identicalFieldsInAttributesForNumericalPokValue.setP3(null);
                } else {
                    if (identicalFieldsInAttributesForNumericalPokValue.getP3().getHandbook() != null) {
                        if (!HandbookForNumericalPokValue.equals(attributes.getP3(), identicalFieldsInAttributesForNumericalPokValue.getP3())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP3().setHandbook(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP3().getParamPok() != null) {
                        if (!ParamPokForNumericalPokValue.equalsByName(attributes.getP3(), identicalFieldsInAttributesForNumericalPokValue.getP3())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP3().setParamPok(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP3().getHandbookRow() != null) {
                        if (!HandbookRowForNumericalPokValue.equals(attributes.getP3(), identicalFieldsInAttributesForNumericalPokValue.getP3())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP3().setHandbookRow(null);
                        }
                    }
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getP4() != null) {
                if (attributes.getP4() == null) {
                    identicalFieldsInAttributesForNumericalPokValue.setP4(null);
                } else {
                    if (identicalFieldsInAttributesForNumericalPokValue.getP4().getHandbook() != null) {
                        if (!HandbookForNumericalPokValue.equals(attributes.getP4(), identicalFieldsInAttributesForNumericalPokValue.getP4())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP4().setHandbook(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP4().getParamPok() != null) {
                        if (!ParamPokForNumericalPokValue.equalsByName(attributes.getP4(), identicalFieldsInAttributesForNumericalPokValue.getP4())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP4().setParamPok(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP4().getHandbookRow() != null) {
                        if (!HandbookRowForNumericalPokValue.equals(attributes.getP4(), identicalFieldsInAttributesForNumericalPokValue.getP4())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP4().setHandbookRow(null);
                        }
                    }
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getP5() != null) {
                if (attributes.getP5() == null) {
                    identicalFieldsInAttributesForNumericalPokValue.setP5(null);
                } else {
                    if (identicalFieldsInAttributesForNumericalPokValue.getP5().getHandbook() != null) {
                        if (!HandbookForNumericalPokValue.equals(attributes.getP5(), identicalFieldsInAttributesForNumericalPokValue.getP5())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP5().setHandbook(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP5().getParamPok() != null) {
                        if (!ParamPokForNumericalPokValue.equalsByName(attributes.getP5(), identicalFieldsInAttributesForNumericalPokValue.getP5())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP5().setParamPok(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP5().getHandbookRow() != null) {
                        if (!HandbookRowForNumericalPokValue.equals(attributes.getP5(), identicalFieldsInAttributesForNumericalPokValue.getP5())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP5().setHandbookRow(null);
                        }
                    }
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getP6() != null) {
                if (attributes.getP6() == null) {
                    identicalFieldsInAttributesForNumericalPokValue.setP6(null);
                } else {
                    if (identicalFieldsInAttributesForNumericalPokValue.getP6().getHandbook() != null) {
                        if (!HandbookForNumericalPokValue.equals(attributes.getP6(), identicalFieldsInAttributesForNumericalPokValue.getP6())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP6().setHandbook(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP6().getParamPok() != null) {
                        if (!ParamPokForNumericalPokValue.equalsByName(attributes.getP6(), identicalFieldsInAttributesForNumericalPokValue.getP6())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP6().setParamPok(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP6().getHandbookRow() != null) {
                        if (!HandbookRowForNumericalPokValue.equals(attributes.getP6(), identicalFieldsInAttributesForNumericalPokValue.getP6())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP6().setHandbookRow(null);
                        }
                    }
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getP7() != null) {
                if (attributes.getP7() == null) {
                    identicalFieldsInAttributesForNumericalPokValue.setP7(null);
                } else {
                    if (identicalFieldsInAttributesForNumericalPokValue.getP7().getHandbook() != null) {
                        if (!HandbookForNumericalPokValue.equals(attributes.getP7(), identicalFieldsInAttributesForNumericalPokValue.getP7())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP7().setHandbook(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP7().getParamPok() != null) {
                        if (!ParamPokForNumericalPokValue.equalsByName(attributes.getP7(), identicalFieldsInAttributesForNumericalPokValue.getP7())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP7().setParamPok(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP7().getHandbookRow() != null) {
                        if (!HandbookRowForNumericalPokValue.equals(attributes.getP7(), identicalFieldsInAttributesForNumericalPokValue.getP7())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP7().setHandbookRow(null);
                        }
                    }
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getP8() != null) {
                if (attributes.getP8() == null) {
                    identicalFieldsInAttributesForNumericalPokValue.setP8(null);
                } else {
                    if (identicalFieldsInAttributesForNumericalPokValue.getP8().getHandbook() != null) {
                        if (!HandbookForNumericalPokValue.equals(attributes.getP8(), identicalFieldsInAttributesForNumericalPokValue.getP8())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP8().setHandbook(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP8().getParamPok() != null) {
                        if (!ParamPokForNumericalPokValue.equalsByName(attributes.getP8(), identicalFieldsInAttributesForNumericalPokValue.getP8())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP8().setParamPok(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP8().getHandbookRow() != null) {
                        if (!HandbookRowForNumericalPokValue.equals(attributes.getP8(), identicalFieldsInAttributesForNumericalPokValue.getP8())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP8().setHandbookRow(null);
                        }
                    }
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getP9() != null) {
                if (attributes.getP9() == null) {
                    identicalFieldsInAttributesForNumericalPokValue.setP9(null);
                } else {
                    if (identicalFieldsInAttributesForNumericalPokValue.getP9().getHandbook() != null) {
                        if (!HandbookForNumericalPokValue.equals(attributes.getP9(), identicalFieldsInAttributesForNumericalPokValue.getP9())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP9().setHandbook(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP9().getParamPok() != null) {
                        if (!ParamPokForNumericalPokValue.equalsByName(attributes.getP9(), identicalFieldsInAttributesForNumericalPokValue.getP9())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP9().setParamPok(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP9().getHandbookRow() != null) {
                        if (!HandbookRowForNumericalPokValue.equals(attributes.getP9(), identicalFieldsInAttributesForNumericalPokValue.getP9())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP9().setHandbookRow(null);
                        }
                    }
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getP10() != null) {
                if (attributes.getP10() == null) {
                    identicalFieldsInAttributesForNumericalPokValue.setP10(null);
                } else {
                    if (identicalFieldsInAttributesForNumericalPokValue.getP10().getHandbook() != null) {
                        if (!HandbookForNumericalPokValue.equals(attributes.getP10(), identicalFieldsInAttributesForNumericalPokValue.getP10())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP10().setHandbook(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP10().getParamPok() != null) {
                        if (!ParamPokForNumericalPokValue.equalsByName(attributes.getP10(), identicalFieldsInAttributesForNumericalPokValue.getP10())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP10().setParamPok(null);
                        }
                    }
                    if (identicalFieldsInAttributesForNumericalPokValue.getP10().getHandbookRow() != null) {
                        if (!HandbookRowForNumericalPokValue.equals(attributes.getP10(), identicalFieldsInAttributesForNumericalPokValue.getP10())) {
                            identicalFieldsInAttributesForNumericalPokValue.getP10().setHandbookRow(null);
                        }
                    }
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getOffsetDate() != null) {
                if (!OffsetDateForNumericalPokValue.equals(attributes.getOffsetDate(), identicalFieldsInAttributesForNumericalPokValue.getOffsetDate())) {
                    identicalFieldsInAttributesForNumericalPokValue.setOffsetDate(null);
                }
            }
            if (identicalFieldsInAttributesForNumericalPokValue.getHourReport() != null) {
                if (!attributes.getHourReport().equals(identicalFieldsInAttributesForNumericalPokValue.getHourReport())) {
                    identicalFieldsInAttributesForNumericalPokValue.setHourReport(null);
                }
            }
        }
    }

    void setDisableOrActiveParamPokCheckBoxes() {
        if (listParamPokProperty.getValue().size() > 0
                && listParamPokProperty.getValue().get(0).getHandbook() != null
                && identicalFieldsInAttributesForNumericalPokValue != null
                && identicalFieldsInAttributesForNumericalPokValue.getP1() != null
                && identicalFieldsInAttributesForNumericalPokValue.getP1().getHandbook() != null
                && HandbookForNumericalPokValue.equals(
                identicalFieldsInAttributesForNumericalPokValue.getP1().getHandbook(),
                HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(0).getHandbook()))
        ) {
            ParamPok1CheckBoxElement.setDisable(false);
        } else {
            ParamPok1CheckBoxElement.setSelected(false);
            ParamPok1CheckBoxElement.setDisable(true);
        }
        if (listParamPokProperty.getValue().size() > 1
                && listParamPokProperty.getValue().get(1).getHandbook() != null
                && identicalFieldsInAttributesForNumericalPokValue != null
                && identicalFieldsInAttributesForNumericalPokValue.getP2() != null
                && identicalFieldsInAttributesForNumericalPokValue.getP2().getHandbook() != null
                && HandbookForNumericalPokValue.equals(
                identicalFieldsInAttributesForNumericalPokValue.getP2().getHandbook(),
                HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(1).getHandbook()))
        ) {
            ParamPok2CheckBoxElement.setDisable(false);
        } else {
            ParamPok2CheckBoxElement.setSelected(false);
            ParamPok2CheckBoxElement.setDisable(true);
        }
        if (listParamPokProperty.getValue().size() > 2
                && listParamPokProperty.getValue().get(2).getHandbook() != null
                && identicalFieldsInAttributesForNumericalPokValue != null
                && identicalFieldsInAttributesForNumericalPokValue.getP3() != null
                && identicalFieldsInAttributesForNumericalPokValue.getP3().getHandbook() != null
                && HandbookForNumericalPokValue.equals(
                identicalFieldsInAttributesForNumericalPokValue.getP3().getHandbook(),
                HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(2).getHandbook()))
        ) {
            ParamPok3CheckBoxElement.setDisable(false);
        } else {
            ParamPok3CheckBoxElement.setSelected(false);
            ParamPok3CheckBoxElement.setDisable(true);
        }
        if (listParamPokProperty.getValue().size() > 3
                && listParamPokProperty.getValue().get(3).getHandbook() != null
                && identicalFieldsInAttributesForNumericalPokValue != null
                && identicalFieldsInAttributesForNumericalPokValue.getP4() != null
                && identicalFieldsInAttributesForNumericalPokValue.getP4().getHandbook() != null
                && HandbookForNumericalPokValue.equals(
                identicalFieldsInAttributesForNumericalPokValue.getP4().getHandbook(),
                HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(3).getHandbook()))
        ) {
            ParamPok4CheckBoxElement.setDisable(false);
        } else {
            ParamPok4CheckBoxElement.setSelected(false);
            ParamPok4CheckBoxElement.setDisable(true);
        }
        if (listParamPokProperty.getValue().size() > 4
                && listParamPokProperty.getValue().get(4).getHandbook() != null
                && identicalFieldsInAttributesForNumericalPokValue != null
                && identicalFieldsInAttributesForNumericalPokValue.getP5() != null
                && identicalFieldsInAttributesForNumericalPokValue.getP5().getHandbook() != null
                && HandbookForNumericalPokValue.equals(
                identicalFieldsInAttributesForNumericalPokValue.getP5().getHandbook(),
                HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(4).getHandbook()))
        ) {
            ParamPok5CheckBoxElement.setDisable(false);
        } else {
            ParamPok5CheckBoxElement.setSelected(false);
            ParamPok5CheckBoxElement.setDisable(true);
        }
        if (listParamPokProperty.getValue().size() > 5
                && listParamPokProperty.getValue().get(5).getHandbook() != null
                && identicalFieldsInAttributesForNumericalPokValue != null
                && identicalFieldsInAttributesForNumericalPokValue.getP6() != null
                && identicalFieldsInAttributesForNumericalPokValue.getP6().getHandbook() != null
                && HandbookForNumericalPokValue.equals(
                identicalFieldsInAttributesForNumericalPokValue.getP6().getHandbook(),
                HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(5).getHandbook()))
        ) {
            ParamPok6CheckBoxElement.setDisable(false);
        } else {
            ParamPok6CheckBoxElement.setSelected(false);
            ParamPok6CheckBoxElement.setDisable(true);
        }
        if (listParamPokProperty.getValue().size() > 6
                && listParamPokProperty.getValue().get(6).getHandbook() != null
                && identicalFieldsInAttributesForNumericalPokValue != null
                && identicalFieldsInAttributesForNumericalPokValue.getP7() != null
                && identicalFieldsInAttributesForNumericalPokValue.getP7().getHandbook() != null
                && HandbookForNumericalPokValue.equals(
                identicalFieldsInAttributesForNumericalPokValue.getP7().getHandbook(),
                HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(6).getHandbook()))
        ) {
            ParamPok7CheckBoxElement.setDisable(false);
        } else {
            ParamPok7CheckBoxElement.setSelected(false);
            ParamPok7CheckBoxElement.setDisable(true);
        }
        if (listParamPokProperty.getValue().size() > 7
                && listParamPokProperty.getValue().get(7).getHandbook() != null
                && identicalFieldsInAttributesForNumericalPokValue != null
                && identicalFieldsInAttributesForNumericalPokValue.getP8() != null
                && identicalFieldsInAttributesForNumericalPokValue.getP8().getHandbook() != null
                && HandbookForNumericalPokValue.equals(
                identicalFieldsInAttributesForNumericalPokValue.getP8().getHandbook(),
                HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(7).getHandbook()))
        ) {
            ParamPok8CheckBoxElement.setDisable(false);
        } else {
            ParamPok8CheckBoxElement.setSelected(false);
            ParamPok8CheckBoxElement.setDisable(true);
        }
        if (listParamPokProperty.getValue().size() > 8
                && listParamPokProperty.getValue().get(8).getHandbook() != null
                && identicalFieldsInAttributesForNumericalPokValue != null
                && identicalFieldsInAttributesForNumericalPokValue.getP9() != null
                && identicalFieldsInAttributesForNumericalPokValue.getP9().getHandbook() != null
                && HandbookForNumericalPokValue.equals(
                identicalFieldsInAttributesForNumericalPokValue.getP9().getHandbook(),
                HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(8).getHandbook()))
        ) {
            ParamPok9CheckBoxElement.setDisable(false);
        } else {
            ParamPok9CheckBoxElement.setSelected(false);
            ParamPok9CheckBoxElement.setDisable(true);
        }
        if (listParamPokProperty.getValue().size() > 9
                && listParamPokProperty.getValue().get(9).getHandbook() != null
                && identicalFieldsInAttributesForNumericalPokValue != null
                && identicalFieldsInAttributesForNumericalPokValue.getP10() != null
                && identicalFieldsInAttributesForNumericalPokValue.getP10().getHandbook() != null
                && HandbookForNumericalPokValue.equals(
                identicalFieldsInAttributesForNumericalPokValue.getP10().getHandbook(),
                HandbookForNumericalPokValue.get(listParamPokProperty.getValue().get(9).getHandbook()))
        ) {
            ParamPok10CheckBoxElement.setDisable(false);
        } else {
            ParamPok10CheckBoxElement.setSelected(false);
            ParamPok10CheckBoxElement.setDisable(true);
        }
    }

    void setDisableOrActiveHourReportAndOffsetDateCheckBoxes() {
        if (generalParamsService.getListSelectedCellFormMarkInfo().size() != 0) {
            HourReportCheckBoxElement.setDisable(false);
            OffsetDateCheckBoxElement.setDisable(false);
        } else {
            HourReportCheckBoxElement.setSelected(false);
            HourReportCheckBoxElement.setDisable(true);
            OffsetDateCheckBoxElement.setSelected(false);
            OffsetDateCheckBoxElement.setDisable(true);
        }
    }

    public boolean checkButtonCancelMarkUpIsDisable() {
        return generalParamsService.getListSelectedCellFormMarkInfo().size() == 0;
    }

    //------------------------------------------------------------------------------------------------------------------
    //???????????????? ???????????????? ???? ????????

    void setValuesToCells(List<NumericalPokValue> listNumericalPokValueWithValues) throws IndexOutOfBoundsException {
        String error;
        Double value;
        NumericalPokValueInfoForNumericalPokValue numericalPokValueInfo;
        for (NumericalPokValue numericalPokValue : listNumericalPokValueWithValues) {
            CellInfoForNumericalPokValue cellInfo = numericalPokValue.getCellInfo();
            if (cellInfo == null) {
                System.out.println("?????????????????????? ???????????????????? ?? ????????????");
                continue;
            }
            XCell xCell = generalParamsService.getActiveSheet().getCellByPosition(convertColToNumString(cellInfo.getColumnNumber()), cellInfo.getRowNumber() - 1);

            numericalPokValueInfo = numericalPokValue.getNumericalPokValueInfo();
            if (numericalPokValueInfo != null) {
                error = numericalPokValueInfo.getError();
                value = numericalPokValueInfo.getValue();
                if (error != null) {
                    xCell.setFormula(error);
                } else if (value != null) {
                    xCell.setValue(value);
                } else {
                    xCell.setFormula("");
                }
            } else {
                //???????????? ???? ???????????? ??????????????????, ???? ?????????????? ???? ???????????? ????????????
                xCell.setFormula("");
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //?????????? ???????????? ????????????????????
    private GroupPok findSuitableGroupInChildrenElements(ArrayList<GroupPok> listChildrenGroupPok, Integer idGroupPok) {
        if (listChildrenGroupPok == null) {
            return null;
        }
        for (GroupPok childGroupPok : listChildrenGroupPok) {
            if (childGroupPok.getCorTip().equals("D")) {
                continue;
            }
            if (childGroupPok.getId().equals(idGroupPok)) {
                return childGroupPok;
            }
            GroupPok suitableGroupInChildrenElements = findSuitableGroupInChildrenElements(childGroupPok.getListGroupPok(), idGroupPok);
            if (suitableGroupInChildrenElements != null) {
                return suitableGroupInChildrenElements;
            }
        }
        return null;
    }

    private GroupPok getRootGroupPok() {
        //?????????????????? ???????????? ?????????? ??????????????????????
        ArrayList<GroupPok> listGroupPok;
        try {
            listGroupPok = Main.coreService.getGroupPokTree();
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "???????????? ?????? ?????????????????? ???????????? ?????????? ??????????????????????\n" + e.getMessage());
            throw new SetFiltersException("???????????? ?????? ?????????????????? ???????????? ?????????? ??????????????????????", e);
        }
        if (listGroupPok.size() != 1) {
            createAlertWarning(this.stage, "?? ???????????? ?????????? ?????????????????????? ???????????? ???????? ???????? ???????????????? ????????????");
            throw new SetFiltersException("?? ???????????? ?????????? ?????????????????????? ???????????? ???????? ???????? ???????????????? ????????????");
        }
        return listGroupPok.get(0);
    }

    private void setGroupPokProperty(NumericalPokValue numericalPokValueToSetFilters) {
        if (numericalPokValueToSetFilters == null) {
            groupPokProperty.setValueCustom(null);
            return;
        }

        GroupPok rootGroupPok;
        try {
            rootGroupPok = getRootGroupPok();
        } catch (Exception e) {
            groupPokProperty.setValueCustom(null);
            return;
        }

        Pok pok;
        try {
            pok = Main.coreService.getPokById(numericalPokValueToSetFilters.getAttributes().getPok().getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new SetFiltersException("???????????? ?????? ?????????????????? ?????????????? ???????????????????? ???? ???? ???????????????????? ?? ?????????????? numericalPokValue", e);
        }
        if (pok == null) {
            throw new SetFiltersException("???? ???????????? ???????????????????? ???? ???? ???????????????????? ?? ?????????????? numericalPokValue");
        }
        GroupPok suitableGroup = findSuitableGroupInChildrenElements(rootGroupPok.getListGroupPok(), pok.getFkGroupPok());
        if (suitableGroup == null) {
            throw new SetFiltersException("???? ?????????????? ???????????? ?????????????????????? ???? numericalPokValue ?? ???????????? ?????????? ??????????????????????");
        }
        groupPokProperty.setValueCustom(suitableGroup, numericalPokValueToSetFilters, null);
    }

    public void openModalToSelectGroup(GroupPok rootGroupPok) {
        GroupPok oldGroupPok = groupPokProperty.getValue();
        deactivateFiltersForSelectGroupPok();
        GroupPokController groupPokController;
        try {
            groupPokController = new GroupPokController();
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "???????????? ?????? ???????????????? ???????????????????? ???????? ???? ???????????? ???????????? ??????????????????????\n" + e.getMessage());
            groupPokProperty.setValueCustom(null);
            return;
        }
        Stage stage = new Stage();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //???????????? ?????????????????????? ???? ???????? ??????????????, ?? ?????????????????? ???????? ???????? ??????????????
                if (groupPokProperty.getValue() == null) {
                    groupPokProperty.setValueCustom(null);
                }
            }
        });
        stage.setScene(new Scene(groupPokController));
        stage.setTitle("?????????? ???????????? ??????????????????????");
        stage.getIcons().add(new javafx.scene.image.Image(ConstructorModeController.class.getResourceAsStream(
                "/assets/icon_32.png")));

//        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        groupPokController.setInitialParams(this, rootGroupPok, oldGroupPok);
        groupPokController.setStage(stage);
        stage.showAndWait();
    }

    //------------------------------------------------------------------------------------------------------------------
    //?????????????????? ???????????? ??????????????????????

    void setListPokAndSelectFirstElement(NumericalPokValue numericalPokValueToSetFilters) {
        ArrayList<Pok> listPok;
        Integer groupPokId = groupPokProperty.getValue() != null ? groupPokProperty.getValue().getId() : null;
        if (groupPokId == null) {
            if (numericalPokValueToSetFilters == null) {
                PokElement.setDisable(true);
                PokElement.clear();
                return;
            }
            throw new SetFiltersException("???? ???????????? ?????????????????????? ?????????? null");
        }
        try {
            listPok = Main.coreService.getListPok(groupPokId);
        } catch (Exception e) {
            e.printStackTrace();
            if (numericalPokValueToSetFilters == null) {
                createAlertWarning(this.stage, "???????????? ?????? ?????????????????? ???????????? ??????????????????????\n" + e.getMessage());
                PokElement.setDisable(true);
                PokElement.clear();
                return;
            }
            throw new SetFiltersException("???????????? ?????? ?????????????????? ???????????? ??????????????????????", e);
        }
        PokElement.setDisable(false);
        PokElement.setItemsCustom(FXCollections.observableArrayList(listPok));
        if (listPok.size() != 0) {
            if (numericalPokValueToSetFilters == null) {
                PokElement.setValueCustom(PokElement.getItems().get(0));
                return;
            }
            Pok suitablePok = null;
            for (Pok pok : listPok) {
                if (pok.getId().equals(numericalPokValueToSetFilters.getAttributes().getPok().getId())) {
                    suitablePok = pok;
                    break;
                }
            }
            if (suitablePok == null) {
                throw new SetFiltersException("???????????????????? ???? ?????????????? numericalPokValue ???? ???????????? ?? ?????????????? ??????????????????????");
            }
            PokElement.setValueCustom(suitablePok, numericalPokValueToSetFilters, null);
        } else {
            if (numericalPokValueToSetFilters == null) {
                PokElement.setValueCustom(null);
                return;
            }
            throw new SetFiltersException("???????????? ???????????? ???????????? ??????????????????????. ???????????????????? ???? ?????????????? numericalPokValue ???? ????????????");
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //?????????????????? ???????????? ???????????????? ??????????????????????
    void setListPokValueAndSelectFirstElement(NumericalPokValue numericalPokValueToSetFilters) {
        ArrayList<PokValue> listPokValue;
        Integer pokId = PokElement.getValue() != null ? PokElement.getValue().getId() : null;
        if (pokId == null) {
            if (numericalPokValueToSetFilters == null) {
                PokValueElement.setDisable(true);
                PokValueElement.clear();
                PokValueElementCheckBoxElement.setDisable(true);
                return;
            }
            throw new SetFiltersException("???? ???????????????????? ?????????? null");
        }
        try {
            listPokValue = Main.coreService.getListPokValue(pokId);
        } catch (Exception e) {
            e.printStackTrace();
            if (numericalPokValueToSetFilters == null) {
                createAlertWarning(this.stage, "???????????? ?????? ?????????????????? ???????????? ???????????????? ??????????????????????\n" + e.getMessage());
                PokValueElement.setDisable(true);
                PokValueElement.clear();
                PokValueElementCheckBoxElement.setDisable(true);
                return;
            }
            throw new SetFiltersException("???????????? ?????? ?????????????????? ???????????? ???????????????? ??????????????????????", e);
        }
        PokValueElement.setDisable(false);
        PokValueElement.setItemsCustom(FXCollections.observableArrayList(listPokValue));
        PokValueElementCheckBoxElement.setDisable(false);
        if (listPokValue.size() != 0) {
            if (numericalPokValueToSetFilters == null) {
                PokValueElement.setValueCustom(PokValueElement.getItems().get(0));
                return;
            }
            PokValue suitablePokValue = null;
            for (PokValue pokValue : listPokValue) {
                if (pokValue.getId().equals(numericalPokValueToSetFilters.getAttributes().getPokValue().getId())) {
                    suitablePokValue = pokValue;
                    break;
                }
            }
            if (suitablePokValue == null) {
                throw new SetFiltersException("???????????????? ???????????????????? ???? ?????????????? numericalPokValue ???? ?????????????? ?? ?????????????? ???????????????? ????????????????????");
            }
            PokValueElement.setValueCustom(suitablePokValue, numericalPokValueToSetFilters, null);
        } else {
            if (numericalPokValueToSetFilters == null) {
                PokValueElement.setValueCustom(null);
                return;
            }
            throw new SetFiltersException("???????????? ???????????? ???????????? ???????????????? ????????????????????. ???????????????? ???????????????????? ???? ?????????????? numericalPokValue ???? ??????????????");
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //?????????????????? ???????????????????? ??????????????????????
    public ArrayList<HandbookInnerRecord> getFilteredListForParamPokElement(
            Integer paramPokIndex
    ) {
        List<HandbookInnerRecord> filteredListHandbookInnerRecord;
        if (paramPokIndex != 0) {
            filteredListHandbookInnerRecord = listParamPokElementList.get(paramPokIndex).stream().filter((v) ->
                    v.getHandbookRow().getFkZone() == 0
                            || ParamPok1Element.getValue() == null
                            || v.getHandbookRow().getFkZone().equals(ParamPok1Element.getValue().getHandbookRow().getCode())
            ).collect(Collectors.toCollection(ArrayList::new));
        } else {
            filteredListHandbookInnerRecord = listParamPokElementList.get(paramPokIndex);
        }
        return new ArrayList<HandbookInnerRecord>(filteredListHandbookInnerRecord);
    }

    ArrayList<ArrayList<HandbookInnerRecord>> listParamPokElementList = new ArrayList<ArrayList<HandbookInnerRecord>>() {{
        add(new ArrayList<>());
        add(new ArrayList<>());
        add(new ArrayList<>());
        add(new ArrayList<>());
        add(new ArrayList<>());
        add(new ArrayList<>());
        add(new ArrayList<>());
        add(new ArrayList<>());
        add(new ArrayList<>());
        add(new ArrayList<>());
    }};

    void setListHandbookInnerRecordAndSelectFirstElement(Integer paramPokIndex, ParamPok paramPok, ComboBoxAutoComplete<HandbookInnerRecord> ParamPokElement, Label ParamPokText, NumericalPokValue numericalPokValueToSetFilters, ParamPokInfoForNumericalPokValue paramPokInfoForNumericalPokValueToSetFilters) {
        if (paramPok == null) {
            if (numericalPokValueToSetFilters == null) {
                ParamPokText.setText("");
                ParamPokElement.setDisable(true);
                ParamPokElement.clear();
                return;
            }
            if (paramPokInfoForNumericalPokValueToSetFilters == null) {
                ParamPokText.setText("");
                ParamPokElement.setDisable(true);
                ParamPokElement.setItemsCustom(FXCollections.observableArrayList(new ArrayList<HandbookInnerRecord>()));
                ParamPokElement.setValueCustom(null, numericalPokValueToSetFilters, null);
                return;
            }
            throw new SetFiltersException("?? ?????????????? numericalPokValue ???????? ???????????????????? ???????????????? ???????????????????? p" + (paramPokIndex + 1) + ", ???? ?? ???????????? ???????????????????? ???????????????????? ?????? ??????");
        }
        if (numericalPokValueToSetFilters != null) {
            if (paramPokInfoForNumericalPokValueToSetFilters == null) {
                throw new SetFiltersException("?? ?????????????? numericalPokValue ?????? ?????????????????????? ?????????????????? ???????????????????? p" + (paramPokIndex + 1) + ", ???? ?? ???????????? ???????????????????? ???????????????????? ???? ????????");
            } else if (!paramPok.getId().equals(paramPokInfoForNumericalPokValueToSetFilters.getParamPok().getId())) {
                throw new SetFiltersException("?? ?????????????? numericalPokValue ???????????????? ???????????????????? p" + (paramPokIndex + 1) + " ???? ?????????????????? ?? ???????????????????? ???????????????????? ?? ???????????? ???????????????????? ????????????????????");
            }
        }
        //?????????????????????????? ??????
        ParamPokText.setText("[" + paramPok.getId() + "] " + paramPok.getName() + " ([" + paramPok.getHandbook().getId() + "] " + paramPok.getHandbook().getHandbookName() + ")");

        //???????????????????? ???????????? ?????? ???????????????????? ??????????????????????
        ArrayList<HandbookInnerRecord> listHandbookInnerRecord;
        Integer fkHandbook = paramPok.getFkHandbook();
        if (fkHandbook == null) {
            if (numericalPokValueToSetFilters == null) {
                ParamPokElement.setDisable(true);
                listParamPokElementList.get(paramPokIndex).clear();
                ParamPokElement.clear();
                return;
            }
            throw new SetFiltersException("?? ?????????????????? ???????????????????? p" + (paramPokIndex + 1) + " ???? ?????????? fkHandbook");
        }
        try {
            listHandbookInnerRecord = Main.coreService.getListHandbookInnerRecord(fkHandbook);
        } catch (Exception e) {
            e.printStackTrace();
            if (numericalPokValueToSetFilters == null) {
                createAlertWarning(this.stage, "???????????? ?????? ?????????????????? ???????????? ?????????????????? ???????????????? ?????????????????? ???????????????????? p" + (paramPokIndex + 1) + "\n" + e.getMessage());
                ParamPokElement.setDisable(true);
                ParamPokElement.clear();
                return;
            }
            throw new SetFiltersException("???????????? ?????? ?????????????????? ???????????? ?????????????????? ???????????????? ?????????????????? ???????????????????? [" + (paramPokIndex + 1), e);
        }
        ParamPokElement.setDisable(false);
        listParamPokElementList.get(paramPokIndex).clear();
        listParamPokElementList.get(paramPokIndex).addAll(listHandbookInnerRecord);
        //???????????????????? ??????????????
        listHandbookInnerRecord = getFilteredListForParamPokElement(paramPokIndex);
        ParamPokElement.setItemsCustom(FXCollections.observableArrayList(listHandbookInnerRecord));
        if (listHandbookInnerRecord.size() != 0) {
            if (numericalPokValueToSetFilters == null) {
                ParamPokElement.setValueCustom(listHandbookInnerRecord.get(0));
                return;
            }
            HandbookInnerRecord suitableHandbookInnerRecord = null;
            for (HandbookInnerRecord handbookInnerRecord : listHandbookInnerRecord) {
                if (handbookInnerRecord.getHandbookRow().getId().equals(paramPokInfoForNumericalPokValueToSetFilters.getHandbookRow().getId())) {
                    suitableHandbookInnerRecord = handbookInnerRecord;
                }
            }
            if (suitableHandbookInnerRecord == null) {
                throw new SetFiltersException("???????????? ???????????????????????????? ?????? ?????????????????? ???????????????????? p" + (paramPokIndex + 1) + " ???? ?????????????? numericalPokValue ???? ?????????????? ?? ???????????? ?????????????? ???????????????????????????? ?????? ?????????????????? ????????????????????");
            }
            ParamPokElement.setValueCustom(suitableHandbookInnerRecord, numericalPokValueToSetFilters, null);
        } else {
            if (numericalPokValueToSetFilters == null) {
                ParamPokElement.setValueCustom(null);
                return;
            }
            throw new SetFiltersException("???????????? ???????????? ???????????? ?????????????? ???????????????????????????? ?????? ?????????????????? ????????????????????. ???????????? ???????????????????????????? ?????? ?????????????????? ???????????????????? ???? ??????????????");
        }
    }

    void setListParamPokAndSelectFirstElementForEachParamPok(NumericalPokValue numericalPokValueToSetFilters) {
        Integer pokId = PokElement.getValue() != null ? PokElement.getValue().getId() : null;
        if (pokId == null) {
            if (numericalPokValueToSetFilters == null) {
                listParamPokProperty.setValue(FXCollections.observableList(new ArrayList<ParamPok>()));
            } else {
                throw new SetFiltersException("???? ???????????????????? ?????????? null");
            }
        } else {
            try {
                listParamPokProperty.setValue(FXCollections.observableList(Main.coreService.getListParamPok(pokId)));
            } catch (Exception e) {
                e.printStackTrace();
                if (numericalPokValueToSetFilters == null) {
                    createAlertWarning(this.stage, "???????????? ?????? ?????????????????? ???????????? ???????????????????? ??????????????????????\n" + e.getMessage());
                    listParamPokProperty.setValue(FXCollections.observableList(new ArrayList<ParamPok>()));
                } else {
                    throw new SetFiltersException("???????????? ?????? ?????????????????? ???????????? ???????????????????? ??????????????????????", e);
                }
            }
        }
        AttributesForNumericalPokValue attributesForNumericalPokValue = numericalPokValueToSetFilters != null ? numericalPokValueToSetFilters.getAttributes() : null;
        setListHandbookInnerRecordAndSelectFirstElement(0, listParamPokProperty.getValue().size() > 0 ? listParamPokProperty.getValue().get(0) : null, ParamPok1Element, ParamPok1TextElement, numericalPokValueToSetFilters, attributesForNumericalPokValue != null ? attributesForNumericalPokValue.getP1() : null);
        setListHandbookInnerRecordAndSelectFirstElement(1, listParamPokProperty.getValue().size() > 1 ? listParamPokProperty.getValue().get(1) : null, ParamPok2Element, ParamPok2TextElement, numericalPokValueToSetFilters, attributesForNumericalPokValue != null ? attributesForNumericalPokValue.getP2() : null);
        setListHandbookInnerRecordAndSelectFirstElement(2, listParamPokProperty.getValue().size() > 2 ? listParamPokProperty.getValue().get(2) : null, ParamPok3Element, ParamPok3TextElement, numericalPokValueToSetFilters, attributesForNumericalPokValue != null ? attributesForNumericalPokValue.getP3() : null);
        setListHandbookInnerRecordAndSelectFirstElement(3, listParamPokProperty.getValue().size() > 3 ? listParamPokProperty.getValue().get(3) : null, ParamPok4Element, ParamPok4TextElement, numericalPokValueToSetFilters, attributesForNumericalPokValue != null ? attributesForNumericalPokValue.getP4() : null);
        setListHandbookInnerRecordAndSelectFirstElement(4, listParamPokProperty.getValue().size() > 4 ? listParamPokProperty.getValue().get(4) : null, ParamPok5Element, ParamPok5TextElement, numericalPokValueToSetFilters, attributesForNumericalPokValue != null ? attributesForNumericalPokValue.getP5() : null);
        setListHandbookInnerRecordAndSelectFirstElement(5, listParamPokProperty.getValue().size() > 5 ? listParamPokProperty.getValue().get(5) : null, ParamPok6Element, ParamPok6TextElement, numericalPokValueToSetFilters, attributesForNumericalPokValue != null ? attributesForNumericalPokValue.getP6() : null);
        setListHandbookInnerRecordAndSelectFirstElement(6, listParamPokProperty.getValue().size() > 6 ? listParamPokProperty.getValue().get(6) : null, ParamPok7Element, ParamPok7TextElement, numericalPokValueToSetFilters, attributesForNumericalPokValue != null ? attributesForNumericalPokValue.getP7() : null);
        setListHandbookInnerRecordAndSelectFirstElement(7, listParamPokProperty.getValue().size() > 7 ? listParamPokProperty.getValue().get(7) : null, ParamPok8Element, ParamPok8TextElement, numericalPokValueToSetFilters, attributesForNumericalPokValue != null ? attributesForNumericalPokValue.getP8() : null);
        setListHandbookInnerRecordAndSelectFirstElement(8, listParamPokProperty.getValue().size() > 8 ? listParamPokProperty.getValue().get(8) : null, ParamPok9Element, ParamPok9TextElement, numericalPokValueToSetFilters, attributesForNumericalPokValue != null ? attributesForNumericalPokValue.getP9() : null);
        setListHandbookInnerRecordAndSelectFirstElement(9, listParamPokProperty.getValue().size() > 9 ? listParamPokProperty.getValue().get(9) : null, ParamPok10Element, ParamPok10TextElement, numericalPokValueToSetFilters, attributesForNumericalPokValue != null ? attributesForNumericalPokValue.getP10() : null);
    }

    //------------------------------------------------------------------------------------------------------------------
    // ???????????? ?? ????????????????????????
    public void setListAsSourceInformationAndSelectFirstElement(ComboBoxAutoComplete<AsSourceInformation> AsSourceInformation, NumericalPokValue numericalPokValueToSetFilters) {
        ArrayList<AsSourceInformation> listAsSourceInformation;
        Integer pokValueId = PokValueElement.getValue() != null ? PokValueElement.getValue().getId() : null;
        if (pokValueId == null) {
            if (numericalPokValueToSetFilters == null) {
                AsSourceInformation.setDisable(true);
                AsSourceInformation.clear();
                return;
            }
            throw new SetFiltersException("???? ???????????????? ???????????????????? ?????????? null");
        }
        try {
            listAsSourceInformation = Main.coreService.getListAsSourceInformation(pokValueId);
        } catch (Exception e) {
            e.printStackTrace();
            if (numericalPokValueToSetFilters == null) {
                createAlertWarning(this.stage, "???????????? ?????? ?????????????????? ???????????? ???????????????????? ????????????????????\n" + e.getMessage());
                AsSourceInformation.setDisable(true);
                AsSourceInformation.clear();
                return;
            }
            throw new SetFiltersException("???????????? ?????? ?????????????????? ???????????? ???? ???????????????????? ????????????????????", e);

        }
        AsSourceInformation.setDisable(false);
        AsSourceInformation.setItemsCustom(FXCollections.observableArrayList(listAsSourceInformation));
        if (listAsSourceInformation.size() != 0) {
            if (numericalPokValueToSetFilters == null) {
                AsSourceInformation.setValueCustom(listAsSourceInformation.get(0));
                return;
            }
            AsSourceInformation suitableAsSourceInformation = null;
            for (AsSourceInformation asSourceInformation : listAsSourceInformation) {
                if (asSourceInformation.getId().equals(numericalPokValueToSetFilters.getAttributes().getAsSourceInformation().getId())) {
                    suitableAsSourceInformation = asSourceInformation;
                    break;
                }
            }
            if (suitableAsSourceInformation == null) {
                throw new SetFiltersException("???? ?????????????????? ???????????????????? ???? ?????????????? numericalPokValue ???? ???????????? ?? ?????????????? ???? ???????????????????? ????????????????????");
            }
            AsSourceInformation.setValueCustom(suitableAsSourceInformation, numericalPokValueToSetFilters, null);
        } else {
            if (numericalPokValueToSetFilters == null) {
                AsSourceInformation.setValueCustom(null);
                return;
            }
            throw new SetFiltersException("???????????? ???????????? ???????????? ???? ???????????????????? ????????????????????. ???? ?????????????????? ???????????????????? ???? ?????????????? numericalPokValue ???? ????????????");
        }
    }

    public void setListCompAsSourceInfAndSelectFirstElement(NumericalPokValue numericalPokValueToSetFilters) {
        ArrayList<CompAsToPokValue> listCompAsToPokValue;
        Integer pokValueId = PokValueElement.getValue() != null ? PokValueElement.getValue().getId() : null;
        Integer asSourceInformationId = AsSourceInformationForPriorityAsAndCompElement.getValue() != null ? AsSourceInformationForPriorityAsAndCompElement.getValue().getId() : null;
        if (pokValueId == null || asSourceInformationId == null) {
            if (numericalPokValueToSetFilters == null) {
                CompAsToPokValueForPriorityAsAndCompElement.setDisable(true);
                CompAsToPokValueForPriorityAsAndCompElement.clear();
                return;
            }
            throw new SetFiltersException("???????? ?????? ?????? ???? ???????????????????? ???? ???? ?????????????????? ???????????????????? ?????? ???? ???????????????? ???????????????????? ?????????? null");
        }
        try {
            listCompAsToPokValue = Main.coreService.getListCompAsToPokValue(PokValueElement.getValue().getId(), AsSourceInformationForPriorityAsAndCompElement.getValue().getId());
        } catch (Exception e) {
            e.printStackTrace();
            if (numericalPokValueToSetFilters == null) {
                createAlertWarning(this.stage, "???????????? ?????? ?????????????????? ???????????? ?????????????????????? ???? ?????????????????? ????????????????????\n" + e.getMessage());
                CompAsToPokValueForPriorityAsAndCompElement.setDisable(true);
                CompAsToPokValueForPriorityAsAndCompElement.clear();
                return;
            }
            throw new SetFiltersException("???????????? ?????? ?????????????????? ???????????? ?????????????????????? ???? ?????????????????? ????????????????????", e);
        }
        CompAsToPokValueForPriorityAsAndCompElement.setDisable(false);
        CompAsToPokValueForPriorityAsAndCompElement.setItemsCustom(FXCollections.observableArrayList(listCompAsToPokValue));
        if (listCompAsToPokValue.size() != 0) {
            if (numericalPokValueToSetFilters == null) {
                CompAsToPokValueForPriorityAsAndCompElement.setValueCustom(listCompAsToPokValue.get(0));
                return;
            }
            CompAsToPokValue suitableCompAsToPokValue = null;
            for (CompAsToPokValue compAsToPokValue : listCompAsToPokValue) {
                if (compAsToPokValue.getId().equals(numericalPokValueToSetFilters.getAttributes().getCompAsToPokValue().getId())) {
                    suitableCompAsToPokValue = compAsToPokValue;
                    break;
                }
            }
            if (suitableCompAsToPokValue == null) {
                throw new SetFiltersException("?????????????????? ???? ?????????????????? ???????????????????? ???? ?????????????? numericalPokValue ???? ???????????? ?? ?????????????? ?????????????????????? ???? ?????????????????? ????????????????????");
            }
            CompAsToPokValueForPriorityAsAndCompElement.setValueCustom(suitableCompAsToPokValue, numericalPokValueToSetFilters, null);
        } else {
            if (numericalPokValueToSetFilters == null) {
                CompAsToPokValueForPriorityAsAndCompElement.setValueCustom(null);
                return;
            }
            throw new SetFiltersException("???????????? ???????????? ???????????? ?????????????????????? ???? ???????????????? ????????????????????. ???????????????? ???????????????????? ???? ?????????????????? ???????????????????? ???? ?????????????? numericalPokValue ???? ??????????????");
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //?????????? ?????????????????? ????????
    public void setListHourReportAndSelectFirstElement(NumericalPokValue numericalPokValueToSetFilters) {
        ObservableList<Integer> listHourReport = FXCollections.observableArrayList(Main.coreService.getListHourReport());
        HourReportElement.setDisable(false);
        HourReportElement.setItemsCustom(listHourReport);
        if (numericalPokValueToSetFilters == null) {
            HourReportElement.setValueCustom(HourReportElement.getItems().get(18));
        } else {
            Integer suitableHourReport = null;
            for (Integer hourReport : listHourReport) {
                if (hourReport.equals(numericalPokValueToSetFilters.getAttributes().getHourReport())) {
                    suitableHourReport = hourReport;
                    break;
                }
            }
            if (suitableHourReport == null) {
                throw new SetFiltersException("???????????????? ?????????????????? ???????? ???? ?????????????? numericalPokValue ???? ?????????????? ?? ?????????????? ???????????????? ??????????");
            }
            HourReportElement.setValueCustom(suitableHourReport, numericalPokValueToSetFilters, null);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //?????????????????? ????????????????
    public void setListOffsetDateAndSelectFirstElement(NumericalPokValue numericalPokValueToSetFilters) {
        ArrayList<OffsetDate> listOffsetDate;
        try {
            listOffsetDate = Main.coreService.getListOffsetDate();
        } catch (Exception e) {
            if (numericalPokValueToSetFilters == null) {
                e.printStackTrace();
                createAlertWarning(this.stage, "???????????? ?????? ?????????????????? ???????????? ???????????????? ???? ????????\n" + e.getMessage());
                OffsetDateElement.setDisable(true);
                OffsetDateElement.clear();
                return;
            }
            throw new SetFiltersException("???????????? ?????? ?????????????????? ???????????? ???????????????? ???? ????????", e);
        }
        OffsetDateElement.setDisable(false);
        OffsetDateElement.setItemsCustom(FXCollections.observableArrayList(listOffsetDate));
        if (listOffsetDate.size() != 0) {
            if (numericalPokValueToSetFilters == null) {
                OffsetDateElement.setValueCustom(OffsetDateElement.getItems().get(0));
                return;
            }
            OffsetDate suitableOffsetDate = null;
            for (OffsetDate offsetDate : listOffsetDate) {
                if (offsetDate == null) {
                    if (numericalPokValueToSetFilters.getAttributes().getOffsetDate() == null) {
                        suitableOffsetDate = new OffsetDate();
                        break;
                    }
                    continue;
                }
                if (offsetDate.getId().equals(numericalPokValueToSetFilters.getAttributes().getOffsetDate().getId())) {
                    suitableOffsetDate = offsetDate;
                    break;
                }
            }
            if (suitableOffsetDate == null) {
                throw new SetFiltersException("???????????????? ???????????????? ???? ???????? ???? ?????????????? numericalPokValue ???? ?????????????? ?? ?????????????? ???????????????? ???? ????????");
            }
            OffsetDateElement.setValueCustom(suitableOffsetDate.getId() != null ? suitableOffsetDate : null, numericalPokValueToSetFilters, null);
        } else {
            if (numericalPokValueToSetFilters == null) {
                OffsetDateElement.setValueCustom(null);
                return;
            }
            throw new SetFiltersException("???????????? ???????????? ???????????? ???????????????? ???? ????????. ???????????????? ???? ???????? ???? ?????????????? numericalPokValue ???? ??????????????");
        }
    }


    boolean checkButtonMarkUpIsDisable() {
        if (generalParamsService.getListSelectedIterationGroupDto().size() != 0) {
            if (generalParamsService.getListSelectedIterationGroupDto().size() > 1) {
                //???????????????????? ?????????????????? ???????????? ?????????? ???????? ???????????????????????? ??????????
                //@ToDo ???????????????????????? ????????????, ?????????? ???????????????????????? ???????????? ?????????? ???????????????????? ?????????????????????? ???????? ???? ??????????????????????????
                return true;
            }
            if (generalParamsService.checkListSelectedCellFormMarkInfoHasSimpleCells()) {
                //???????????????????? ?????????????????? ????????????, ?????????? ???????????????? ?? ?????????????? ????????????, ?? ???????????? ???????????????????????? ????????????
                return true;
            }
            //???????????????? ???????????? ???????????? ???????????????????????? ????????????
            if (generalParamsService.getSelectedCellsCount() == generalParamsService.getListSelectedIterationGroupHandbookDto().size()) {
                //???????????????????? ?????????????????? ????????????, ?????????? ???????????????? ???????????? ???????????? ?????????????????????????????? ???????????????????????? ????????????
                return true;
            }
        }
        if (
                (
                        ParamPok1CheckBoxElement.isSelected() || ParamPok2CheckBoxElement.isSelected() || ParamPok3CheckBoxElement.isSelected()
                                || ParamPok4CheckBoxElement.isSelected() || ParamPok5CheckBoxElement.isSelected() || ParamPok6CheckBoxElement.isSelected()
                                || ParamPok7CheckBoxElement.isSelected() || ParamPok8CheckBoxElement.isSelected() || ParamPok9CheckBoxElement.isSelected()
                                || ParamPok10CheckBoxElement.isSelected()
                                || HourReportCheckBoxElement.isSelected() || OffsetDateCheckBoxElement.isSelected()
                ) || (
                        groupPokProperty.getValue() != null
                                && PokElement.getValue() != null
                                && PokValueElement.getValue() != null
                                &&
                                (
                                        PriorityAsAndCompElement.isSelected() && AsSourceInformationForPriorityAsAndCompElement.getValue() != null && CompAsToPokValueForPriorityAsAndCompElement.getValue() != null
                                                || PriorityAsElement.isSelected() && AsSourceInformationForPriorityAsElement.getValue() != null
                                                || WithoutPriorityElement.isSelected()
                                )
                                && (ParamPok1Element.isDisable() || !ParamPok1Element.isDisable() && ParamPok1Element.getValue() != null)
                                && (ParamPok2Element.isDisable() || !ParamPok2Element.isDisable() && ParamPok2Element.getValue() != null)
                                && (ParamPok3Element.isDisable() || !ParamPok3Element.isDisable() && ParamPok3Element.getValue() != null)
                                && (ParamPok4Element.isDisable() || !ParamPok4Element.isDisable() && ParamPok4Element.getValue() != null)
                                && (ParamPok5Element.isDisable() || !ParamPok5Element.isDisable() && ParamPok5Element.getValue() != null)
                                && (ParamPok6Element.isDisable() || !ParamPok6Element.isDisable() && ParamPok6Element.getValue() != null)
                                && (ParamPok7Element.isDisable() || !ParamPok7Element.isDisable() && ParamPok7Element.getValue() != null)
                                && (ParamPok8Element.isDisable() || !ParamPok8Element.isDisable() && ParamPok8Element.getValue() != null)
                                && (ParamPok9Element.isDisable() || !ParamPok9Element.isDisable() && ParamPok9Element.getValue() != null)
                                && (ParamPok10Element.isDisable() || !ParamPok10Element.isDisable() && ParamPok10Element.getValue() != null)
                                && HourReportElement.getValue() != null
                )
        ) {
            return false;
        }
        return true;
    }

    //------------------------------------------------------------------------------------------------------------------
    //?????????????????????? ???????????????? ?????? ???????????? ???????????? ????????????????????
    private void deactivateFiltersForSelectGroupPok() {
        //?????? ?????????????????? ?????????????? ?????????????????? ???? ?????????????? ??????????????????????????
        groupPokProperty.setValueCustom(null);
    }

    //?????????????????? ???????? ????????????????
    private void deactivateAllFilters() {
        //?????? ?????????????????? ?????????????? ?????????????????? ???? ?????????????? ??????????????????????????
        groupPokProperty.setValueCustom(null);
        //?????????????????????? HourReport
        HourReportElement.setDisable(true);
        HourReportElement.clear();
        //?????????????????????? OffsetDate
        OffsetDateElement.setDisable(true);
        OffsetDateElement.clear();
    }

    public void filterParamPokElement(Integer paramPokIndex, ComboBoxAutoComplete<HandbookInnerRecord> paramPokElement) {
        if (paramPokIndex == 0) {
            return;
        }
        ArrayList<HandbookInnerRecord> filteredListHandbookInnerRecord = getFilteredListForParamPokElement(paramPokIndex);
        paramPokElement.setItemsCustom(FXCollections.observableArrayList(filteredListHandbookInnerRecord));
        //???????????????? ???????????? ?????????????? ?? ?????????????????????????????? ??????????????, ?????????????? ?????????????????? ?? ?????????????????? ??????????????
        ArrayList<HandbookInnerRecord> elementsInFilteredListSimilarToSelectedElement = filteredListHandbookInnerRecord.stream()
                .filter((v) -> v == paramPokElement.getValue())
                .collect(Collectors.toCollection(ArrayList::new));
        //???????? ?? ?????????????????????????????? ?????????????? ???????????????? ?????????????????? ????????????, ???? ???? ???????????? ???? ????????????
        //???????? ?????????????????? ???????????? ???????? ??????????????????????????, ???? ???????????????? ???????????? ???????????? ???? ?????????????????????? ????????????
        if (elementsInFilteredListSimilarToSelectedElement.size() == 0) {
            paramPokElement.setValueCustom(filteredListHandbookInnerRecord.size() != 0 ? filteredListHandbookInnerRecord.get(0) : null);
        }
    }

    public void filterAllParamPokElements() {
        if (ParamPok1Element.getValue() == null) {
            return;
        }
        filterParamPokElement(1, ParamPok2Element);
        filterParamPokElement(2, ParamPok3Element);
        filterParamPokElement(3, ParamPok4Element);
        filterParamPokElement(4, ParamPok5Element);
        filterParamPokElement(5, ParamPok6Element);
        filterParamPokElement(6, ParamPok7Element);
        filterParamPokElement(7, ParamPok8Element);
        filterParamPokElement(8, ParamPok9Element);
        filterParamPokElement(9, ParamPok10Element);
    }

    public void onSelectGroupPok(NumericalPokValue numericalPokValueToSetFilters) {
        if (groupPokProperty.getValue() != null) {
            GroupPokTextElement.setText("[" + groupPokProperty.getValue().getId() + "] " + groupPokProperty.getValue().getName());
        } else {
            GroupPokTextElement.setText("");
        }
        setListPokAndSelectFirstElement(numericalPokValueToSetFilters);
    }

    public void onSelectPok(NumericalPokValue numericalPokValueToSetFilters) {
        setListParamPokAndSelectFirstElementForEachParamPok(numericalPokValueToSetFilters);
        setListPokValueAndSelectFirstElement(numericalPokValueToSetFilters);
    }

    public void onSelectPokValue(NumericalPokValue numericalPokValueToSetFilters) {
        Integer pokValueId = PokValueElement.getValue() != null ? PokValueElement.getValue().getId() : null;
        if (pokValueId != null) {
            PriorityAsAndCompElement.setDisable(false);
            PriorityAsElement.setDisable(false);
            WithoutPriorityElement.setDisable(false);
        } else {
            PriorityAsAndCompElement.setDisable(true);
            PriorityAsElement.setDisable(true);
            WithoutPriorityElement.setDisable(true);
        }

        if (numericalPokValueToSetFilters == null) {
            if (PriorityAsAndCompElement.isSelected()) {
                onFirePriorityAsAndComp(numericalPokValueToSetFilters);
            } else if (PriorityAsElement.isSelected()) {
                onFirePriorityAs(numericalPokValueToSetFilters);
            } else if (WithoutPriorityElement.isSelected()) {
                onFireWithoutPriority(numericalPokValueToSetFilters);
            } else {
                PriorityAsAndCompElement.fireCustom(numericalPokValueToSetFilters);
            }
            return;
        }
        if (numericalPokValueToSetFilters.getAttributes().getPriority().equals(PriorityTypeForNumericalPokValue.priorityAsAndComp)) {
            PriorityAsAndCompElement.fireCustom(numericalPokValueToSetFilters);
        } else if (numericalPokValueToSetFilters.getAttributes().getPriority().equals(PriorityTypeForNumericalPokValue.priorityAs)) {
            PriorityAsElement.fireCustom(numericalPokValueToSetFilters);
        } else if (numericalPokValueToSetFilters.getAttributes().getPriority().equals(PriorityTypeForNumericalPokValue.withoutPriority)) {
            WithoutPriorityElement.fireCustom(numericalPokValueToSetFilters);
        } else {
            throw new SetFiltersException("?????????????????? ???? ?????????????? numericalPokValue ???? ??????????");
        }
    }

    public void onSelectAsSourceInformationForPriorityAsAndComp(NumericalPokValue numericalPokValueToSetFilters) {
        setListCompAsSourceInfAndSelectFirstElement(numericalPokValueToSetFilters);
    }

    public void onFirePriorityAsAndComp(NumericalPokValue numericalPokValueToSetFilters) {
        AsSourceInformationForPriorityAsElement.setDisable(true);
        AsSourceInformationForPriorityAsElement.clear();

        setListAsSourceInformationAndSelectFirstElement(AsSourceInformationForPriorityAsAndCompElement, numericalPokValueToSetFilters);
    }

    public void onFirePriorityAs(NumericalPokValue numericalPokValueToSetFilters) {
        AsSourceInformationForPriorityAsAndCompElement.setDisable(true);
        AsSourceInformationForPriorityAsAndCompElement.clear();
        CompAsToPokValueForPriorityAsAndCompElement.setDisable(true);
        CompAsToPokValueForPriorityAsAndCompElement.clear();

        setListAsSourceInformationAndSelectFirstElement(AsSourceInformationForPriorityAsElement, numericalPokValueToSetFilters);
    }

    public void onFireWithoutPriority(NumericalPokValue numericalPokValueToSetFilters) {
        AsSourceInformationForPriorityAsAndCompElement.setDisable(true);
        AsSourceInformationForPriorityAsAndCompElement.clear();
        CompAsToPokValueForPriorityAsAndCompElement.setDisable(true);
        CompAsToPokValueForPriorityAsAndCompElement.clear();

        AsSourceInformationForPriorityAsElement.setDisable(true);
        AsSourceInformationForPriorityAsElement.clear();
    }


    public void setElementsAction() {
        //???????????????? ???????????? radioButton, ?? ?????????????? ?????? ?????????? ?????????????????????????? (???????? ???????? ?????????????? ???????????? ?????????? ????????????, ???? ?? ?????????????????? ?????????? ???????? ??????????)
        ToggleGroup group = new ToggleGroup();
        PriorityAsAndCompElement.setToggleGroup(group);
        PriorityAsElement.setToggleGroup(group);
        WithoutPriorityElement.setToggleGroup(group);
        //???????????????????? ????????????????????
        listParamPokProperty.addListener(new ListChangeListener<ParamPok>() {
            @Override
            public void onChanged(Change<? extends ParamPok> c) {
                setDisableOrActiveParamPokCheckBoxes();
            }
        });
        //?????????????????? ?????????????? changed
        listParamPokProperty.setValue(FXCollections.observableList(new ArrayList<ParamPok>()));
        generalParamsService.getQuantityUpdateSelectedElementsProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setIdenticalFieldsValuesForListSelectedNumericalPokValues();
                formMarkMetaInfoController.setInitialParams(identicalFieldsInAttributesForNumericalPokValue);

                setDisableOrActiveParamPokCheckBoxes();
                setDisableOrActiveHourReportAndOffsetDateCheckBoxes();
            }
        });
        //?????????????????? ?????????????? changed
        generalParamsService.updateSelectedElements();
        groupPokProperty.setCallBackForSetValueCustom(this::onSelectGroupPok, null);
        groupPokProperty.addChangeListener(new ChangeListener<GroupPok>() {
            @Override
            public void changed(ObservableValue<? extends GroupPok> observable, GroupPok oldValue, GroupPok newValue) {
                onSelectGroupPok(null);
            }
        });
        PokElement.setCallBackForSetValueCustom(this::onSelectPok, null);
        PokElement.addActionEventHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onSelectPok(null);
            }
        });
        PokValueElement.setCallBackForSetValueCustom(this::onSelectPokValue, null);
        PokValueElement.addActionEventHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onSelectPokValue(null);
            }
        });
        ParamPok1Element.addActionEventHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                filterAllParamPokElements();
            }
        });
        AsSourceInformationForPriorityAsAndCompElement.setCallBackForSetValueCustom(this::onSelectAsSourceInformationForPriorityAsAndComp, null);
        AsSourceInformationForPriorityAsAndCompElement.addActionEventHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onSelectAsSourceInformationForPriorityAsAndComp(null);
            }
        });
        PriorityAsAndCompElement.setCallBackForSelectCustom(this::onFirePriorityAsAndComp);
        PriorityAsAndCompElement.addActionEventHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onFirePriorityAsAndComp(null);
            }
        });
        PriorityAsElement.setCallBackForSelectCustom(this::onFirePriorityAs);
        PriorityAsElement.addActionEventHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onFirePriorityAs(null);
            }
        });
        WithoutPriorityElement.setCallBackForSelectCustom(this::onFireWithoutPriority);
        WithoutPriorityElement.addActionEventHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onFireWithoutPriority(null);
            }
        });
        ButtonMarkUp.disableProperty().bind(Bindings.createBooleanBinding(
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return checkButtonMarkUpIsDisable();
                    }
                },
                ParamPok1CheckBoxElement.selectedProperty(),
                ParamPok2CheckBoxElement.selectedProperty(),
                ParamPok3CheckBoxElement.selectedProperty(),
                ParamPok4CheckBoxElement.selectedProperty(),
                ParamPok5CheckBoxElement.selectedProperty(),
                ParamPok6CheckBoxElement.selectedProperty(),
                ParamPok7CheckBoxElement.selectedProperty(),
                ParamPok8CheckBoxElement.selectedProperty(),
                ParamPok9CheckBoxElement.selectedProperty(),
                ParamPok10CheckBoxElement.selectedProperty(),
                HourReportCheckBoxElement.selectedProperty(),
                OffsetDateCheckBoxElement.selectedProperty(),
                groupPokProperty,
                PokElement.valueProperty(),
                PokValueElement.valueProperty(),
                PriorityAsAndCompElement.selectedProperty(),
                AsSourceInformationForPriorityAsAndCompElement.valueProperty(),
                CompAsToPokValueForPriorityAsAndCompElement.valueProperty(),
                PriorityAsElement.selectedProperty(),
                AsSourceInformationForPriorityAsElement.valueProperty(),
                WithoutPriorityElement.selectedProperty(),
                ParamPok1Element.disableProperty(),
                ParamPok1Element.valueProperty(),
                ParamPok2Element.disableProperty(),
                ParamPok2Element.valueProperty(),
                ParamPok3Element.disableProperty(),
                ParamPok3Element.valueProperty(),
                ParamPok4Element.disableProperty(),
                ParamPok4Element.valueProperty(),
                ParamPok5Element.disableProperty(),
                ParamPok5Element.valueProperty(),
                ParamPok6Element.disableProperty(),
                ParamPok6Element.valueProperty(),
                ParamPok7Element.disableProperty(),
                ParamPok7Element.valueProperty(),
                ParamPok8Element.disableProperty(),
                ParamPok8Element.valueProperty(),
                ParamPok9Element.disableProperty(),
                ParamPok9Element.valueProperty(),
                ParamPok10Element.disableProperty(),
                ParamPok10Element.valueProperty(),
                HourReportElement.valueProperty()
        ));
        ButtonSetFilters.disableProperty().bind(Bindings.createBooleanBinding(
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return checkButtonSetFiltersIsDisable();
                    }
                },
                generalParamsService.getQuantityUpdateSelectedElementsProperty()
        ));
        ButtonCancelMarkUp.disableProperty().bind(Bindings.createBooleanBinding(
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return checkButtonCancelMarkUpIsDisable();
                    }
                },
                generalParamsService.getQuantityUpdateSelectedElementsProperty()
        ));
    }

    public boolean checkButtonSetFiltersIsDisable() {
        return generalParamsService.getListSelectedCellFormMarkInfo().size() == 0;
    }

    private void setElementsValue(NumericalPokValue numericalPokValueToSetFilters) {
        //???????????????? ?????? ??????????????
        deactivateAllFilters();
        //?????????????????????????? ?????? ?????????????? ???? numericalPokValue
        String stringForAlert = "";
        try {
            setGroupPokProperty(numericalPokValueToSetFilters);
        } catch (SetFiltersException e) {
            stringForAlert += e.getMessage();
        }
        try {
            setListOffsetDateAndSelectFirstElement(numericalPokValueToSetFilters);
        } catch (SetFiltersException e) {
            stringForAlert += stringForAlert.length() > 0 ? "\n" + e.getMessage() : e.getMessage();
        }
        try {
            setListHourReportAndSelectFirstElement(numericalPokValueToSetFilters);
        } catch (SetFiltersException e) {
            stringForAlert += stringForAlert.length() > 0 ? "\n" + e.getMessage() : e.getMessage();
        }
        if (stringForAlert.length() > 0) {
            createAlertWarning(this.stage, stringForAlert);
            throw new SetFiltersException(stringForAlert);
        }
    }

    public void PressedButtonSetFilters(NumericalPokValue numericalPokValueToSetFilters) {
        try {
            setElementsValue(numericalPokValueToSetFilters);
        } catch (Exception e) {
            return;
        }
        //createAlertInfo(this.stage, "?????????????? ?????????????? ????????????");
        createSuccessNotification("?????????????? ?????????????? ????????????");
    }
}
