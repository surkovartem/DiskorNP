package ru.rzd.discor.diskorReportConstReportBack.models.currentProject.chooseFormController;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.reportRepo.Form;
import ru.rzd.discor.diskorReportConstReportBack.models.reportRepo.ObjGroup;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChooseFormControllerObserverOutput {
    private ObjGroup objGroup;
    private Form form;
}
