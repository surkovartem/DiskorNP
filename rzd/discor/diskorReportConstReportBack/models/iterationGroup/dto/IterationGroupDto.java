package ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.dto;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.enum_.IterationGroupHandbookSortPosition;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IterationGroupDto {
    private String id;
    private String fkForm;
    private List<IterationGroupHandbookDto> listIterationGroupHandbook = new ArrayList<IterationGroupHandbookDto>();
    private List<IterationGroupFormMarkDto> listIterationGroupFormMark = new ArrayList<IterationGroupFormMarkDto>();
    private String name;
    private String listName;
    private String cellAddressRange;

    public IterationGroupHandbookDto getIterationGroupHandbookBySortPosition(IterationGroupHandbookSortPosition iterationGroupHandbookSortPosition) {
        if (iterationGroupHandbookSortPosition == null) {
            return null;
        }
        for (IterationGroupHandbookDto iterationGroupHandbookDto : listIterationGroupHandbook) {
            if (iterationGroupHandbookDto.getSortPosition().equals(iterationGroupHandbookSortPosition)) {
                return iterationGroupHandbookDto;
            }
        }
        return null;
    }
}
