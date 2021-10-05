package ru.rzd.discor.diskorReportConstReportBack.models.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompAsToPokValue {
    public Integer id;
    public String name;
    public Integer fkPokValue;
    public PokValue pokValue;
    public Integer fkCompAsSourceInf;
    public CompAsSourceInf compAsSourceInf;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Moscow")
    public Date dateBegin;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Moscow")
    public Date dateEnd;
    public String regulationcalc;
    public Integer pos;
    public Integer priority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
    public Date dateCreate;
    public String corTip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
    public Date dateDelete;
}
