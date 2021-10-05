package ru.rzd.discor.diskorReportConstReportBack.models.currentProject.mode;

public enum ProjectMode {
    constructor("constructor", "Режим конструктора", 0, 0),
    operator("operator", "Режим оператора", 1, 1);

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

    private Integer objGroupType;
    public Integer getObjGroupType () {
        return this.objGroupType;
    }

    ProjectMode(String techName, String realName, Integer id, Integer objGroupType) {
        this.techName = techName;
        this.realName = realName;
        this.id = id;
        this.objGroupType = objGroupType;
    }
}
