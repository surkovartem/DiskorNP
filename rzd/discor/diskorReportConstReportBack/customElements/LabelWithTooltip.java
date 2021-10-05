package ru.rzd.discor.diskorReportConstReportBack.customElements;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

public class LabelWithTooltip extends Label {

    public LabelWithTooltip() {
        super();
        this.setTooltip(new Tooltip());
        this.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (getText() == null || getText().equals("")) {
                    getTooltip().setText(null);
                }
                getTooltip().setText(getText());
            }
        });
    }
}
