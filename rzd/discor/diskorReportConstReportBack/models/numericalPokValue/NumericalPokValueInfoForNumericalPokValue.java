package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NumericalPokValueInfoForNumericalPokValue {
    private Double value;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Moscow")
    private Date dateReport;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Europe/Moscow")
    private Date dateWrite;
    private AsSourceInformationForNumericalPokValue asSourceInformation = new AsSourceInformationForNumericalPokValue();
    private CompAsSourceInfForNumericalPokValue compAsSourceInf = new CompAsSourceInfForNumericalPokValue();
    private CompAsToPokValueForNumericalPokValue compAsToPokValue = new CompAsToPokValueForNumericalPokValue();
    private String integrationMsgId;
    private Integer calculationId;
    private String error;
}
