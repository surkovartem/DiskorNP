package ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.enum_;

public enum IterationGroupHandbookSortPosition {
    pos1("pos1", "1", 1),
    pos2("pos2", "2", 2),
    pos3("pos3", "3", 3),
    pos4("pos4", "4", 4),
    pos5("pos5", "5", 5),
    pos6("pos6", "6", 6),
    pos7("pos7", "7", 7),
    pos8("pos8", "8", 8),
    pos9("pos9", "9", 9),
    pos10("pos10", "10", 10);

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

    IterationGroupHandbookSortPosition(String techName, String realName, Integer id) {
        this.techName = techName;
        this.realName = realName;
        this.id = id;
    }
}

