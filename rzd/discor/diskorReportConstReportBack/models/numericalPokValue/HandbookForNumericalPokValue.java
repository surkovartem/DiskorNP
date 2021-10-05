package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.core.Handbook;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HandbookForNumericalPokValue {
    private Integer id;
    private String name;

    public static HandbookForNumericalPokValue get(Handbook element) {
        if (element == null) {
            return null;
        }
        HandbookForNumericalPokValue elementForNumericalPokValue = new HandbookForNumericalPokValue();
        elementForNumericalPokValue.setId(element.getId());
        elementForNumericalPokValue.setName(element.getHandbookName());
        return elementForNumericalPokValue;
    }

    public static boolean equals(
            ParamPokInfoForNumericalPokValue object1,
            ParamPokInfoForNumericalPokValue object2
    ) {
        if (!(object1 != null && object2 != null)) {
            return false;
        }
        return HandbookForNumericalPokValue.equals(object1.getHandbook(), object2.getHandbook());
    }

    public static boolean equals(
            HandbookForNumericalPokValue object1,
            HandbookForNumericalPokValue object2
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
