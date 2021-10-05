package ru.rzd.discor.diskorReportConstReportBack.customElements;

import javafx.scene.control.Alert;

public class AlertWithFixedMessageLength extends Alert {

    public AlertWithFixedMessageLength(AlertType alertType, String contentText) {
        super(alertType, contentText != null ? contentText.substring(0, Math.min(contentText.length(), 300)) : "");
    }
}
