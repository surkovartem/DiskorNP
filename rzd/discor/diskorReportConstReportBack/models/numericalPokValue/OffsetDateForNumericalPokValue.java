package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.core.OffsetDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OffsetDateForNumericalPokValue {
    private Integer id;
    private String code;
    private String name;

    public static OffsetDateForNumericalPokValue get(OffsetDate element) {
        if (element == null) {
            return null;
        }
        OffsetDateForNumericalPokValue elementForNumericalPokValue = new OffsetDateForNumericalPokValue();
        elementForNumericalPokValue.setId(element.getId());
        elementForNumericalPokValue.setCode(element.getCode());
        elementForNumericalPokValue.setName(element.getName());
        return elementForNumericalPokValue;
    }

    public static boolean equals(
            OffsetDateForNumericalPokValue object1,
            OffsetDateForNumericalPokValue object2
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
