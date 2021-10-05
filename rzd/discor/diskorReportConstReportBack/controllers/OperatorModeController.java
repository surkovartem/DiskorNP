package ru.rzd.discor.diskorReportConstReportBack.controllers;

import com.sun.star.container.XNamed;
import com.sun.star.sheet.XCellAddressable;
import com.sun.star.sheet.XCellRangeAddressable;
import com.sun.star.sheet.XSheetAnnotations;
import com.sun.star.sheet.XSheetAnnotationsSupplier;
import com.sun.star.table.CellAddress;
import com.sun.star.table.CellRangeAddress;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import ru.rzd.discor.diskorReportConstReportBack.Main;
import ru.rzd.discor.diskorReportConstReportBack.SetFiltersException;
import ru.rzd.discor.diskorReportConstReportBack.customElements.SimpleObjectPropertyWithChangeListenersArray;
import ru.rzd.discor.diskorReportConstReportBack.models.ImportExport.ExportFormClass;
import ru.rzd.discor.diskorReportConstReportBack.models.ImportExport.FullFormMarkInfo;
import ru.rzd.discor.diskorReportConstReportBack.models.ImportExport.ImportFormClass;
import ru.rzd.discor.diskorReportConstReportBack.models.currentProject.cellFormMarkInfo.CellFormMarkInfo;
import ru.rzd.discor.diskorReportConstReportBack.models.currentProject.chooseFormController.ChooseFormControllerObserverOutput;
import ru.rzd.discor.diskorReportConstReportBack.models.currentProject.chooseObjGroupController.ChooseObjGroupControllerObserverOutput;
import ru.rzd.discor.diskorReportConstReportBack.models.currentProject.mode.ProjectMode;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.CellInfoForNumericalPokValue;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.NumericalPokValue;
import ru.rzd.discor.diskorReportConstReportBack.models.reportRepo.Form;
import ru.rzd.discor.diskorReportConstReportBack.models.reportRepo.ObjGroup;
import ru.rzd.discor.diskorReportConstReportBack.services.GeneralParamsService;
import rx.Observer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.rzd.discor.diskorReportConstReportBack.ConvertColumnNameFunctions.convertColToNumString;
import static ru.rzd.discor.diskorReportConstReportBack.ConvertColumnNameFunctions.convertNumToColString;
import static ru.rzd.discor.diskorReportConstReportBack.Main.*;

public class OperatorModeController extends AnchorPane {

    private final List<NumericalPokValue> numericalPokValueToCutOut = new ArrayList<>();
    private final GeneralParamsService generalParamsService = Main.generalParamsService;
    public String CurrentFileActiveSheet;
    @FXML
    Button ButtonInputAndSaveNewAuthorizationToken;
    public SimpleObjectPropertyWithChangeListenersArray<ObjGroup> objGroup =
            new SimpleObjectPropertyWithChangeListenersArray<ObjGroup>();
    public SimpleObjectPropertyWithChangeListenersArray<Form> form =
            new SimpleObjectPropertyWithChangeListenersArray<Form>();
    @FXML
    Label TextStatusPublish;
    @FXML
    MenuItem MenuCreate, MenuOpen, MenuSave, MenuSaveAs, MenuClearList, MenuFormInfo, MenuUnpublish, MenuPublish,
            MenuExportForm, MenuImportForm, MenuCutOut, MenuCopy, MenuPaste, MenuChangeMode, MenuLogIn, MenuLogOut, MenuDelete;
    @FXML
    private FormMarkController formMarkController;

    Stage stage;

    public OperatorModeController() throws Exception {
        setFullFormInfo(null, null, null);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/OperatorMode.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //Добавление иконок
    public void SetGraphicAndSizeToIcon(MenuItem menuItem, ImageView imageView, Integer width, Integer height){
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        menuItem.setGraphic(imageView);
    }
    public void SetIconsToMenuBar() throws Exception{
        SetGraphicAndSizeToIcon(MenuCreate, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/Create.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuOpen, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/Open.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuSave, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/Save.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuSaveAs, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/SaveAs.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuDelete, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/Delete.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuExportForm, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/Export.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuImportForm, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/Import.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuCutOut, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/CutOut.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuCopy, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/Copy.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuPaste, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/Paste.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuClearList, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/ClearList.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuFormInfo, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/InfoList.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuPublish, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/Publish.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuUnpublish, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/UnPublish.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuChangeMode, new ImageView(new Image(getClass().getResourceAsStream("/assets/icon_32.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuLogIn, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/LogIn.png"))), 16, 16);
        SetGraphicAndSizeToIcon(MenuLogOut, new ImageView(new Image(getClass().getResourceAsStream("/assets/MenuBarIcons/LogOut.png"))), 16, 16);
    }
    //------------------------------------------------------------------------------------------------------------------

    @FXML
    void initialize() throws Exception {
        //Задание дефолтных значений времени появляния и исчезновения tooltip по всему проекту
        Tooltip tooltip = new Tooltip();
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);
            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(250)));
            fieldTimer = objBehavior.getClass().getDeclaredField("hideTimer");
            fieldTimer.setAccessible(true);
            objTimer = (Timeline) fieldTimer.get(objBehavior);
            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(30000)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SetIconsToMenuBar();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setInitialParams() {
        //Блокировка кнопки обновления, публикации и снятия с публикации формы, так как изначально форма не выбрана
        this.MenuDelete.setDisable(true);
        this.MenuSave.setDisable(true);
        this.MenuFormInfo.setDisable(true);
        this.MenuPublish.setDisable(true);
        this.MenuUnpublish.setDisable(true);
        this.formMarkController.setStage(this.stage);
    }

    public void SetCurrentFileActiveSheet(){
        XNamed xNamed = UnoRuntime.queryInterface(XNamed.class, generalParamsService.getActiveSheet());
        CurrentFileActiveSheet = xNamed.getName();
    }

    //------------------------------------------------------------------------------------------------------------------
    //Создать новый документ
    @FXML
    void ChooseForm() {
        ObjGroup oldObjGroup = objGroup.getValue();
        Form oldForm = form.getValue();
        ChooseFormController chooseFormController;
        try {
            chooseFormController = new ChooseFormController();
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "Ошибка при открытии модального окна по выбору формы\n" + e.getMessage());
            return;
        }
        Stage stage = new Stage();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //Действия при закрытии модального окна
            }
        });
        stage.setScene(new Scene(chooseFormController));
        stage.setTitle("Выбор формы");
        stage.getIcons().add(new javafx.scene.image.Image(OperatorModeController.class.getResourceAsStream(
                "/assets/icon_32.png")));

//        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        //Установка связи между компонентами
        chooseFormController.setStage(stage);
        chooseFormController.getChooseFormControllerEventEmitter().subscribe(
                new Observer<ChooseFormControllerObserverOutput>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        createAlertWarning(stage, e.getMessage());
                    }

                    @Override
                    public void onNext(ChooseFormControllerObserverOutput chooseFormControllerOutput) {
                        onChooseForm(chooseFormControllerOutput);
                    }
                }
        );
        chooseFormController.setInitialParams(oldObjGroup, oldForm, ProjectMode.operator);
        stage.showAndWait();
        SetCurrentFileActiveSheet();
    }

    @FXML
    void openFormInfoWindow() {
        FormInfoController formInfoController;
        try {
            formInfoController = new FormInfoController();
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "Ошибка при открытии окна по просмотру информации о форме\n" + e.getMessage());
            return;
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(formInfoController));
        stage.setTitle("Информация о форме");
        stage.getIcons().add(new javafx.scene.image.Image(ConstructorModeController.class.getResourceAsStream(
                "/assets/icon_32.png")));
        stage.setAlwaysOnTop(true);
        //Установка связи между компонентами
        formInfoController.setStage(stage);
        try {
            formInfoController.setInitialParams(form.getValue());
        } catch (Exception e) {
            createAlertWarning(stage, "Ошибка при отображении информации о форме\n" + e.getMessage());
        }
        stage.showAndWait();
    }

    void onChooseForm(ChooseFormControllerObserverOutput chooseFormControllerOutput) {
        Form formWithListNumericalPokValue;
        try {
            formWithListNumericalPokValue = reportRepoService.getFormById(chooseFormControllerOutput.getForm().getId());
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(stage, "Ошибка при получении разметки для выбранной формы\n" + e.getMessage());
            return;
        }
        byte[] byteArray;
        try {
            byteArray = reportRepoService.getFileWithFormMarks(chooseFormControllerOutput.getForm().getId());
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(stage, "Ошибка при получении файла для выбранной формы\n" + e.getMessage());
            return;
        }
        FullFormMarkInfo fullFormMarkInfo = new FullFormMarkInfo();
        fullFormMarkInfo.setListNumericalPokValue(formWithListNumericalPokValue.getListNumericalPokValue());
        fullFormMarkInfo.setFileByteArray(byteArray);
        try {
            setFullFormInfo(fullFormMarkInfo, formWithListNumericalPokValue, chooseFormControllerOutput.getObjGroup());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updateFormDisplayedInfo() {
        if (this.form.getValue() == null) {
            this.stage.setTitle("Конструктор отчетов. Режим оператора");
            this.MenuSave.setDisable(true);
            this.MenuFormInfo.setDisable(true);
            this.MenuDelete.setDisable(true);
        } else {
            //Установка нового заголовка окна с именем выбранной формы и группы
            this.stage.setTitle("Конструктор отчетов. Режим оператора (Форма \"" + this.form.getValue().getName() + "\", группа \"" + this.objGroup.getValue().getName() + "\")");
            //Разблокировка кнопки обновления формы
            this.MenuSave.setDisable(false);
            this.MenuFormInfo.setDisable(false);
            this.MenuDelete.setDisable(false);
        }
        isActive();
    }

    //Кнопка "Создать новую форму"
    @FXML
    void createForm() {
        try {
            setFullFormInfo(null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "Ошибка при создании формы\n" + e.getMessage());
        }
        SetCurrentFileActiveSheet();
    }

    @FXML
    void DeleteForm(){
        if (form.getValue() == null) {
            createInfoNotification("Выберите форму");
            return;
        }
        Optional<ButtonType> option  = Main.createDialogAlert(stage, "Удаление формы", "Вы действительно хотите удалить эту форму?");
        if (option.get() == null) {
            //Диалоговое окно закрыто, без подтверждения
            return;
        }
        if (option.get() == ButtonType.OK) {
            //удаление формы
            onChooseFormForDeleteForm(form.getValue());
            createForm();
        }
        if (option.get() == ButtonType.CANCEL) {
            //отмена удаления
            return;
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //Кнопка "Добавить новую форму"
    @FXML
    void AddForm() {
        ObjGroup oldObjGroup = objGroup.getValue();
        ChooseObjGroupController chooseObjGroupController;
        try {
            chooseObjGroupController = new ChooseObjGroupController();
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "Ошибка при открытии модального окна по выбору группы форм\n" + e.getMessage());
            return;
        }
        Stage stage = new Stage();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //Действия при закрытии модального окна
            }
        });
        stage.setScene(new Scene(chooseObjGroupController));
        stage.setTitle("Выбор группы форм");
        stage.getIcons().add(new javafx.scene.image.Image(OperatorModeController.class.getResourceAsStream(
                "/assets/icon_32.png")));
//        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        //Установка связи между компонентами
        chooseObjGroupController.setStage(stage);
        chooseObjGroupController.getChooseObjGroupControllerEventEmitter().subscribe(
                new Observer<ChooseObjGroupControllerObserverOutput>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ChooseObjGroupControllerObserverOutput chooseObjGroupControllerObserverOutput) {
                        onChooseFormForAddForm(chooseObjGroupControllerObserverOutput);
                    }
                });
        chooseObjGroupController.setInitialParams(oldObjGroup, ProjectMode.operator);
        stage.showAndWait();
    }

    @FXML
    void UpdateForm() {
        XNamed xNamed = UnoRuntime.queryInterface(XNamed.class, generalParamsService.getActiveSheet());
        if(!CurrentFileActiveSheet.equals(xNamed.getName())){
            Optional<ButtonType> option  = Main.createDialogAlert(stage, "Изменение имени активного листа", "Сохранить отчет с новым именим активного листа - " + xNamed.getName() + "?");
            if (option.get() == null) { return; }
            if (option.get() == ButtonType.OK) {
                if (objGroup.getValue() == null) {
                    createInfoNotification("Выберите группу форм");
                    return;
                }
                if (form.getValue() == null) {
                    //createAlertInfo(this.stage, "Выберите форму");
                    createInfoNotification("Выберите форму");
                    return;
                }
                ChooseFormControllerObserverOutput chooseFormControllerObserverOutput = new ChooseFormControllerObserverOutput();
                chooseFormControllerObserverOutput.setObjGroup(objGroup.getValue());
                chooseFormControllerObserverOutput.setForm(form.getValue());
                onChooseFormForUpdateForm(chooseFormControllerObserverOutput);
            }
            if (option.get() == ButtonType.CANCEL) { return; }
        }
        else {
            if (objGroup.getValue() == null) {
                //createAlertInfo(this.stage, "Выберите группу форм");
                createInfoNotification("Выберите группу форм");
                return;
            }
            if (form.getValue() == null) {
                //createAlertInfo(this.stage, "Выберите форму");
                createInfoNotification("Выберите форму");
                return;
            }
            ChooseFormControllerObserverOutput chooseFormControllerObserverOutput = new ChooseFormControllerObserverOutput();
            chooseFormControllerObserverOutput.setObjGroup(objGroup.getValue());
            chooseFormControllerObserverOutput.setForm(form.getValue());
            onChooseFormForUpdateForm(chooseFormControllerObserverOutput);
        }
    }

    //Кнопка "Очистить лист"
    @FXML
    void clearList() {
        try {
            setFullFormInfo(null, form.getValue(), objGroup.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "Ошибка при создании формы\n" + e.getMessage());
        }
    }

    @FXML
    void SetActive() {
        if (form.getValue() == null) {
            //createAlertInfo(this.stage, "Выберите форму");
            createInfoNotification("Выберите форму");
            return;
        }
        onChooseFormForSetActive(form.getValue());
        isActive();
    }

    @FXML
    void SetInActive() {
        if (form.getValue() == null) {
            //createAlertInfo(this.stage, "Выберите форму");
            createInfoNotification("Выберите форму");
            return;
        }
        onChooseFormForSetInActive(form.getValue());
        isActive();
    }

    @FXML
    private void PressedButtonMenuEnter() {
        try {
            Main.inputAndSaveTokenFromWindowAuthorization();
        } catch (Exception e) {
            createAlertWarning(this.stage, "Ошибка при обновлении токена\n" + e.getMessage());
            return;
        }
        //createAlertWarning(this.stage, "Токен был успешно обновлен");
        createSuccessNotification("Токен был успешно обновлен");
        try {
            formMarkController.setListOffsetDateAndSelectFirstElement(null);
        } catch (Exception e) {
            createAlertWarning(this.stage, e.getMessage());
        }
        try {
            formMarkController.setListHourReportAndSelectFirstElement(null);
        } catch (Exception e) {
            createAlertWarning(this.stage, e.getMessage());
        }
    }

    @FXML
    private void PressedButtonMenuExit() {
        try {
            Main.LogOut();
        } catch (Exception e) {
            createAlertWarning(this.stage, "Ошибка выхода из системы\n" + e.getMessage());
            return;
        }
        //createAlertWarning(this.stage, "Выход из системы произведен");
        createSuccessNotification("Выход из системы произведен");
        try {
            formMarkController.setListOffsetDateAndSelectFirstElement(null);
        } catch (Exception e) {
            createAlertWarning(this.stage, e.getMessage());
        }
        try {
            formMarkController.setListHourReportAndSelectFirstElement(null);
        } catch (Exception e) {
            createAlertWarning(this.stage, e.getMessage());
        }
    }

    @FXML
    private void PressedButtonMenuExportForm() throws Exception {
        ExportFormClass export = new ExportFormClass();
        try {
            export.CreateZipExport(getFullFormMarkInfo(), form.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "Ошибка при формировании архива отчета. " + e.getMessage());
            return;
        }
        //createAlertInfo(this.stage, "Архив с данными отчета сформирован");
        createSuccessNotification("Архив с данными отчета сформирован");
    }

    @FXML
    private void PressedButtonMenuImportForm() throws Exception {
        ImportFormClass Import = new ImportFormClass();
        try {
            setFullFormInfo(Import.ReadZipForm(), form.getValue(), objGroup.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "Ошибка при получении данных из архива. " + e.getMessage());
            return;
        }
        //createAlertInfo(this.stage, "Данные отчета были загружены из архива");
        createSuccessNotification("Данные отчета были загружены из архива");
    }

    public boolean checkButtonMarkUpIsDisable() {
        return generalParamsService.getListSelectedCellFormMarkInfo().size() == 0;
    }

    //Кнопка "Вырезать"
    @FXML
    private void PressedButtonCutOut() throws Exception {
        numericalPokValueToCutOut.clear();
        if (checkButtonMarkUpIsDisable()) {
            //createAlertWarning(this.stage, "Для операции 'Вырезать' необходимо выделить не меньше одной размеченной ячейки");
            createInfoNotification("Для операции 'Вырезать' необходимо выделить\nне меньше одной размеченной ячейки");
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
                        //Запись в буфер
                        numericalPokValueToCutOut.add(numericalPokValue);
                        generalParamsService.getListNumericalPokValue().remove(numericalPokValue);
                        k--;
                    }
                }

                //Обращение к ячейке в выбраном диапозоне
                XCell xCell = generalParamsService.getActiveSheet().getCellByPosition(i, j);
                //Получение адреса ячейки
                XCellAddressable xCellAddr = UnoRuntime.queryInterface(XCellAddressable.class, xCell);
                CellAddress aAddress = xCellAddr.getCellAddress();
                //Вставка аннотации
                XSheetAnnotationsSupplier xAnnotationsSupp = UnoRuntime.queryInterface(XSheetAnnotationsSupplier.class, generalParamsService.getActiveSheet());
                XSheetAnnotations xAnnotations = xAnnotationsSupp.getAnnotations();

                String annotation = "";
                xAnnotations.insertNew(aAddress, annotation);
            }
        }
        //Задание информации о ячейке в модальном окне
        generalParamsService.updateSelectedElements();
        //createAlertInfo(this.stage, "Операция 'Вырезать' выполнена");
        createSuccessNotification("Операция 'Вырезать' выполнена");
    }

    //Кнопка "Копировать"
    @FXML
    private void PressedButtonCopy() throws Exception {
        numericalPokValueToCutOut.clear();
        if (checkButtonMarkUpIsDisable()) {
            //createAlertWarning(this.stage, "Для операции 'Копировать' необходимо выделить не меньше одной размеченной ячейки");
            createInfoNotification("Для операции 'Копировать' необходимо выделить\nне меньше одной размеченной ячейки");
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
                        numericalPokValueToCutOut.add(numericalPokValue);
                    }
                }
            }
        }
        //Задание информации о ячейке в модальном окне
        generalParamsService.updateSelectedElements();
        //createAlertInfo(this.stage, "Операция 'Копировать' выполнена");
        createSuccessNotification("Операция 'Копировать' выполнена");
    }

    //Кнопка "Вставить"
    @FXML
    private void PressedButtonPaste() throws Exception {
        try {
            past();
        } catch (Exception e) {
            e.printStackTrace();
            createAlertInfo(this.stage, e.getMessage());
        }
    }

    private void past() {
        List<NumericalPokValue> localNumericalPokValueToCutOut = new ArrayList<>();


        XNamed xNamed = UnoRuntime.queryInterface(XNamed.class, generalParamsService.getActiveSheet());
        XCellRangeAddressable xRangeAddr = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeAddressable.class, generalParamsService.getActiveCells());
        CellRangeAddress aRangeAddress = xRangeAddr.getRangeAddress();

        int rowCellSelect = aRangeAddress.StartRow, colCellSelect = aRangeAddress.StartColumn;
        int rowCellFromMemory, colCellFromMemory;

        //Замена cellInfo у объектов numericalPokValue
        int maxRow = numericalPokValueToCutOut.stream().map(v -> v.getCellInfo().getRowNumber()).max(Integer::compare).get();
        int minRow = numericalPokValueToCutOut.stream().map(v -> v.getCellInfo().getRowNumber()).min(Integer::compare).get();

        int maxCol = numericalPokValueToCutOut.stream().map(v -> convertColToNumString(v.getCellInfo().getColumnNumber())).max(Integer::compare).get();
        int minCol = numericalPokValueToCutOut.stream().map(v -> convertColToNumString(v.getCellInfo().getColumnNumber())).min(Integer::compare).get();
        for (int r = minRow; r <= maxRow; r++) {
            for (int c = minCol; c <= maxCol; c++) {
                //row & col сохраненных в паями ячеек ("Вырезать")
                int finalR = r;
                int finalC = c;
                Optional<NumericalPokValue> currentNumericalPokValue = numericalPokValueToCutOut.stream().filter(v -> v.getCellInfo().getRowNumber() == finalR &&
                        convertColToNumString(v.getCellInfo().getColumnNumber()) == finalC).findFirst();
                if (currentNumericalPokValue.isPresent()) {
                    NumericalPokValue localNumericalPokValue;
                    try {
                        localNumericalPokValue = objectMapper.readValue(objectMapper.writeValueAsString(currentNumericalPokValue.get()), NumericalPokValue.class);
                    } catch (IOException e) {
                        throw new RuntimeException("Ошибка при копировании объекта localNumericalPokValue");
                    }
                    localNumericalPokValue.setCellInfo(new CellInfoForNumericalPokValue(currentNumericalPokValue.get().getCellInfo().getListName(), currentNumericalPokValue.get().getCellInfo().getRowNumber(), currentNumericalPokValue.get().getCellInfo().getColumnNumber()));

                    localNumericalPokValue.getCellInfo().setListName(xNamed.getName());
                    rowCellFromMemory = localNumericalPokValue.getCellInfo().getRowNumber();
                    colCellFromMemory = convertColToNumString(localNumericalPokValue.getCellInfo().getColumnNumber());
                    //Работа с row
                    if (rowCellSelect < rowCellFromMemory) {
                        localNumericalPokValue.getCellInfo().setRowNumber(rowCellFromMemory - Math.abs(rowCellSelect - localNumericalPokValue.getCellInfo().getRowNumber()) + r - minRow + 1);
                    }
                    if (rowCellSelect > rowCellFromMemory) {
                        localNumericalPokValue.getCellInfo().setRowNumber(rowCellFromMemory + Math.abs(rowCellSelect - localNumericalPokValue.getCellInfo().getRowNumber()) + r - minRow + 1);
                    }
                    if (rowCellSelect == rowCellFromMemory) {
                        localNumericalPokValue.getCellInfo().setRowNumber(rowCellFromMemory + r - minRow + 1);
                    }
                    //Работа с column
                    if (colCellSelect < colCellFromMemory) {
                        localNumericalPokValue.getCellInfo().setColumnNumber(convertNumToColString(colCellFromMemory - Math.abs(colCellFromMemory - colCellSelect) + c - minCol));
                    }
                    if (colCellSelect > colCellFromMemory) {
                        localNumericalPokValue.getCellInfo().setColumnNumber(convertNumToColString(colCellFromMemory + Math.abs(colCellFromMemory - colCellSelect) + c - minCol));
                    }
                    if (colCellSelect == colCellFromMemory) {
                        localNumericalPokValue.getCellInfo().setColumnNumber(convertNumToColString(colCellFromMemory + c - minCol));
                    }
                    Optional<NumericalPokValue> existNumericalPokValue = generalParamsService.getListNumericalPokValue().stream().filter(v ->
                            v.getCellInfo().getRowNumber().equals(localNumericalPokValue.getCellInfo().getRowNumber()) &&
                                    v.getCellInfo().getColumnNumber().equals(localNumericalPokValue.getCellInfo().getColumnNumber())).findFirst();
                    if (existNumericalPokValue.isPresent()) {
                        throw new RuntimeException("Вставляемая область пересекается с текщей разметкой листа. Вставка не будет выполнена, выберите другую начальную ячейку или очстити область листа");
                    }
                    localNumericalPokValueToCutOut.add(localNumericalPokValue);
                }
            }
        }
        localNumericalPokValueToCutOut.forEach(currentNumericalPokValue -> {
            generalParamsService.getListNumericalPokValue().add(currentNumericalPokValue);
            CellFormMarkInfo newCellFormMarkInfo = new CellFormMarkInfo();
            newCellFormMarkInfo.setNumericalPokValue(currentNumericalPokValue);
            newCellFormMarkInfo.setIterationGroupDto(null);
            try {
                generalParamsService.setCellAnnotation(generalParamsService.getActiveSheet(), newCellFormMarkInfo);
            } catch (Exception e) {
                System.out.println("Ошибка при добавлении аннотации для ячейки " + currentNumericalPokValue.getCellInfo());
            }
        });
        System.out.println(xNamed.getName() + " " + convertNumToColString(colCellSelect) + " " + convertNumToColString(colCellSelect + maxCol - minCol) + " " + rowCellSelect + 1 + " " + (rowCellSelect + maxRow - minRow + 1));
        generalParamsService.updateSelectedElements(xNamed.getName(), colCellSelect, colCellSelect + maxCol - minCol, rowCellSelect + 1, rowCellSelect + maxRow - minRow + 1);
        numericalPokValueToCutOut.clear();
    }

    @FXML
    private void PressedChangeMode() {
        try {
            Main.openConstructorMode(this.stage);
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, e.getMessage());
        }
    }

    void isActive() {
        if (form.getValue() == null) {
            TextStatusPublish.setText("");
            this.MenuPublish.setDisable(true);
            this.MenuUnpublish.setDisable(true);
            return;
        }
        if (form.getValue().getActiveTip()) {
            TextStatusPublish.setText("Форма в публикации");
            this.MenuPublish.setDisable(true);
            this.MenuUnpublish.setDisable(false);
        } else {
            TextStatusPublish.setText("Форма не опубликована");
            this.MenuPublish.setDisable(false);
            this.MenuUnpublish.setDisable(true);
        }
    }

    public void onChooseFormForDeleteForm(Form form) {
        try {
            Main.reportRepoService.deleteForm(form);
        } catch (Exception e) {
            createAlertWarning(this.stage, "Ошибка при удалении формы\n" + e.getMessage());
            return;
        }
        //this.form.getValue().setActiveTip(true);
        //createAlertInfo(this.stage, "Форма опубликована");
        createSuccessNotification("Форма удалена");
    }

    public void onChooseFormForSetActive(Form form) {
        try {
            Main.reportRepoService.setActive(form);
        } catch (Exception e) {
            createAlertWarning(this.stage, "Ошибка при публикации\n" + e.getMessage());
            return;
        }
        this.form.getValue().setActiveTip(true);
        //createAlertInfo(this.stage, "Форма опубликована");
        createSuccessNotification("Форма опубликована");
    }

    public void onChooseFormForSetInActive(Form form) {
        try {
            Main.reportRepoService.setInActive(form);
        } catch (Exception e) {
            createAlertWarning(this.stage, "Ошибка при снятии публикации\n" + e.getMessage());
            return;
        }
        this.form.getValue().setActiveTip(false);
        //createAlertInfo(this.stage, "Форма снята с публикации");
        createSuccessNotification("Форма снята с публикации");
    }

    public void onChooseFormForUpdateForm(ChooseFormControllerObserverOutput chooseObjGroupControllerObserverOutput) {
        String nameFile;
        try {
            nameFile = generalParamsService.saveDocumentWithNewName(null, this.stage);
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "Ошибка при сохранении документа для сохранения формы\n" + e.getMessage());
            return;
        }
        Form form;
        try {
            form = Main.reportRepoService.updateForm(generalParamsService.getDocumentByName(nameFile), chooseObjGroupControllerObserverOutput.getForm(), generalParamsService.getListNumericalPokValue());
        } catch (Exception e) {
            createAlertWarning(this.stage, "Ошибка при размещении формы в репозитории\n" + e.getMessage());
            return;
        }
        this.objGroup.setValueCustom(chooseObjGroupControllerObserverOutput.getObjGroup());
        this.form.setValueCustom(form);
        //createAlertInfo(this.stage, "Измененная форма успешно размещена в репозитории");
        createSuccessNotification("Измененная форма успешно размещена в репозитории");
    }

    public void onChooseFormForAddForm(ChooseObjGroupControllerObserverOutput chooseObjGroupControllerObserverOutput) {
        String nameFile;
        try {
            nameFile = generalParamsService.saveDocumentWithNewName(null, stage);
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "Ошибка при сохранении документа для сохранения формы\n" + e.getMessage());
            return;
        }
        Form form;
        try {
            form = createNewFormObject(chooseObjGroupControllerObserverOutput);
        } catch (Exception e) {
            e.printStackTrace();
            createAlertWarning(this.stage, "Ошибка при формировании объекта формы\n" + e.getMessage());
            return;
        }
        try {
            form = Main.reportRepoService.addForm(generalParamsService.getDocumentByName(nameFile), form, generalParamsService.getListNumericalPokValue());
        } catch (Exception e) {
            createAlertWarning(this.stage, "Ошибка при размещении формы в репозитории\n" + e.getMessage());
            return;
        }
        this.objGroup.setValueCustom(chooseObjGroupControllerObserverOutput.getObjGroup());
        this.form.setValueCustom(form);
        updateFormDisplayedInfo();
        //createAlertInfo(this.stage, "Форма успешно размещена в репозитории");
        createSuccessNotification("Форма успешно размещена в репозитории");
    }

    //------------------------------------------------------------------------------------------------------------------
    //Создание формы
    Form createNewFormObject(ChooseObjGroupControllerObserverOutput chooseObjGroupControllerObserverOutput) {
        Form createdForm = new Form();
        createdForm.setDescription("Описание");
        createdForm.setFkObjGroup(chooseObjGroupControllerObserverOutput.getObjGroup().getId());
        createdForm.setListNumericalPokValue(null);
        createdForm.setName(chooseObjGroupControllerObserverOutput.getFormName());
        if (Main.user == null) {
            throw new SetFiltersException("Ошибка при получении объекта пользователя");
        }
        createdForm.setIdDor(Main.user.getDorCode());
        createdForm.setPrivilege(0);
        createdForm.setTypeForm("otchetnaya");
        return createdForm;
    }

    private void setFullFormInfo(FullFormMarkInfo fullFormMarkInfo, Form currentForm, ObjGroup currentObjGroup) throws Exception {
        String nameFile = null;
        if (fullFormMarkInfo != null) {
            try {
                nameFile = generalParamsService.saveDocumentWithNewName(fullFormMarkInfo.getFileByteArray(), stage);
            } catch (Exception e) {
                e.printStackTrace();
                //createAlertWarning(stage, "Ошибка при сохранении файла ods");
                createErrorNotification("Ошибка при сохранении файла ods");
                throw e;
            }
            generalParamsService.openExistingLibreOfficeFile(nameFile, this.stage);
        } else {
            generalParamsService.openNewLibreOfficeFile(this.stage);
        }
        objGroup.setValueCustom(currentObjGroup);
        form.setValueCustom(currentForm);
        generalParamsService.getListNumericalPokValue().clear();
        if (fullFormMarkInfo != null) {
            generalParamsService.getListNumericalPokValue().addAll(fullFormMarkInfo.getListNumericalPokValue());
        }
        generalParamsService.updateSelectedElements();
        if (this.stage != null) {
            updateFormDisplayedInfo();
            //createAlertInfo(this.stage, "Выбранная форма была успешно открыта");
            createSuccessNotification("Выбранная форма была успешно открыта");
        }
    }

    private FullFormMarkInfo getFullFormMarkInfo() throws Exception {
        String nameFile;
        try {
            nameFile = generalParamsService.saveDocumentWithNewName(null, stage);
        } catch (Exception e) {
            throw new Exception("Ошибка при сохранении документа для сохранения формы\n" + e.getMessage(), e);
        }
        FullFormMarkInfo fullFormMarkInfo = new FullFormMarkInfo();
        fullFormMarkInfo.setFileByteArray(Files.readAllBytes(generalParamsService.getDocumentByName(nameFile).toPath()));
        fullFormMarkInfo.setListNumericalPokValue(generalParamsService.getListNumericalPokValue());
        return fullFormMarkInfo;
    }
}
