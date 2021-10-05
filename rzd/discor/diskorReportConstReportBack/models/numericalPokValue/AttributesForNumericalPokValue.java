package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AttributesForNumericalPokValue {
    private PriorityTypeForNumericalPokValue priority;
    private AsSourceInformationForNumericalPokValue asSourceInformation = new AsSourceInformationForNumericalPokValue();
    private CompAsSourceInfForNumericalPokValue compAsSourceInf = new CompAsSourceInfForNumericalPokValue();
    private CompAsToPokValueForNumericalPokValue compAsToPokValue = new CompAsToPokValueForNumericalPokValue();
    private OffsetDateForNumericalPokValue offsetDate = new OffsetDateForNumericalPokValue();
    private PokForNumericalPokValue pok = new PokForNumericalPokValue();
    private PokValueForNumericalPokValue pokValue = new PokValueForNumericalPokValue();
    private PermissibleValuePokForNumericalPokValue permissibleValuePok = new PermissibleValuePokForNumericalPokValue();
    private EdIzmForNumericalPokValue edIzm = new EdIzmForNumericalPokValue();
    private ParamPokInfoForNumericalPokValue p1 = new ParamPokInfoForNumericalPokValue();
    private ParamPokInfoForNumericalPokValue p2 = new ParamPokInfoForNumericalPokValue();
    private ParamPokInfoForNumericalPokValue p3 = new ParamPokInfoForNumericalPokValue();
    private ParamPokInfoForNumericalPokValue p4 = new ParamPokInfoForNumericalPokValue();
    private ParamPokInfoForNumericalPokValue p5 = new ParamPokInfoForNumericalPokValue();
    private ParamPokInfoForNumericalPokValue p6 = new ParamPokInfoForNumericalPokValue();
    private ParamPokInfoForNumericalPokValue p7 = new ParamPokInfoForNumericalPokValue();
    private ParamPokInfoForNumericalPokValue p8 = new ParamPokInfoForNumericalPokValue();
    private ParamPokInfoForNumericalPokValue p9 = new ParamPokInfoForNumericalPokValue();
    private ParamPokInfoForNumericalPokValue p10 = new ParamPokInfoForNumericalPokValue();
    private Integer hourReport;
}
