package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.core.CompAsToPokValue;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompAsToPokValueForNumericalPokValue {
    private Integer id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Moscow")
    private Date dateBegin;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Moscow")
    private Date dateEnd;
    private Integer priority;

    public static CompAsToPokValueForNumericalPokValue get(CompAsToPokValue element) {
        if (element == null) {
            return null;
        }
        CompAsToPokValueForNumericalPokValue elementForNumericalPokValue = new CompAsToPokValueForNumericalPokValue();
        elementForNumericalPokValue.setId(element.getId());
        elementForNumericalPokValue.setName(element.getName());
        elementForNumericalPokValue.setDateBegin(element.getDateBegin());
        elementForNumericalPokValue.setDateEnd(element.getDateEnd());
        elementForNumericalPokValue.setPriority(element.getPriority());
        return elementForNumericalPokValue;
    }

    public static boolean equals(
            CompAsToPokValueForNumericalPokValue object1,
            CompAsToPokValueForNumericalPokValue object2
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
