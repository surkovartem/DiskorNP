package ru.rzd.discor.diskorReportConstReportBack.models.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GroupHandbook {
    public Integer id;
    public Integer fkGroupHandbook;
    private List<GroupHandbook> listGroupHandbook = new ArrayList<>();
    public String name;
    public Integer codeDor;
    public Integer grantInclude;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
    private Date dateCreate;
    private String corTip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
    private Date dateDelete;

}
