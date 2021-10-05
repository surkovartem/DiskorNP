package ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.enum_;

public enum IterationGroupHandbookDisplayedDataType {
    code("code", "Код", 1),
    name("name", "Имя", 2);

    private String techName;
    public String getTechName () {
        return this.techName;
    }

    private String realName;
    public String getRealName () {
        return this.realName;
    }

    private Integer id;
    public Integer getId () {
        return this.id;
    }

    IterationGroupHandbookDisplayedDataType(String techName, String realName, Integer id) {
        this.techName = techName;
        this.realName = realName;
        this.id = id;
    }
}
