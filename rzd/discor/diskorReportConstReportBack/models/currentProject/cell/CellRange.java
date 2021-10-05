package ru.rzd.discor.diskorReportConstReportBack.models.currentProject.cell;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.SetFiltersException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CellRange {
    CellCoordinate cellCoordinateBegin;
    CellCoordinate cellCoordinateEnd;

    public CellRange(String listName, String cellAddressRange) throws SetFiltersException {
        if (!(listName != null && listName.length() > 0)) {
            throw new SetFiltersException("Имя листа диапазона ячеек не должно быть равно null, и длина имени листа диапазона ячеек должна быть больше 0");
        }
        if (!(cellAddressRange != null && cellAddressRange.length() > 0)) {
            throw new SetFiltersException("Строка диапазона ячеек должна быть не равна null, и длина строки диапазона ячеек должна быть больше 0");
        }
        String stringCellCoordinateBegin, stringCellCoordinateEnd;
        Integer indexOfCellCoordinateSplit, indexBeginRowNumber;
        indexOfCellCoordinateSplit = cellAddressRange.indexOf(":");
        if (indexOfCellCoordinateSplit == -1) {
            throw new SetFiltersException("Строка диапазона ячеек должна содержать разделительный символ \":\" (" + cellAddressRange + ")");
        }
        stringCellCoordinateBegin = cellAddressRange.substring(0, indexOfCellCoordinateSplit);
        stringCellCoordinateEnd = cellAddressRange.substring(indexOfCellCoordinateSplit + 1);

        String patternStr = "[1-9]";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(stringCellCoordinateBegin);
        if (matcher.find()) {
            indexBeginRowNumber = matcher.start();
        } else {
            throw new SetFiltersException("В строке координаты начала диапазона ячеек не указан номер строки (" + cellAddressRange + ")");
        }
        CellCoordinate cellCoordinateBegin = new CellCoordinate(
                listName,
                Integer.parseInt(stringCellCoordinateBegin.substring(indexBeginRowNumber + 1)),
                stringCellCoordinateBegin.substring(0, indexBeginRowNumber)
        );
        matcher = pattern.matcher(stringCellCoordinateEnd);
        if (matcher.find()) {
            indexBeginRowNumber = matcher.start();
        } else {
            throw new SetFiltersException("В строке координаты конца диапазона ячеек не указан номер строки (" + cellAddressRange + ")");
        }
        CellCoordinate cellCoordinateEnd = new CellCoordinate(
                listName,
                Integer.parseInt(stringCellCoordinateBegin.substring(indexBeginRowNumber + 1)),
                stringCellCoordinateBegin.substring(0, indexBeginRowNumber)
        );
        this.cellCoordinateBegin = cellCoordinateBegin;
        this.cellCoordinateEnd = cellCoordinateEnd;
    }

    public Integer getRowCount() {
        return cellCoordinateEnd.getRowNumber() - cellCoordinateBegin.getRowNumber() + 1;
    }

    public Integer getColumnCount() {
        return CellCoordinate.getColumnNumberByName(cellCoordinateEnd.getColumnName()) - CellCoordinate.getColumnNumberByName(cellCoordinateBegin.getColumnName()) + 1;
    }
}
