package ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.dto;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.enum_.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IterationGroupHandbookDto {
    private String id;
    private String fkIterationGroup;
    private Integer idHandbook;
    private String name;
    private String cellAddress;
    private IterationGroupHandbookDisplayedDataType displayedDataType;
    private IterationGroupHandbookSortPosition sortPosition;
    private IterationGroupHandbookSortDataType sortDataType;
    private IterationGroupHandbookSortOrder sortOrder;
    private List<Integer> sortListHandbookRowCode = new ArrayList<Integer>();
    private Boolean filterPresence;
    private IterationGroupHandbookFilterType filterType;
    private List<Integer> filterList = new ArrayList<Integer>();
}
