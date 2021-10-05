package ru.rzd.discor.diskorReportConstReportBack.models.currentProject.chooseObjGroupController;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.reportRepo.ObjGroup;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChooseObjGroupControllerObserverOutput {
    private ObjGroup objGroup;
    private String formName;
}
