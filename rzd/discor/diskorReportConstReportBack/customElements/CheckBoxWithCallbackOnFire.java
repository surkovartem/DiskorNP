package ru.rzd.discor.diskorReportConstReportBack.customElements;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.dto.IterationGroupHandbookDto;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.NumericalPokValue;

import java.util.ArrayList;
import java.util.function.Consumer;

public class CheckBoxWithCallbackOnFire extends CheckBox {
    public void setSelectedCustom(Boolean value, NumericalPokValue numericalPokValue, IterationGroupHandbookDto iterationGroupHandbookDto) {
        for (EventHandler<ActionEvent> actionEventHandler: listActionEventHandler) {
            removeEventHandler(ActionEvent.ACTION, actionEventHandler);
        }
        setSelected(value);
        if (callbackForFireCustomCustom != null) {
            callbackForFireCustomCustom.accept(numericalPokValue);
        }
        if (callbackForFireCustomCustom2 != null) {
            callbackForFireCustomCustom2.accept(iterationGroupHandbookDto);
        }
        for (EventHandler<ActionEvent> actionEventHandler: listActionEventHandler) {
            addEventHandler(ActionEvent.ACTION, actionEventHandler);
        }
    }
    public void setSelectedCustom(Boolean value) {
        setSelectedCustom(value,null, null);
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
    Consumer<IterationGroupHandbookDto> callbackForFireCustomCustom2 = null;
    public void setCallBackForSelectCustom(Consumer<NumericalPokValue> callback, Consumer<IterationGroupHandbookDto> callback2) {
        this.callbackForFireCustomCustom = callback;
        this.callbackForFireCustomCustom2 = callback2;
    }
}
