package ru.rzd.discor.diskorReportConstReportBack.services;

import com.sun.star.beans.PropertyValue;
import com.sun.star.container.XNamed;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.*;
import com.sun.star.table.CellAddress;
import com.sun.star.table.CellRangeAddress;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.view.XSelectionChangeListener;
import com.sun.star.view.XSelectionSupplier;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.rzd.discor.diskorReportConstReportBack.connector.BootstrapSocketConnector;
import ru.rzd.discor.diskorReportConstReportBack.models.currentProject.cell.CellCoordinate;
import ru.rzd.discor.diskorReportConstReportBack.models.currentProject.cell.CellRange;
import ru.rzd.discor.diskorReportConstReportBack.models.currentProject.cellFormMarkInfo.CellFormMarkInfo;
import ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.dto.IterationGroupDto;
import ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.dto.IterationGroupFormMarkDto;
import ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.dto.IterationGroupHandbookDto;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.AttributesForNumericalPokValue;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.NumericalPokValue;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.ParamPokInfoForNumericalPokValue;
import rx.Observer;
import rx.Subscription;
import rx.subjects.PublishSubject;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.*;

import static ru.rzd.discor.diskorReportConstReportBack.ConvertColumnNameFunctions.convertColToNumString;
import static ru.rzd.discor.diskorReportConstReportBack.ConvertColumnNameFunctions.convertNumToColString;
import static ru.rzd.discor.diskorReportConstReportBack.Main.createAlertWarning;
import static ru.rzd.discor.diskorReportConstReportBack.Main.folderUrlToSaveOdsFiles;

@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
public class GeneralParamsService {
    private Stage primaryStage;
    //------------------------------------------------------------------------------------------------------------------
    //Получение APi Open Office
    private XModel xSpreadsheetModel;
    private XSpreadsheetView xSpreadsheetView;
    private XMultiComponentFactory xRemoteServiceManager;
    private XComponentLoader xComponentLoader;
    private XComponent xSpreadsheetComponent;
    private XSpreadsheetDocument xSpreadsheetDocument;
    private XModel xDocModel;
    private XController xSpreadsheetController;
    private XSelectionSupplier xSel;
    private PropertyValue[] loadProps;
    private XSpreadsheet activeSheet;
    private XCellRangeAddressable activeCells;
    //------------------------------------------------------------------------------------------------------------------
    //Информация, которой обмениваемся с сервером
    private final List<NumericalPokValue> listNumericalPokValue = new ArrayList<>();
    private final List<IterationGroupDto> listIterationGroupDto = new ArrayList<>();
    //Выбранные элементы формы
    private final List<CellFormMarkInfo> listSelectedCellFormMarkInfo = new ArrayList<>();
    private final List<IterationGroupDto> listSelectedIterationGroupDto = new ArrayList<>();
    private final List<IterationGroupHandbookDto> listSelectedIterationGroupHandbookDto = new ArrayList<>();
    //Обновление списков выбранных элементов формы
    public final SimpleIntegerProperty quantityUpdateSelectedElementsProperty = new SimpleIntegerProperty();
    @Getter(AccessLevel.PRIVATE)
    private final PublishSubject<Void> updateSelectedElementsEventEmitter = PublishSubject.create();
    private final List<Subscription> listSubscriptionUpdateSelectedElementsEventEmitter = new ArrayList<>();

    public GeneralParamsService() {
        quantityUpdateSelectedElementsProperty.setValue(0);
        subscribeUpdateSelectedElementsEventEmitter(new Observer<Void>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Void unused) {
                Integer value = quantityUpdateSelectedElementsProperty.getValue();
                if (value == 10000) {
                    value = 0;
                } else {
                    value++;
                }
                quantityUpdateSelectedElementsProperty.setValue(value);
            }
        });
    }

    public void destroy() {
        for (Subscription subscription : listSubscriptionUpdateSelectedElementsEventEmitter) {
            subscription.unsubscribe();
        }
    }

    public void setInitialParams(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        //Инициализирующая загрузка
        String oooExeFolder;
        String oooExeFolder1 = "C:/Program Files (x86)/LibreOffice/program";
        String oooExeFolder2 = "C:/Program Files/LibreOffice/program";
        File folder1 = new File(oooExeFolder1);
        File folder2 = new File(oooExeFolder2);
        if (folder1.exists()) {
            oooExeFolder = oooExeFolder1;
        } else if (folder2.exists()) {
            oooExeFolder = oooExeFolder2;
        } else {
            throw new Exception("LibreOffice отсутсвует в предполагаемых директориях: \"C:/Program Files (x86)/LibreOffice/program\", \"C:/Program Files/LibreOffice/program\"");
        }
        //Класс BootstrapSocketConnector скопирован с некоторыми правками из библиотеки compile group: 'org.openoffice', name: 'bootstrap-connector', version: '0.1.1'
        XComponentContext xRemoteContext = BootstrapSocketConnector.bootstrap(oooExeFolder);

        if (xRemoteContext == null) {
            System.err.println("ERROR: Could not bootstrap default Office.");
        }

        assert xRemoteContext != null;
        xRemoteServiceManager = xRemoteContext.getServiceManager();

        Object desktop =
                xRemoteServiceManager.createInstanceWithContext("com.sun.star.frame.Desktop", xRemoteContext);

        xComponentLoader = UnoRuntime.queryInterface(XComponentLoader.class, desktop);
        loadProps = new PropertyValue[0];
    }

    public void subscribeUpdateSelectedElementsEventEmitter(Observer<Void> observer) {
        listSubscriptionUpdateSelectedElementsEventEmitter.add(this.updateSelectedElementsEventEmitter.subscribe(observer));
    }

    public void openNewLibreOfficeFile(Stage stage) throws Exception {
        openNewAndCloseOldLinkWithOpenOfficeApi(null, stage);
    }

    public void openExistingLibreOfficeFile(String nameFile, Stage stage) throws Exception {
        openNewAndCloseOldLinkWithOpenOfficeApi(nameFile, stage);
    }

    private void openNewAndCloseOldLinkWithOpenOfficeApi(String nameFile, Stage stage) throws Exception {
        XComponent oldXSpreadsheetComponent = getXSpreadsheetComponent();
        //@ToDo Надо будет при возможности разобрать с ошибкой, которая описана ниже
        //Сворачиваем и разворачиваем окно JavaFx для того, чтобы убрать с него фокус
        //Если не убрать фокус, то тогда по какой-то причине новое окно OpenOffice зависает на строчке xComponentLoader.loadComponentFromURL
        if (this.primaryStage != null) {
            this.primaryStage.setIconified(true);
        }
        if (nameFile != null) {
            try {
                String urlOfFile = "file:///" + folderUrlToSaveOdsFiles + nameFile + ".ods";
                openNewLinkWithOffice(urlOfFile);
            } catch (Exception e) {
                e.printStackTrace();
                createAlertWarning(stage, "Ошибка при открытии сохраненного документа ods\n" + e.getMessage());
                throw e;
            }
        } else {
            try {
                openNewLinkWithOffice(null);
            } catch (Exception e) {
                e.printStackTrace();
                createAlertWarning(stage, "Ошибка при открытии нового документа ods\n" + e.getMessage());
                throw e;
            }
        }
        if (this.primaryStage != null) {
            this.primaryStage.setIconified(false);
        }
        if (oldXSpreadsheetComponent != null) {
            try {
                oldXSpreadsheetComponent.dispose();
            } catch (Exception e) {
                e.printStackTrace();
                createAlertWarning(stage, "Ошибка при закрытии старого документа ods\n" + e.getMessage());
                throw e;
            }
        }
    }

    private void openNewLinkWithOffice(String path) throws Exception {
        xSpreadsheetComponent = xComponentLoader.loadComponentFromURL(
                path == null ? "private:factory/scalc" : path, "_blank", 0, loadProps);

        xSpreadsheetDocument = UnoRuntime.queryInterface(
                XSpreadsheetDocument.class, xSpreadsheetComponent);

        this.xDocModel = UnoRuntime.queryInterface(XModel.class, xSpreadsheetDocument);
        this.xSpreadsheetModel = UnoRuntime.queryInterface(XModel.class, xDocModel);
        this.xSpreadsheetController = xSpreadsheetModel.getCurrentController();
        this.xSpreadsheetView = UnoRuntime.queryInterface(XSpreadsheetView.class, xSpreadsheetController);
        this.activeSheet = this.xSpreadsheetView.getActiveSheet();

        //com.sun.star.sheet.XSpreadsheet xSheet = xSpreadsheetView.getActiveSheet();
        //com.sun.star.table.XCellRange xResultRange = xSheet.getCellRangeByName("A2:B5");
        //xSel.select(xResultRange);

        //XFrame frame = xSpreadsheetController.getFrame();
        //XWindow xWindow = frame.getContainerWindow();

        this.xSel = UnoRuntime.queryInterface(com.sun.star.view.XSelectionSupplier.class, xSpreadsheetController);
        this.activeCells = UnoRuntime.queryInterface(XCellRangeAddressable.class, xSpreadsheetModel.getCurrentSelection());
        XSelectionChangeListener xSelectionChangeListener = new XSelectionChangeListener() {
            @Override
            //Учтем, что в этом методе мы работаем в потоке OpenOffice
            public void selectionChanged(EventObject eventObject) {
                XSpreadsheet selectedSheet = xSpreadsheetView.getActiveSheet();
                XCellRangeAddressable selectedCells = UnoRuntime.queryInterface(XCellRangeAddressable.class, xSpreadsheetModel.getCurrentSelection());
                if (selectedSheet == null || selectedCells == null) {
                    // Platform.runLater используется для того, чтобы действия указанные в Runnable выполнялись в потоке выполнения JavaFx
                    // Иначе будет выскакивать Exception о том, что невозможно работать с элементами JavaFx не в потоке вывполнения JavaFx
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            listSelectedCellFormMarkInfo.clear();
                            listSelectedIterationGroupDto.clear();
                            listSelectedIterationGroupHandbookDto.clear();
                        }
                    });
                    return;
                }

                //Проверка на то, была ли выбрана та же самая область
                //Проверка нужна из-за того, что при выборе области экрана проверка selectionChanged срабатывает несколько раз
                XNamed selectedNamed = UnoRuntime.queryInterface(XNamed.class, selectedSheet);
                XNamed activeNamed = UnoRuntime.queryInterface(XNamed.class, activeSheet);
                if (
                        activeNamed != null && selectedNamed.getName().equals(activeNamed.getName())
                                && selectedCells.getRangeAddress().StartRow == activeCells.getRangeAddress().StartRow
                                && selectedCells.getRangeAddress().StartColumn == activeCells.getRangeAddress().StartColumn
                                && selectedCells.getRangeAddress().EndRow == activeCells.getRangeAddress().EndRow
                                && selectedCells.getRangeAddress().EndColumn == activeCells.getRangeAddress().EndColumn
                ) {
                    return;
                }
                //Установка новых активных ячеек
                activeSheet = selectedSheet;
                activeCells = selectedCells;
                //Переменные, которые нужны для того, чтобы проверить в TimerTask, соответствует ли лист и выделенные ячейки тому состоянию, которые были до delay
                XNamed activeNamedInThisMethod = UnoRuntime.queryInterface(XNamed.class, activeSheet);
                //Определяем данные переменные здесь, так как в потоке OpenOffice это делается в раз 20 быстрее
                String activeSheetNameInThisMethod = activeNamedInThisMethod.getName();
                int activeStartColumnInThisMethod = activeCells.getRangeAddress().StartColumn;
                int activeEndColumnInThisMethod = activeCells.getRangeAddress().EndColumn;
                int activeStartRowInThisMethod = activeCells.getRangeAddress().StartRow;
                int activeEndRowInThisMethod = activeCells.getRangeAddress().EndRow;
                //Platform.runLater используется для того, чтобы действия указанные в Runnable выполнялись в потоке выполнения JavaFx
                //В данном случае мы снимаем нагрузку с процесса Java в OpenOffice в пользу нагрузки процесса Java в нашем приложении
                //TimerTask-и в таком случае будут создаваться в процессе Java в нашем приложении
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //Проверяем через delay, выделена ли все еще та же самая область ячеек. И только если ответ положителен, то выполняем действия,
                        //которые предписаны в listSelectedNumericalPokValueProperty.addListener (после установки listSelectedNumericalPokValueProperty.setValue(listSelectedNumericalPokValueInSelection))
                        //Для каждого Timer создается РОВНО ОДИН поток, в котором выполняются все TimerTask, которые привязаны к Timer с помощью schedule
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            //Уже не поток OpenOffice, но еще и не поток JavaFx
                            public void run() {
                                XNamed activeNamedGlobal = UnoRuntime.queryInterface(XNamed.class, activeSheet);
                                String activeSheetNameGlobal = activeNamedGlobal.getName();
                                int activeStartColumnGlobal = activeCells.getRangeAddress().StartColumn;
                                int activeEndColumnGlobal = activeCells.getRangeAddress().EndColumn;
                                int activeStartRowGlobal = activeCells.getRangeAddress().StartRow;
                                int activeEndRowGlobal = activeCells.getRangeAddress().EndRow;
                                //Проверка на то, была ли выбрана другая область за время delay
                                if (!(
                                        activeSheetNameGlobal.equals(activeSheetNameInThisMethod)
                                                && activeStartRowGlobal == activeStartRowInThisMethod
                                                && activeStartColumnGlobal == activeStartColumnInThisMethod
                                                && activeEndRowGlobal == activeEndRowInThisMethod
                                                && activeEndColumnGlobal == activeEndColumnInThisMethod
                                )) {
                                    return;
                                }
                                //Platform.runLater используется для того, чтобы действия указанные в Runnable выполнялись в потоке выполнения JavaFx
                                //Иначе будет выскакивать Exception о том, что невозможно работать с элементами JavaFx не в потоке вывполнения JavaFx
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateSelectedElements(activeSheetNameGlobal, activeStartColumnGlobal, activeEndColumnGlobal, activeStartRowGlobal, activeEndRowGlobal);
                                    }
                                });
                            }
                        }, 300);
                    }
                });
            }

            @Override
            public void disposing(EventObject eventObject) {

            }
        };
        xSel.addSelectionChangeListener(xSelectionChangeListener);
    }

    public void updateSelectedElements() {
        XNamed activeNamed = UnoRuntime.queryInterface(XNamed.class, activeSheet);
        String activeSheetName = activeNamed.getName();
        int activeStartColumn = activeCells.getRangeAddress().StartColumn;
        int activeEndColumn = activeCells.getRangeAddress().EndColumn;
        int activeStartRow = activeCells.getRangeAddress().StartRow;
        int activeEndRow = activeCells.getRangeAddress().EndRow;
        updateSelectedElements(activeSheetName, activeStartColumn, activeEndColumn, activeStartRow, activeEndRow);
    }

    public void updateSelectedElements(String sheetName, Integer startColumn, Integer endColumn, Integer startRow, Integer endRow) {
        NumericalPokValue numericalPokValue;
        CellFormMarkInfo cellFormMarkInfo;
        ArrayList<CellFormMarkInfo> listSelectedCellFormMarkInfo = new ArrayList<>();
        ArrayList<IterationGroupDto> listSelectedIterationGroupDto = new ArrayList<>();
        ArrayList<IterationGroupHandbookDto> listSelectedIterationGroupHandbookDto = new ArrayList<>();
        int maxColumn = Math.min(startColumn + 100, endColumn);
        int maxRow = Math.min(startRow + 100, endRow);
        for (int columnIndex = startColumn; columnIndex <= maxColumn; columnIndex++) {
            b:
            for (int rowIndex = startRow; rowIndex <= maxRow; rowIndex++) {
                //Попытка найти разметку обычной (не в составе итерационной группы) ячейки
                for (int i = 0; i < listNumericalPokValue.size(); i++) {
                    numericalPokValue = listNumericalPokValue.get(i);
                    if (numericalPokValue.getCellInfo().getListName().equals(sheetName)
                            && numericalPokValue.getCellInfo().getRowNumber() == rowIndex + 1
                            && numericalPokValue.getCellInfo().getColumnNumber().equals(convertNumToColString(columnIndex))) {
                        cellFormMarkInfo = new CellFormMarkInfo();
                        cellFormMarkInfo.setNumericalPokValue(numericalPokValue);
                        cellFormMarkInfo.setIterationGroupDto(null);
                        listSelectedCellFormMarkInfo.add(cellFormMarkInfo);
                        continue b;
                    }
                }
                //Попытка найти разметку ячейки итерационной группы
                for (int i = 0; i < listIterationGroupDto.size(); i++) {
                    IterationGroupDto iterationGroupDto = listIterationGroupDto.get(i);
                    for (int j = 0; j < iterationGroupDto.getListIterationGroupFormMark().size(); j++) {
                        IterationGroupFormMarkDto iterationGroupFormMarkDto = iterationGroupDto.getListIterationGroupFormMark().get(j);
                        numericalPokValue = iterationGroupFormMarkDto.getNumericalPokValue();
                        if (numericalPokValue.getCellInfo().getListName().equals(sheetName)
                                && numericalPokValue.getCellInfo().getRowNumber() == rowIndex + 1
                                && numericalPokValue.getCellInfo().getColumnNumber().equals(convertNumToColString(columnIndex))
                        ) {
                            cellFormMarkInfo = new CellFormMarkInfo();
                            cellFormMarkInfo.setNumericalPokValue(numericalPokValue);
                            cellFormMarkInfo.setIterationGroupDto(iterationGroupDto);
                            listSelectedCellFormMarkInfo.add(cellFormMarkInfo);
                            continue b;
                        }
                    }
                }
                //Попытка найти принадлежность ячейки к итерационной группе
                for (int i = 0; i < listIterationGroupDto.size(); i++) {
                    IterationGroupDto iterationGroupDto = listIterationGroupDto.get(i);
                    CellRange cellAddressRange = new CellRange(iterationGroupDto.getListName(), iterationGroupDto.getCellAddressRange());
                    if (iterationGroupDto.getListName().equals(sheetName)
                            && convertColToNumString(cellAddressRange.getCellCoordinateBegin().getColumnName()) <= columnIndex
                            && convertColToNumString(cellAddressRange.getCellCoordinateEnd().getColumnName()) >= columnIndex
                            && cellAddressRange.getCellCoordinateBegin().getRowNumber() <= rowIndex + 1
                            && cellAddressRange.getCellCoordinateEnd().getRowNumber() >= rowIndex + 1
                    ) {
                        if (!listSelectedIterationGroupDto.contains(iterationGroupDto)) {
                            listSelectedIterationGroupDto.add(iterationGroupDto);
                        }
                        //Попытка найти разметку классификатора итерационной группы
                        for (int j = 0; j < iterationGroupDto.getListIterationGroupHandbook().size(); j++) {
                            IterationGroupHandbookDto iterationGroupHandbookDto = iterationGroupDto.getListIterationGroupHandbook().get(j);
                            CellCoordinate cellCoordinateGroupHandbookDto = CellCoordinate.parseCellCoordinateStr(iterationGroupDto.getListName(), iterationGroupDto.getCellAddressRange());
                            if (convertColToNumString(cellCoordinateGroupHandbookDto.getColumnName()) == columnIndex
                                    && cellCoordinateGroupHandbookDto.getRowNumber() == rowIndex
                            ) {
                                listSelectedIterationGroupHandbookDto.add(iterationGroupHandbookDto);
                            }
                        }
                        continue b;
                    }
                }
            }
        }
        this.listSelectedCellFormMarkInfo.clear();
        this.listSelectedCellFormMarkInfo.addAll(listSelectedCellFormMarkInfo);
        this.listSelectedIterationGroupDto.clear();
        this.listSelectedIterationGroupDto.addAll(listSelectedIterationGroupDto);
        this.listSelectedIterationGroupHandbookDto.clear();
        this.listSelectedIterationGroupHandbookDto.addAll(listSelectedIterationGroupHandbookDto);
        //Вызов EventEmitter-а для вызова подписанных событий
        this.updateSelectedElementsEventEmitter.onNext(null);
    }

    public String saveDocumentWithNewName(byte[] byteArray, Stage stage) {
        String nameFile = UUID.randomUUID().toString();

        //Сохранение файла в папке temp (disc 'C')
        File folder = new File(folderUrlToSaveOdsFiles);
        try {
            Files.createDirectories(folder.toPath());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при создании каталога для файла", e);
        }

        if (byteArray == null) {
            String UrlOfFile = "file:///" + folderUrlToSaveOdsFiles.replace("\\", "/") + nameFile + ".ods";
            XStorable xstorable = UnoRuntime.queryInterface(XStorable.class, getXSpreadsheetComponent());
            try {
                xstorable.storeToURL(UrlOfFile, getLoadProps());
            } catch (Exception e) {
                createAlertWarning(stage, "Ошибка при сохранении файла");
                e.printStackTrace(System.err);
                throw new RuntimeException("Ошибка при сохранении файла", e);
            }
        } else {
            File file = new File(folderUrlToSaveOdsFiles, nameFile + ".ods");
            if (!file.exists()) {
                try {
                    Files.createFile(file.toPath());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Ошибка при создании файла", e);
                }
            }
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(byteArray);
                //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
            } catch (Exception e) {
                createAlertWarning(stage, "Ошибка при сохранении файла");
                e.printStackTrace();
                throw new RuntimeException("Ошибка при сохранении файла", e);
            }
        }
        return nameFile;
    }

    public File getDocumentByName(String nameFile) {
        String UrlOfFile = folderUrlToSaveOdsFiles + nameFile + ".ods";
        return new File(UrlOfFile);
    }

    public Integer getSelectedCellsCount() {
        CellRangeAddress cellRangeAddress = activeCells.getRangeAddress();
        return (cellRangeAddress.EndColumn - cellRangeAddress.StartColumn + 1) * (cellRangeAddress.EndRow - cellRangeAddress.StartRow);
    }

    public boolean checkListSelectedCellFormMarkInfoHasSimpleCells() {
        return listSelectedCellFormMarkInfo.stream().anyMatch(v -> v.getIterationGroupDto() == null);
    }

    public void setCellAnnotation(XSpreadsheet xSpreadsheet, CellFormMarkInfo cellFormMarkInfo) throws Exception {
        if (cellFormMarkInfo == null) {
            throw new RuntimeException("Для установки аннотации cellFormMarkInfo должен быть не равен null");
        }
        NumericalPokValue numericalPokValue = cellFormMarkInfo.getNumericalPokValue();
        if (numericalPokValue == null) {
            throw new RuntimeException("Для установки аннотации cellFormMarkInfo.numericalPokValue должен быть не равен null");
        }
        CellCoordinate cellCoordinate = new CellCoordinate(
                numericalPokValue.getCellInfo().getListName(),
                numericalPokValue.getCellInfo().getRowNumber(),
                numericalPokValue.getCellInfo().getColumnNumber()
        );
//      String annotationText = getCellAnnotationText(cellFormMarkInfo);
        String annotationText="+";
        setCellAnnotation(xSpreadsheet, cellCoordinate, annotationText);
    }

    public void setCellAnnotation(XSpreadsheet xSpreadsheet, IterationGroupDto iterationGroupDto, IterationGroupHandbookDto iterationGroupHandbookDto) throws Exception {
        if (iterationGroupDto == null) {
            throw new RuntimeException("Для установки аннотации iterationGroupDto должен быть не равен null");
        }
        if (iterationGroupHandbookDto == null) {
            throw new RuntimeException("Для установки аннотации iterationGroupHandbookDto должен быть не равен null");
        }
        CellCoordinate cellCoordinate = CellCoordinate.parseCellCoordinateStr(iterationGroupDto.getListName(), iterationGroupHandbookDto.getCellAddress());
        String annotationText = getCellAnnotationText(iterationGroupDto, iterationGroupHandbookDto);
        setCellAnnotation(xSpreadsheet, cellCoordinate, annotationText);
    }

    private void setCellAnnotation(XSpreadsheet xSpreadsheet, CellCoordinate cellCoordinate, String annotationText) throws Exception {
        //Получение адреса ячейки
        XCell xCell = xSpreadsheet.getCellByPosition(CellCoordinate.getColumnNumberByName(cellCoordinate.getColumnName()) - 1, cellCoordinate.getRowNumber() - 1);
        XCellAddressable xCellAddr = UnoRuntime.queryInterface(XCellAddressable.class, xCell);
        CellAddress aAddress = xCellAddr.getCellAddress();
        //Вставка аннотации
        XSheetAnnotationsSupplier xAnnotationsSupp = UnoRuntime.queryInterface(XSheetAnnotationsSupplier.class, xSpreadsheet);
        XSheetAnnotations xAnnotations = xAnnotationsSupp.getAnnotations();
        xAnnotations.insertNew(aAddress, annotationText);
        //Получение объекта аннотации
//        com.sun.star.sheet.XSheetAnnotationAnchor xAnnotAnchor =
//                (com.sun.star.sheet.XSheetAnnotationAnchor) UnoRuntime.queryInterface(
//                        com.sun.star.sheet.XSheetAnnotationAnchor.class, xCell);
//        com.sun.star.sheet.XSheetAnnotation xAnnotation = xAnnotAnchor.getAnnotation();

        //Задание новой ширины аннотации
//        com.sun.star.sheet.XSheetAnnotationShapeSupplier xSheetAnnotationShapeSupplier = (com.sun.star.sheet.XSheetAnnotationShapeSupplier) UnoRuntime.queryInterface(
//                com.sun.star.sheet.XSheetAnnotationShapeSupplier.class, xAnnotation);
//        xSheetAnnotationShapeSupplier.getAnnotationShape().setSize(new com.sun.star.awt.Size(25000, xSheetAnnotationShapeSupplier.getAnnotationShape().getSize().Height));
    }

    private String getCellAnnotationText(CellFormMarkInfo cellFormMarkInfo) {
        IterationGroupDto iterationGroupDto = cellFormMarkInfo.getIterationGroupDto();
        NumericalPokValue numericalPokValue = cellFormMarkInfo.getNumericalPokValue();
        if (numericalPokValue == null) {
            return "";
        }
        AttributesForNumericalPokValue attributes = numericalPokValue.getAttributes();
        if (attributes == null) {
            return "";
        }
        StringBuilder annotationText = new StringBuilder();
        annotationText.append("Итерационная группа:\n").append(iterationGroupDto != null ? iterationGroupDto.getName() : "Не выбрана").append("\n\n");
        annotationText.append("Приоритет:\n").append(attributes.getPriority() != null ? attributes.getPriority().getRealName() : "Не выбран").append("\n\n");
        annotationText.append("АС ист.инф.:\n").append(attributes.getAsSourceInformation() != null ? attributes.getAsSourceInformation() : "Не выбран").append("\n\n");
        annotationText.append("Компонент АС ист. инф.:\n").append(attributes.getCompAsSourceInf() != null ? attributes.getCompAsSourceInf() : "Не выбран").append("\n\n");
        annotationText.append("Компонент АС ист. инф. для знач. пок.:\n").append(attributes.getCompAsToPokValue() != null ? attributes.getCompAsToPokValue() : "Не выбран").append("\n\n");
        annotationText.append("Показатель:\n").append(attributes.getPok() != null ? attributes.getPok() : "Не выбран").append("\n\n");
        annotationText.append("Значение показателя:\n").append(attributes.getPokValue() != null ? attributes.getPokValue() : "Не выбрано").append("\n\n");
        annotationText.append(getParamPokTextForAnnotation(1, attributes.getP1()));
        annotationText.append(getParamPokTextForAnnotation(2, attributes.getP2()));
        annotationText.append(getParamPokTextForAnnotation(3, attributes.getP3()));
        annotationText.append(getParamPokTextForAnnotation(4, attributes.getP4()));
        annotationText.append(getParamPokTextForAnnotation(5, attributes.getP5()));
        annotationText.append(getParamPokTextForAnnotation(6, attributes.getP6()));
        annotationText.append(getParamPokTextForAnnotation(7, attributes.getP7()));
        annotationText.append(getParamPokTextForAnnotation(8, attributes.getP8()));
        annotationText.append(getParamPokTextForAnnotation(9, attributes.getP9()));
        annotationText.append(getParamPokTextForAnnotation(10, attributes.getP10()));
        annotationText.append("Смещение по дате:\n").append(attributes.getOffsetDate() != null ? attributes.getPokValue() : "Смещение по дате отсутсвует").append("\n\n");
        annotationText.append("Отчетный час:\n").append(attributes.getHourReport() != null ? attributes.getHourReport() : "Не выбран");
        return annotationText.toString();
    }

    private String getParamPokTextForAnnotation(Integer paramPokIndex, ParamPokInfoForNumericalPokValue paramPokInfoForNumericalPokValue) {
        StringBuilder paramPokTextForAnnotation = new StringBuilder();
        paramPokTextForAnnotation.append("Параметр ").append(paramPokIndex).append(":");
        if (paramPokInfoForNumericalPokValue != null) {
            paramPokTextForAnnotation.append("\n");
            paramPokTextForAnnotation.append("Параметр:\n").append(paramPokInfoForNumericalPokValue.getParamPok()).append("\n");
            paramPokTextForAnnotation.append("Классификатор:\n").append(paramPokInfoForNumericalPokValue.getHandbook()).append("\n");
            paramPokTextForAnnotation.append("Значение параметра:\n").append(paramPokInfoForNumericalPokValue.getHandbookRow()).append("\n");
        } else {
            paramPokTextForAnnotation.append(" Не существует для выбранного показателя\n");
        }
        paramPokTextForAnnotation.append("\n");
        return paramPokTextForAnnotation.toString();
    }

    private String getCellAnnotationText(IterationGroupDto iterationGroupDto, IterationGroupHandbookDto iterationGroupHandbookDto) {
        if (iterationGroupDto == null) {
            return "";
        }
        if (iterationGroupHandbookDto == null) {
            return "";
        }
        StringBuilder annotationText = new StringBuilder();
        annotationText.append("Итерационная группа:\n").append(iterationGroupDto.getName()).append("\n\n");
        annotationText.append("Классификатор итерационной группы:\n").append(iterationGroupHandbookDto).append("\n\n");
        return annotationText.toString();
    }
}
