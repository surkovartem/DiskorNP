package ru.rzd.discor.diskorReportConstReportBack.customElements;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.NumericalPokValue;

import java.util.ArrayList;
import java.util.function.Consumer;

public class RadioButtonWithCallbackOnFire extends RadioButton {
    public void fireCustom (NumericalPokValue numericalPokValue) {
        for (EventHandler<ActionEvent> actionEventHandler: listActionEventHandler) {
            removeEventHandler(ActionEvent.ACTION, actionEventHandler);
        }
        fire();
        if (callbackForFireCustomCustom != null) {
            callbackForFireCustomCustom.accept(numericalPokValue);
        }
        for (EventHandler<ActionEvent> actionEventHandler: listActionEventHandler) {
            addEventHandler(ActionEvent.ACTION, actionEventHandler);
        }
    }
    public void fireCustom () {
        fireCustom(null);
    }

    ArrayList<EventHandler<ActionEvent>> listActionEventHandler = new ArrayList<EventHandler<ActionEvent>>();
    public void addActionEventHandler(EventHandler<ActionEvent> actionEventEventHandler) {
        listActionEventHandler.add(actionEventEventHandler);
        addEventHandler(ActionEvent.ACTION, actionEventEventHandler);
    }
    public void removeActionEventHandler(EventHandler<ActionEvent> actionEventEventHandler) {
        listActionEventHandler.remove(actionEventEventHandler);
        removeEventHandler(ActionEvent.ACTION, actionEventEventHandler);
    }

    //Function не подходит, так как возвращает значение. А моя функция возвращает ничего.
    //Поэтому выбор пал на Consumer
    Consumer<NumericalPokValue> callbackForFireCustomCustom = null;
    public void setCallBackForSelectCustom(Consumer<NumericalPokValue> callback) {
        this.callbackForFireCustomCustom = callback;
    }
}
