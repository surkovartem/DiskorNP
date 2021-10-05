package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.core.HandbookRow;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HandbookRowForNumericalPokValue {
    private Integer id;
    private Integer code;
    private String name;

    public static HandbookRowForNumericalPokValue get(HandbookRow element) {
        if (element == null) {
            return null;
        }
        HandbookRowForNumericalPokValue elementForNumericalPokValue = new HandbookRowForNumericalPokValue();
        elementForNumericalPokValue.setId(element.getId());
        elementForNumericalPokValue.setCode(element.getCode());
        elementForNumericalPokValue.setName(element.getName());
        return elementForNumericalPokValue;
    }

    public static boolean equals(
            ParamPokInfoForNumericalPokValue object1,
            ParamPokInfoForNumericalPokValue object2
    ) {
        if (!HandbookForNumericalPokValue.equals(object1, object2)) {
            return false;
        }
        return HandbookRowForNumericalPokValue.equals(object1.getHandbookRow(), object2.getHandbookRow());
    }

    public static boolean equals(
            HandbookRowForNumericalPokValue object1,
            HandbookRowForNumericalPokValue object2
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
