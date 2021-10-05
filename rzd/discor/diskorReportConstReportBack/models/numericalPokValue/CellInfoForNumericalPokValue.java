package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CellInfoForNumericalPokValue {
    private String listName;
    private Integer rowNumber;
    private String columnNumber;

    public static boolean equals(
            CellInfoForNumericalPokValue object1,
            CellInfoForNumericalPokValue object2
    ) {
        return (
                (
                        object1 == null
                                && object2 == null
                ) || (
                        object1 != null
                                && object2 != null
                                && object1.getListName().equals(object2.getListName())
                                && object1.getRowNumber().equals(object2.getRowNumber())
                                && object1.getColumnNumber().equals(object2.getColumnNumber())
                )
        );
    }
}