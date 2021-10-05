package ru.rzd.discor.diskorReportConstReportBack.models.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Handbook {
    private Integer id;
    private String nameColHandbookCode;
    private String nameColHandbookName;
    private String technicalName;
    private Integer fkGroupHandbook;
    private Integer fkGroupCommonHandbook;
    private String handbookName;
    private Integer codeDor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
    private Date dateCreate;
    private String corTip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
    private Date dateDelete;
}
