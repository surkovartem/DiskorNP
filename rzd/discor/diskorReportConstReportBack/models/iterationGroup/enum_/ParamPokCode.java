package ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.enum_;

public enum ParamPokCode {
    p1("p1", "Параметр 1", 1),
    p2("p2", "Параметр 2", 2),
    p3("p3", "Параметр 3", 3),
    p4("p4", "Параметр 4", 4),
    p5("p5", "Параметр 5", 5),
    p6("p6", "Параметр 6", 6),
    p7("p7", "Параметр 7", 7),
    p8("p8", "Параметр 8", 8),
    p9("p9", "Параметр 9", 9),
    p10("p10", "Параметр 10", 10);

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

    ParamPokCode(String techName, String realName, Integer id) {
        this.techName = techName;
        this.realName = realName;
        this.id = id;
    }
}

