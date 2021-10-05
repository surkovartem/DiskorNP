package ru.rzd.discor.diskorReportConstReportBack.models.ImportExport;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.rzd.discor.diskorReportConstReportBack.Main;
import ru.rzd.discor.diskorReportConstReportBack.models.reportRepo.Form;

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ExportFormClass {
    String StringListNumericalPokValue = "stringListNumericalPokValue.txt";
    String FileName = "form.ods";

    Stage stage = new Stage();

    public void CreateZipExport(FullFormMarkInfo fullFormMarkInfo, Form form) throws Exception {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(SelectPathToExport(form)))) {
            ZipEntry entry = new ZipEntry(StringListNumericalPokValue);
            zout.putNextEntry(entry);
            zout.write(Main.objectMapper.writeValueAsString(fullFormMarkInfo.getListNumericalPokValue()).getBytes());
            zout.closeEntry();

            entry = new ZipEntry(FileName);
            zout.putNextEntry(entry);
            zout.write(fullFormMarkInfo.getFileByteArray());
            zout.closeEntry();
        }
    }

    private String SelectPathToExport(Form form) throws Exception {
        String initialFileName;
        if (form == null) {
            initialFileName = "unnamed";
        } else {
            initialFileName = form.getName();
        }
        initialFileName += ".zip";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Экспорт");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Zip files (*.zip)", "*.zip");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(initialFileName);
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            throw new Exception("Архив отчета не был сохранен");
        }
        return file.getAbsolutePath();
    }
}
