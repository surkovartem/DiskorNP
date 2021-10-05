package ru.rzd.discor.diskorReportConstReportBack.models.ImportExport;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.rzd.discor.diskorReportConstReportBack.Main;
import ru.rzd.discor.diskorReportConstReportBack.controllers.ConstructorModeController;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.NumericalPokValue;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ImportFormClass {
    Stage stage = new Stage();

    public FullFormMarkInfo ReadZipForm() throws Exception {
        FullFormMarkInfo fullFormMarkInfo = new FullFormMarkInfo();
        //Работа с содержимым zip файла без разархивирования
        try (
                ZipFile zipFile = new ZipFile(SelectPathZipToImport());
        ) {
            for (Enumeration e = zipFile.entries(); e.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                String nameFile = entry.getName();
                InputStream inputStream = zipFile.getInputStream(entry);
                if (nameFile.contains(".txt")) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();
                    while (line != null) {
                        sb.append(line).append("\n");
                        line = br.readLine();
                    }
                    String fileAsString = sb.toString();
                    ArrayList<NumericalPokValue> list =
                            new ArrayList<NumericalPokValue>(Arrays.asList(Main.objectMapper.readValue(fileAsString, NumericalPokValue[].class)));
                    fullFormMarkInfo.setListNumericalPokValue(list);
                }
                if (nameFile.contains(".ods")) {
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int nRead;
                    byte[] data = new byte[1024];
                    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    buffer.flush();
                    fullFormMarkInfo.setFileByteArray(buffer.toByteArray());
                }
            }
        }
        return fullFormMarkInfo;
    }

    private String SelectPathZipToImport() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Импорт");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Zip files (*.zip)", "*.zip");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            throw new Exception("Архив отчета не был выбран");
        }
        return file.getAbsolutePath();
    }
}
