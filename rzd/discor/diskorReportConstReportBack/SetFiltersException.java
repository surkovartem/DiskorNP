package ru.rzd.discor.diskorReportConstReportBack;

public class SetFiltersException extends RuntimeException {
    public SetFiltersException(String message, Throwable e) {
        super(message, e);
    }
    public SetFiltersException(String message) {
        super(message);
    }
    public SetFiltersException(Throwable e) {
        super(e);
    }
}
