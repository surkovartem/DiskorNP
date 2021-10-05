package ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue;

public enum PriorityTypeForNumericalPokValue {
    priorityAsAndComp("priorityAsAndComp", "По источнику и компоненте", 1),
    priorityAs("priorityAs", "По источнику", 2),
    withoutPriority("withoutPriority", "Выбор компоненты с максимальным приоритетом", 3);

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

    PriorityTypeForNumericalPokValue(String techName, String realName, Integer id) {
        this.techName = techName;
        this.realName = realName;
        this.id = id;
    }
}