package ru.rzd.discor.diskorReportConstReportBack.models.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ParamPok {
    public Integer id;
    public Integer fkPok;
    public Integer fkHandbook;
    public Handbook handbook;
    public String codeParam;
    public String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
    public Date dateCreate;
    public String corTip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
    public Date dateDelete;
}
