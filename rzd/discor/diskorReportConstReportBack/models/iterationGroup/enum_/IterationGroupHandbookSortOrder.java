package ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.enum_;

public enum IterationGroupHandbookSortOrder {
    ASC("ASC", "По возрастанию", 1),
    DESC("DESC", "По убыванию", 2);

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

    IterationGroupHandbookSortOrder(String techName, String realName, Integer id) {
        this.techName = techName;
        this.realName = realName;
        this.id = id;
    }
}
