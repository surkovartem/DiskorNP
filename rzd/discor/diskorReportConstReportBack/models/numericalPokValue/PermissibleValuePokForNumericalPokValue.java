package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.core.PermissibleValuePok;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PermissibleValuePokForNumericalPokValue {
    private Integer id;
    private String code;
    private String name;
    private String typePok;
    private Boolean calc;

    public static PermissibleValuePokForNumericalPokValue get(PermissibleValuePok element) {
        if (element == null) {
            return null;
        }
        PermissibleValuePokForNumericalPokValue elementForNumericalPokValue = new PermissibleValuePokForNumericalPokValue();
        elementForNumericalPokValue.setId(element.getId());
        elementForNumericalPokValue.setCode(element.getCode());
        elementForNumericalPokValue.setName(element.getName());
        elementForNumericalPokValue.setTypePok(element.getTypePok());
        elementForNumericalPokValue.setCalc(element.getCalc());
        return elementForNumericalPokValue;
    }

    public static boolean equals(
            PermissibleValuePokForNumericalPokValue object1,
            PermissibleValuePokForNumericalPokValue object2
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
