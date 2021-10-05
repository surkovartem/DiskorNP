package ru.rzd.discor.diskorReportConstReportBack;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import ru.rzd.discor.diskorReportConstReportBack.controllers.ConstructorModeController;
import ru.rzd.discor.diskorReportConstReportBack.controllers.LogInController;
import ru.rzd.discor.diskorReportConstReportBack.controllers.LogOutController;
import ru.rzd.discor.diskorReportConstReportBack.controllers.OperatorModeController;
import ru.rzd.discor.diskorReportConstReportBack.customElements.AlertWithFixedMessageLength;
import ru.rzd.discor.diskorReportConstReportBack.models.user.User;
import ru.rzd.discor.diskorReportConstReportBack.services.CoreService;
import ru.rzd.discor.diskorReportConstReportBack.services.GeneralParamsService;
import ru.rzd.discor.diskorReportConstReportBack.services.ReportRepoService;
import ru.rzd.discor.diskorReportConstReportBack.services.UserService;

import tray.animations.AnimationType;
import tray.notification.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;
import java.util.TimeZone;

public class Main extends Application {
    public static String coreApiUrl;
    public static String reportRepoApiUrl;
    public static String folderUrlForTokenFile;
    public static String fileNameForTokenFile;
    public static String folderUrlToSaveOdsFiles;
    public static String urlWebAuthorization;
    public static String urlWebLogOut;

    public static User user;
    public static ObjectMapper objectMapper;
    public static GeneralParamsService generalParamsService = new GeneralParamsService();
    public static CoreService coreService = new CoreService();
    public static ReportRepoService reportRepoService = new ReportRepoService();
    public static UserService userService = new UserService();
    public static String token = "";
    //Полученный токен из окна авторизации WebView.fxml
    public static String tokenToSave = "";

    public static void main(String[] args) throws Exception {//
        launch(args);
    }

    private static void setObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        objectMapper.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void init() {
        String activeProfile = "dev";
        if (activeProfile.equals("dev")) {
            coreApiUrl = "###";
            reportRepoApiUrl = "###";
            folderUrlForTokenFile = "C:\\diskorReportConstReport\\dev\\token\\";
            fileNameForTokenFile = "token.txt";
            folderUrlToSaveOdsFiles = "C:\\diskorReportConstReport\\dev\\tmp\\";
            urlWebAuthorization = "###";
            urlWebLogOut = "###";
        } else if (activeProfile.equals("test")) {
            coreApiUrl = "###";
            reportRepoApiUrl = "###";
            folderUrlForTokenFile = "C:\\diskorReportConstReport\\test\\token\\";
            fileNameForTokenFile = "token.txt";
            folderUrlToSaveOdsFiles = "C:\\diskorReportConstReport\\test\\tmp\\";
            urlWebAuthorization = "###";
            urlWebLogOut = "###";
        } else if (activeProfile.equals("prod")) {
            coreApiUrl = "###";
            reportRepoApiUrl = "###";
            folderUrlForTokenFile = "C:\\diskorReportConstReport\\prod\\token\\";
            fileNameForTokenFile = "token.txt";
            folderUrlToSaveOdsFiles = "C:\\diskorReportConstReport\\prod\\tmp\\";
            urlWebAuthorization = "###";
            urlWebLogOut = "###";
        }
        setObjectMapper();
    }

    @Override
    public void start(Stage primaryStage) {
        //Добавление слушателя на нажатие кнопки закрытия главного окна приложения
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //Остановка приложения JavaFx
                Platform.exit();
            }
        });
        try {
            token = getAuthorizationTokenFromFile();
            user = userService.getUser(token);
            System.out.println(user.getExp());
            System.out.println(getCurrentTime());
            System.out.println(user.getExp() - getCurrentTime());

        } catch (Exception e) {
//            createAlertWarning(null, e.getMessage(), true);
            try {
                inputAndSaveTokenFromWindowAuthorization();
            } catch (Exception e1) {
                e.printStackTrace();
                createAlertWarning(null, "Ошибка при добавлении токена\n" + e1.getMessage(), true);
                return;
            }
            createAlertWarning(null, "Токен был успешно добавлен", true);
        }

        if (token == null || token.equals("")) {
            createAlertWarning(null, "Токен в файле токена не заполнен", true);
            try {
                inputAndSaveTokenFromWindowAuthorization();
            } catch (Exception e) {
                e.printStackTrace();
                createAlertWarning(null, "Ошибка при добавлении токена\n" + e.getMessage(), true);
                return;
            }
            createAlertWarning(null, "Токен был успешно добавлен", true);
        }

        if(isFreshToken(user.getExp(), getCurrentTime())){
            try{
                openConstructorMode(primaryStage);
            } catch (Exception e){
                e.printStackTrace();
                createAlertWarning(null, "Ошибка при открытии главной формы\n" + e.getMessage(), true);
            }
        }
        else {
            try {
                inputAndSaveTokenFromWindowAuthorization();
                openConstructorMode(primaryStage);
            } catch (Exception e){
                e.printStackTrace();
                createAlertWarning(null, "Ошибка при добавлении токена\n" + e.getMessage(), true);
            }
        }
    }

    @Override
    public void stop() {
        System.out.println("Stage is closing");
        //Закрытие окна LibreOffice
        if (generalParamsService != null && generalParamsService.getXSpreadsheetComponent() != null) {
            try {
                generalParamsService.getXSpreadsheetComponent().dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    public static void inputAndSaveTokenFromWindowAuthorization() throws Exception {
        openWindowAuthorization();
        if (tokenToSave == null || tokenToSave.equals("")) {
            throw new Exception("Не был указан новый токен");
        }
        saveAuthorizationTokenToFile();
        token = tokenToSave;
        user = userService.getUser(token);
    }
    //------------------------------------------------------------------------------------------------------------------
    public static void LogOut() throws Exception {
        openWindowLogOut();
        saveAuthorizationTokenToFile();
        token = tokenToSave;
        user = null;
    }
    //------------------------------------------------------------------------------------------------------------------
    //Открыть основной интерфейс конструктора
    public static void openConstructorMode(Stage primaryStage) throws Exception {
        if (generalParamsService != null && generalParamsService.getXSpreadsheetComponent() != null) {
            try {
                generalParamsService.getXSpreadsheetComponent().dispose();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Ошибка при закрытии старого документа ods\n" + e.getMessage(), e);
            }
        }
        generalParamsService = new GeneralParamsService();
        generalParamsService.setInitialParams(primaryStage);
        ConstructorModeController constructorModeController = new ConstructorModeController();
        primaryStage.setTitle("Конструктор отчетов. Режим конструктора");
        primaryStage.getIcons().add(new javafx.scene.image.Image(ConstructorModeController.class.getResourceAsStream("/assets/icon_32.png")));
        //И ширина, и длина должны быть на 2px больше, чем реальные размеры fxml,
        //чтобы не появлялась полоса прокрутки (данное расстояние нужно для рамок окна, по 1 px с каждой стороны)
        primaryStage.setScene(new Scene(constructorModeController, 828, 760));
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        constructorModeController.setStage(primaryStage);
        constructorModeController.setInitialParams();
        constructorModeController.SetCurrentFileActiveSheet();
        primaryStage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()));
        primaryStage.setY(1);

    }
    public static void openOperatorMode(Stage primaryStage) throws Exception {
        if (generalParamsService != null && generalParamsService.getXSpreadsheetComponent() != null) {
            try {
                generalParamsService.getXSpreadsheetComponent().dispose();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Ошибка при закрытии старого документа ods\n" + e.getMessage(), e);
            }
        }
        generalParamsService = new GeneralParamsService();
        generalParamsService.setInitialParams(primaryStage);
        OperatorModeController operatorModeController = new OperatorModeController();
        primaryStage.setTitle("Конструктор отчетов. Режим оператора");
        primaryStage.getIcons().add(new javafx.scene.image.Image(OperatorModeController.class.getResourceAsStream("/assets/icon_32.png")));
        //И ширина, и длина должны быть на 2px больше, чем реальные размеры fxml,
        //чтобы не появлялась полоса прокрутки (данное расстояние нужно для рамок окна, по 1 px с каждой стороны)
        primaryStage.setScene(new Scene(operatorModeController, 828, 760));
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        operatorModeController.setStage(primaryStage);
        operatorModeController.setInitialParams();
        operatorModeController.SetCurrentFileActiveSheet();
        primaryStage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()));
        primaryStage.setY(1);
    }
    //------------------------------------------------------------------------------------------------------------------
    //Окно авторизации в системе ДИСКОР НП
    private static void openWindowAuthorization() throws Exception {
        tokenToSave = "";
        LogInController logInController;
        try {
            logInController = new LogInController();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Ошибка при открытии окна авторизации", e);
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(logInController, 1202, 800));
        stage.setTitle("Авторизация - Конструктор отчетов");
        stage.getIcons().add(new javafx.scene.image.Image(ConstructorModeController.class.getResourceAsStream(
                "/assets/icon_32.png")));
//        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        logInController.setStage(stage);
        stage.showAndWait();
    }
    //------------------------------------------------------------------------------------------------------------------
    //Окно LogOut в системе ДИСКОР НП
    private static void openWindowLogOut() throws Exception {
        LogOutController logOutController;
        try {
             logOutController = new LogOutController();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Ошибка при открытии окна выхода из системы", e);
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(logOutController, 1202, 702));
        stage.setTitle("Выход из системы - Конструктор отчетов");
        stage.getIcons().add(new javafx.scene.image.Image(ConstructorModeController.class.getResourceAsStream(
                "/assets/icon_32.png")));
//        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        logOutController.setStage(stage);
        stage.showAndWait();
    }
    //------------------------------------------------------------------------------------------------------------------
    private static String getAuthorizationTokenFromFile() throws Exception {
        File file = new File(folderUrlForTokenFile, fileNameForTokenFile);
        if (!file.exists()) {
            throw new Exception("Файл с токеном не существует");
        }
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                resultStringBuilder.append(line);
            }
        } catch (Exception e) {
            throw new Exception("Ошибка чтения из файла", e);
        }

        return resultStringBuilder.toString();
    }
    //------------------------------------------------------------------------------------------------------------------
    private static Long getCurrentTime(){
        return System.currentTimeMillis() / 1000L;
    }
    //------------------------------------------------------------------------------------------------------------------
    private static boolean isFreshToken(Long TokenTime, Long CurrentTime){
        boolean flag;
        flag = TokenTime > (CurrentTime + 60 * 60 * 3);
        return flag;
    }
    //------------------------------------------------------------------------------------------------------------------
    private static void saveAuthorizationTokenToFile() throws Exception {
        File folder = new File(folderUrlForTokenFile);
        try {
            Files.createDirectories(folder.toPath());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Ошибка при создании каталога для файла токена", e);
        }
        File file = new File(folderUrlForTokenFile, fileNameForTokenFile);
        if (!file.exists()) {
            try {
                Files.createFile(file.toPath());
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Ошибка при создании файла токена", e);
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            writer.write(tokenToSave);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Ошибка записи в файл токена", e);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public static void createAlertInfo(Stage stage, String message) {
        createAlertInfo(stage, message, false);
    }
    //------------------------------------------------------------------------------------------------------------------
    public static void createAlertInfo(Stage stage, String message, Boolean waitUntilClose) {
        createAlert(stage, message, Alert.AlertType.INFORMATION, waitUntilClose);
    }
    //------------------------------------------------------------------------------------------------------------------
    public static void createAlertWarning(Stage stage, String message) {
        createAlertWarning(stage, message, false);
    }
    //------------------------------------------------------------------------------------------------------------------
    public static void createAlertWarning(Stage stage, String message, Boolean waitUntilClose) {
        createAlert(stage, message, Alert.AlertType.WARNING, waitUntilClose);
    }
    //------------------------------------------------------------------------------------------------------------------
    public static void createAlert(Stage stage, String message, Alert.AlertType alertType, Boolean waitUntilClose) {
        try {
            AlertWithFixedMessageLength alert = new AlertWithFixedMessageLength(alertType, message);
            if (stage != null) {
                alert.initOwner(stage);
                //Проверка на то, что окно свернуто
                //Проверка нужна, так как если окно свернуто, то alert или stage зависнет, так как он привяжется к свернутому окну
                //Проверку можно не выполнять, так как setIconified(false) отработает корректно и для развернутого окна, но для понимания оставляю
                if (stage.isIconified()) {
                    //Разворачиваем окно
                    stage.setIconified(false);
                }
            }
            if (waitUntilClose) {
                alert.showAndWait();
            } else {
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public static Optional<ButtonType> createDialogAlert(Stage stage, String title, String message){
        try {
            AlertWithFixedMessageLength alert = new AlertWithFixedMessageLength(Alert.AlertType.CONFIRMATION, message);
            alert.setTitle(title);
            if (stage != null) {
                alert.initOwner(stage);
                //Проверка на то, что окно свернуто
                //Проверка нужна, так как если окно свернуто, то alert или stage зависнет, так как он привяжется к свернутому окну
                //Проверку можно не выполнять, так как setIconified(false) отработает корректно и для развернутого окна, но для понимания оставляю
                if (stage.isIconified()) {
                    //Разворачиваем окно
                    stage.setIconified(false);
                }
            }
            return alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void createSuccessNotification(String message){
        TrayNotification tray = new TrayNotification();
        tray.notification.NotificationType type = NotificationType.SUCCESS;
        tray.setTitle("Успешно");
        tray.setMessage(message);
        tray.setAnimationType(AnimationType.POPUP);
        tray.setRectangleFill(Paint.valueOf("#2A9A84"));
        tray.showAndDismiss(Duration.seconds(4));
        tray.setNotificationType(type);
    }

    public static void createErrorNotification(String message){
        TrayNotification tray = new TrayNotification();
        tray.notification.NotificationType type = NotificationType.ERROR;
        tray.setTitle("Ошибка");
        tray.setMessage(message);
        tray.setAnimationType(AnimationType.POPUP);
        tray.setRectangleFill(Paint.valueOf("#CC0033"));
        tray.showAndDismiss(Duration.seconds(10));
        tray.setNotificationType(type);
    }

    public static void createInfoNotification(String message){
        TrayNotification tray = new TrayNotification();
        tray.notification.NotificationType type = NotificationType.INFORMATION;
        tray.setTitle("Внимание");
        tray.setMessage(message);
        tray.setAnimationType(AnimationType.POPUP);
        tray.setRectangleFill(Paint.valueOf("#2C54AB"));
        tray.showAndDismiss(Duration.seconds(5));
        tray.setNotificationType(type);
    }
}
