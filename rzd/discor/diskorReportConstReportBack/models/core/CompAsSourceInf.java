package ru.rzd.discor.diskorReportConstReportBack.models.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompAsSourceInf {
    public Integer id;
    public Integer fkAsSourceInf;
    public Integer fkFunction;
    public String code;
    public String name;
    public String type;
    public String regulationcalc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
    public Date dateCreate;
    public String corTip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
    public Date dateDelete;
}
