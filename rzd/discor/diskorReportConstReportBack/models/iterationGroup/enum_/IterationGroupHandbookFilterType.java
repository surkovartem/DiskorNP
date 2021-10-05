package ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.enum_;

public enum IterationGroupHandbookFilterType {
    IN("IN", "Выбирает при фильтрации только выбранные элементы", 1),
    NOT_IN("NOT IN", "Выбирает при фильтрации все элементы, кроме выбранных", 2);

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

    IterationGroupHandbookFilterType(String techName, String realName, Integer id) {
        this.techName = techName;
        this.realName = realName;
        this.id = id;
    }
}
