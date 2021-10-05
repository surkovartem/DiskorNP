package ru.rzd.discor.diskorReportConstReportBack.models.reportRepo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ObjGroup {
    public Boolean activeTip;
    public AuthorCreate authorCreate;
    public AuthorDelete authorDelete;
    public String corTip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Europe/Moscow")
    public Date dateCreate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Europe/Moscow")
    public Date dateDelete;
    public String fkObjGroup;
    public String id;
    public Integer idAuthorCreate;
    public Integer idAuthorDelete;
    public Integer idDor;
    public ArrayList<ObjGroup> listChildrenObjGroup = new ArrayList<>();
    public String name;
    public String objType;
    public Integer pos;
    public Integer privilege;
}
