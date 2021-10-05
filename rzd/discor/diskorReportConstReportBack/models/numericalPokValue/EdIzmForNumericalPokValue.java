package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.core.UnitMeasurement;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EdIzmForNumericalPokValue {
    private Integer id;
    private String code;
    private String name;

    public static EdIzmForNumericalPokValue get(UnitMeasurement element) {
        if (element == null) {
            return null;
        }
        EdIzmForNumericalPokValue elementForNumericalPokValue = new EdIzmForNumericalPokValue();
        elementForNumericalPokValue.setId(element.getId());
        elementForNumericalPokValue.setCode(element.getCode());
        elementForNumericalPokValue.setName(element.getName());
        return elementForNumericalPokValue;
    }

    public static boolean equals(
            EdIzmForNumericalPokValue object1,
            EdIzmForNumericalPokValue object2
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
