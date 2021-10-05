package ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.dto;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.NumericalPokValue;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IterationGroupFormMarkDto {
    NumericalPokValue numericalPokValue;
    List<IterationGroupHandbookToFormMarkParamPokDto> listIterationGroupHandbookToFormMarkParamPok = new ArrayList<IterationGroupHandbookToFormMarkParamPokDto>();
}
