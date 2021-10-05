package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.core.AsSourceInformation;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AsSourceInformationForNumericalPokValue {
    private Integer id;
    private String code;
    private String name;

    public static AsSourceInformationForNumericalPokValue get(AsSourceInformation element) {
        if (element == null) {
            return null;
        }
        AsSourceInformationForNumericalPokValue elementForNumericalPokValue = new AsSourceInformationForNumericalPokValue();
        elementForNumericalPokValue.setId(element.getId());
        elementForNumericalPokValue.setCode(element.getCode());
        elementForNumericalPokValue.setName(element.getName());
        return elementForNumericalPokValue;
    }

    public static boolean equals(
            AsSourceInformationForNumericalPokValue object1,
            AsSourceInformationForNumericalPokValue object2
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
