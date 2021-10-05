package ru.rzd.discor.diskorReportConstReportBack.models.reportRepo;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthorCreate {
    public Integer id;
    public String name;
    public String organizationName;
    public String secondName;
    public String surName;
    public String workPosition;
}
