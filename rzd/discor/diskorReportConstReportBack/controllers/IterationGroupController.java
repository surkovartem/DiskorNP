package ru.rzd.discor.diskorReportConstReportBack.controllers;

import com.sun.star.container.XNamed;
import com.sun.star.table.CellRangeAddress;
import com.sun.star.uno.UnoRuntime;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.rzd.discor.diskorReportConstReportBack.Main;
import ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.dto.IterationGroupDto;
import ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.dto.IterationGroupHandbookDto;
import ru.rzd.discor.diskorReportConstReportBack.services.GeneralParamsService;
import rx.Observer;

import java.io.IOException;

import static ru.rzd.discor.diskorReportConstReportBack.ConvertColumnNameFunctions.convertNumToColString;
import static ru.rzd.discor.diskorReportConstReportBack.Main.*;

public class IterationGroupController extends AnchorPane {
    Stage stage;
    private final GeneralParamsService generalParamsService = Main.generalParamsService;
    @FXML
    public TextField nameElement;
    @FXML
    public TextField listNameElement;
    @FXML
    public TextField cellAddressRangeElement;
    @FXML
    public TabPane listIterationGroupHandbookControllerParent;

    public IterationGroupController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/IterationGroup.fxml"));
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
    }

    public void setInitialParams(ConstructorModeController constructorModeController) {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void chooseListNameAndCellAddressRange() {
        //Установка наименования листа
        listNameElement.setText((UnoRuntime.queryInterface(XNamed.class, generalParamsService.getActiveSheet()).getName()));
        //Установка диапазона адресов ячеек
        CellRangeAddress cellRangeAddress = generalParamsService.getActiveCells().getRangeAddress();
        cellAddressRangeElement.setText(convertNumToColString(cellRangeAddress.StartColumn) + (cellRangeAddress.StartRow + 1)
                + ":" + convertNumToColString(cellRangeAddress.EndColumn) + (cellRangeAddress.EndRow + 1)
        );
    }

    @FXML
    void addIterationGroupHandbookController() {
        addIterationGroupHandbookController(createIterationGroupHandbookController());
    }

    @FXML
    public void pressButtonSetFilters() {
        if (checkButtonSetFiltersIsDisable()) {
            //createAlertWarning(this.stage, "Для задания фильтров необходимо выделить не меньше одной размеченной итерационной группы");
            createInfoNotification("Для задания фильтров необходимо выделить\nне меньше одной размеченной итерационной группы");
            return;
        }
        pressButtonSetFilters(generalParamsService.getListSelectedIterationGroupDto().get(0));
    }

    private IterationGroupHandbookController createIterationGroupHandbookController() {
        return createIterationGroupHandbookController(null);
    }

    private IterationGroupHandbookController createIterationGroupHandbookController(IterationGroupHandbookDto iterationGroupHandbookDto) {
        IterationGroupHandbookController iterationGroupHandbookController = new IterationGroupHandbookController();
        iterationGroupHandbookController.setInitialParams(iterationGroupHandbookDto);
        iterationGroupHandbookController.setStage(this.stage);
        return iterationGroupHandbookController;
    }

    public void addIterationGroupHandbookController(IterationGroupHandbookController iterationGroupHandbookController) {
        addIterationGroupHandbookController(this.listIterationGroupHandbookControllerParent.getTabs().size(), iterationGroupHandbookController);
    }

    public void addIterationGroupHandbookController(int index, IterationGroupHandbookController iterationGroupHandbookController) {
        iterationGroupHandbookController.subscribeRemoveIterationGroupHandbookControllerEventEmitter(new Observer<IterationGroupHandbookController>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(IterationGroupHandbookController iterationGroupHandbookController) {
                removeIterationGroupHandbookController(iterationGroupHandbookController);
            }
        });
        this.listIterationGroupHandbookControllerParent.getTabs().add(index, iterationGroupHandbookController);
    }

    public void removeIterationGroupHandbookController(IterationGroupHandbookController iterationGroupHandbookController) {
        this.listIterationGroupHandbookControllerParent.getTabs().remove(iterationGroupHandbookController);
        iterationGroupHandbookController.destroy();
    }

    private void setElementsStyle() {

    }

    private void setElementsAction() {

    }

    private void setElementsValue(IterationGroupDto iterationGroupDtoToSetFilters) {
        setNameElement(iterationGroupDtoToSetFilters);
        setListNameElement(iterationGroupDtoToSetFilters);
        setCellAddressRangeElement(iterationGroupDtoToSetFilters);
        setListIterationGroupHandbookControllerParent(iterationGroupDtoToSetFilters);
    }

    private void setNameElement(IterationGroupDto iterationGroupDtoToSetFilters) {
        if (iterationGroupDtoToSetFilters == null) {
            nameElement.setText("");
            return;
        }
        nameElement.setText(iterationGroupDtoToSetFilters.getName());
    }

    private void setListNameElement(IterationGroupDto iterationGroupDtoToSetFilters) {
        if (iterationGroupDtoToSetFilters == null) {
            listNameElement.setText("");
            return;
        }
        listNameElement.setText(iterationGroupDtoToSetFilters.getListName());
    }

    private void setCellAddressRangeElement(IterationGroupDto iterationGroupDtoToSetFilters) {
        if (iterationGroupDtoToSetFilters == null) {
            listNameElement.setText("");
            return;
        }
        listNameElement.setText(iterationGroupDtoToSetFilters.getCellAddressRange());
    }

    private void setListIterationGroupHandbookControllerParent(IterationGroupDto iterationGroupDtoToSetFilters) {
        for (int i = 0; i < listIterationGroupHandbookControllerParent.getTabs().size(); i++) {
            removeIterationGroupHandbookController((IterationGroupHandbookController) listIterationGroupHandbookControllerParent.getTabs().get(i));
            i--;
        }
        if (iterationGroupDtoToSetFilters == null) {
            return;
        }
        for (IterationGroupHandbookDto iterationGroupHandbookDto : iterationGroupDtoToSetFilters.getListIterationGroupHandbook()) {
            addIterationGroupHandbookController(createIterationGroupHandbookController(iterationGroupHandbookDto));
        }
    }

    public boolean checkButtonSetFiltersIsDisable() {
        return generalParamsService.getListSelectedIterationGroupDto().size() == 0;
    }

    public void pressButtonSetFilters(IterationGroupDto iterationGroupDtoToSetFilters) {
        //Устанавливаем все фильтры
        setElementsValue(iterationGroupDtoToSetFilters);
        //createAlertInfo(this.stage, "Фильтры успешно заданы");
        createSuccessNotification("Фильтры успешно заданы");
    }
}
