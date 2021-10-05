package ru.rzd.discor.diskorReportConstReportBack.models.currentProject.cellFormMarkInfo;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.dto.IterationGroupDto;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.NumericalPokValue;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CellFormMarkInfo {
    private NumericalPokValue numericalPokValue;
    private IterationGroupDto iterationGroupDto;
}
