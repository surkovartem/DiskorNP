package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.core.PokValue;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PokValueForNumericalPokValue {
    private Integer id;

    public static PokValueForNumericalPokValue get(PokValue element) {
        if (element == null) {
            return null;
        }
        PokValueForNumericalPokValue elementForNumericalPokValue = new PokValueForNumericalPokValue();
        elementForNumericalPokValue.setId(element.getId());
        return elementForNumericalPokValue;
    }

    public static boolean equals(
            PokValueForNumericalPokValue object1,
            PokValueForNumericalPokValue object2
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
