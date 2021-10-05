package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NumericalPokValue {
    private NumericalPokValueInfoForNumericalPokValue numericalPokValueInfo = new NumericalPokValueInfoForNumericalPokValue();
    private AttributesForNumericalPokValue attributes = new AttributesForNumericalPokValue();
    private CellInfoForNumericalPokValue cellInfo = new CellInfoForNumericalPokValue();

    public static NumericalPokValue copy(NumericalPokValue numericalPokValue) {
        if (numericalPokValue == null) {
            return null;
        }
        return new NumericalPokValue(
                numericalPokValue.getNumericalPokValueInfo(),
                numericalPokValue.getAttributes(),
                numericalPokValue.getCellInfo()
        );
    }
}
