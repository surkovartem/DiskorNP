package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.core.Pok;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PokForNumericalPokValue {
    private Integer id;
    private String name;

    public static PokForNumericalPokValue get(Pok element) {
        if (element == null) {
            return null;
        }
        PokForNumericalPokValue elementForNumericalPokValue = new PokForNumericalPokValue();
        elementForNumericalPokValue.setId(element.getId());
        elementForNumericalPokValue.setName(element.getName());
        return elementForNumericalPokValue;
    }

    public static boolean equals(
            PokForNumericalPokValue object1,
            PokForNumericalPokValue object2
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
