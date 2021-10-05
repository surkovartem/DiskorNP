package ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.dto;


import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.enum_.ParamPokCode;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IterationGroupHandbookToFormMarkParamPokDto {
    private String id;
    private String fkIterationGroupHandbook;
    private IterationGroupHandbookDto iterationGroupHandbook;
    private String fkFormMark;
    private ParamPokCode formMarkParamPokCode;
}
