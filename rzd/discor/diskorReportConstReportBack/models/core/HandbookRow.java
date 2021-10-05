package ru.rzd.discor.diskorReportConstReportBack.models.core;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HandbookRow {
    private Integer id;
    private Integer code;
    private String name;
    private Integer fkZone;
}
