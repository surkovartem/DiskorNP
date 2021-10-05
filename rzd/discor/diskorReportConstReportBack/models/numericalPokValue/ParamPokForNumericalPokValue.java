package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import lombok.*;
import ru.rzd.discor.diskorReportConstReportBack.models.core.ParamPok;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ParamPokForNumericalPokValue {
    private Integer id;
    private String name;

    public static ParamPokForNumericalPokValue get(ParamPok element) {
        if (element == null) {
            return null;
        }
        ParamPokForNumericalPokValue elementForNumericalPokValue = new ParamPokForNumericalPokValue();
        elementForNumericalPokValue.setId(element.getId());
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
        return ParamPokForNumericalPokValue.equals(object1.getParamPok(), object2.getParamPok());
    }

    public static boolean equals(
            ParamPokForNumericalPokValue object1,
            ParamPokForNumericalPokValue object2
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

    public static boolean equalsByName(
            ParamPokInfoForNumericalPokValue object1,
            ParamPokInfoForNumericalPokValue object2
    ) {
        if (!HandbookForNumericalPokValue.equals(object1, object2)) {
            return false;
        }
        return ParamPokForNumericalPokValue.equalsByName(object1.getParamPok(), object2.getParamPok());
    }

    public static boolean equalsByName(
            ParamPokForNumericalPokValue object1,
            ParamPokForNumericalPokValue object2
    ) {
        return (
                (
                        object1 == null
                                && object2 == null
                ) || (
                        object1 != null
                                && object2 != null
                                && object1.getName().equals(object2.getName())
                )
        );
    }
}
