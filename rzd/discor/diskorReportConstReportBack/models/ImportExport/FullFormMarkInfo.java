package ru.rzd.discor.diskorReportConstReportBack.models.ImportExport;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.NumericalPokValue;

import java.util.List;

//Данные для записи в zip
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FullFormMarkInfo {
    List<NumericalPokValue> listNumericalPokValue;
    byte[] fileByteArray;
}
