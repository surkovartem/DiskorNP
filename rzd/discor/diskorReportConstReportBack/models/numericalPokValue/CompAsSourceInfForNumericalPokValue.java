package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.core.CompAsSourceInf;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompAsSourceInfForNumericalPokValue {
    private Integer id;
    private String code;
    private String name;

    public static CompAsSourceInfForNumericalPokValue get(CompAsSourceInf element) {
        if (element == null) {
            return null;
        }
        CompAsSourceInfForNumericalPokValue elementForNumericalPokValue = new CompAsSourceInfForNumericalPokValue();
        elementForNumericalPokValue.setId(element.getId());
        elementForNumericalPokValue.setCode(element.getCode());
        elementForNumericalPokValue.setName(element.getName());
        return elementForNumericalPokValue;
    }

    public static boolean equals(
            CompAsSourceInfForNumericalPokValue object1,
            CompAsSourceInfForNumericalPokValue object2
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
