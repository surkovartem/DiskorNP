package ru.rzd.discor.diskorReportConstReportBack.models.reportRepo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.NumericalPokValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Form {
    private String id;
    private String idFormChain;
    private String fkObjGroup;
    private Integer idDor;
    private Integer pos;
    private String name;
    private String description;
    private String typeForm;
    private Integer idAuthor;
    private Boolean activeTip;
    private Boolean visibleTip;
    private Integer privilege;
    private Integer version;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Europe/Moscow")
    private Date dateStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Europe/Moscow")
    private Date dateEnd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Europe/Moscow")
    private Date dateCreate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Europe/Moscow")
    private Date dateDelete;
    private String corTip;
    private ArrayList<NumericalPokValue> listNumericalPokValue = new ArrayList<>();

    public static boolean equals(
            Form object1,
            Form object2
    ) {
        return (
                (
                        object1 == null
                                && object2 == null
                ) || (
                        object1 != null
                                && object2 != null
                                && object1.getId().equals(object2.getId())
                )
        );
    }
}