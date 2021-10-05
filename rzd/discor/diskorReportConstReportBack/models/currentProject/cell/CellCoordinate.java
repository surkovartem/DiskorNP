package ru.rzd.discor.diskorReportConstReportBack.models.currentProject.cell;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.SetFiltersException;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.CellInfoForNumericalPokValue;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.NumericalPokValue;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.pow;

@Getter
@ToString
//listName, rowNumber, columnName != null
public class CellCoordinate {
    private String listName;
    private Integer rowNumber;
    private String columnName;

    public void setListName(String listName) {
        if (listName == null) {
            listName = "Лист1";
        }
        this.listName = listName;
    }

    public void setRowNumber(Integer rowNumber) {
        if (rowNumber == null) {
            rowNumber = 1;
        }
        this.rowNumber = rowNumber;
    }

    public void setColumnName(String columnName) {
        if (columnName == null) {
            columnName = "A";
        }
        this.columnName = columnName;
    }

    public static Integer getColumnNumberByName(String columnName) {
        char[] charArray = columnName.toCharArray();
        int columnNumber = 0;
        for (int i = 0; i < charArray.length; i++) {
            // The letter A is at 65
            columnNumber += (charArray[i] - 64) * pow(26, charArray.length - i - 1);
        }
        return columnNumber;
    }

    public static String getColumnNameByNumber(Integer columnNumber) {
        int excelColNum = columnNumber;
        StringBuilder colRef = new StringBuilder(2);
        int colRemain = excelColNum;

        while (colRemain > 0) {
            int thisPart = colRemain % 26;
            if (thisPart == 0) {
                thisPart = 26;
            }
            colRemain = (colRemain - thisPart) / 26;

            // The letter A is at 65
            char colChar = (char) (thisPart + 64);
            colRef.insert(0, colChar);
        }

        return colRef.toString();
    }

    public CellCoordinate() {
        this.setListName(null);
        this.setRowNumber(null);
        this.setColumnName(null);
    }

    public CellCoordinate(String listName, Integer rowNumber, String columnName) {
        this.setListName(listName);
        this.setRowNumber(rowNumber);
        this.setColumnName(columnName);
    }

    public static boolean equals(
            CellCoordinate object1,
            CellCoordinate object2
    ) {
        return (
                (
                        object1 == null
                                && object2 == null
                ) || (
                        object1 != null
                                && object2 != null
                                && object1.getListName().equals(object2.getListName())
                                && object1.getRowNumber().equals(object2.getRowNumber())
                                && object1.getColumnName().equals(object2.getColumnName())
                )
        );
    }

    private static NumericalPokValue checkCellInListNumericalPokValue(CellCoordinate cellCoordinate, List<NumericalPokValue> listNumericalPokValue) throws SetFiltersException {
        if (!(listNumericalPokValue != null && listNumericalPokValue.size() > 0)) {
            throw new SetFiltersException("Лист размеченных ячеек не должен быть равен 0 и его длина не должна быть равна 0 для поиска координаты ячейки");
        }
        for (NumericalPokValue numericalPokValue : listNumericalPokValue) {
            CellInfoForNumericalPokValue cellInfo = numericalPokValue.getCellInfo();
            CellCoordinate cellCoordinateNumericalPokValue = new CellCoordinate(cellInfo.getListName(), cellInfo.getRowNumber(), cellInfo.getColumnNumber());
            if (cellCoordinate.equals(cellCoordinateNumericalPokValue)) {
                return numericalPokValue;
            }
        }
        throw new SetFiltersException("Координата ячейки не была найдена ни в одной из размеченных ячеек");
    }

    public static CellCoordinate parseCellCoordinateStr(String listName, String cellCoordinateStr) throws SetFiltersException {
        Integer indexBeginRowNumber;
        String patternStr = "[1-9]";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(cellCoordinateStr);
        if (matcher.find()) {
            indexBeginRowNumber = matcher.start();
        } else {
            throw new SetFiltersException("Ошибка при парсинге строки ячейки " + cellCoordinateStr);
        }
        CellCoordinate cellCoordinate = new CellCoordinate(
                listName,
                Integer.parseInt(cellCoordinateStr.substring(indexBeginRowNumber + 1)),
                cellCoordinateStr.substring(0, indexBeginRowNumber)
        );
        return cellCoordinate;
    }
}
