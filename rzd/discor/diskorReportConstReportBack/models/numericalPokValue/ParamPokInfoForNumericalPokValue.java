package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ParamPokInfoForNumericalPokValue {
    private ParamPokForNumericalPokValue paramPok = new ParamPokForNumericalPokValue();
    private HandbookForNumericalPokValue handbook = new HandbookForNumericalPokValue();
    private HandbookRowForNumericalPokValue handbookRow = new HandbookRowForNumericalPokValue();
}
