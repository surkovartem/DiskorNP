package ru.rzd.discor.diskorReportConstReportBack.customElements;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.dto.IterationGroupHandbookDto;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.NumericalPokValue;

import java.util.ArrayList;
import java.util.function.Consumer;

public class SimpleObjectPropertyWithChangeListenersArray<T> extends SimpleObjectProperty<T> {
    ArrayList<ChangeListener<T>> listChangeListener = new ArrayList<>();
    public void addChangeListener(ChangeListener<T> changeListener) {
        listChangeListener.add(changeListener);
        addListener(changeListener);
    }
    public void removeChangeListener(ChangeListener<T> changeListener) {
        listChangeListener.remove(changeListener);
        removeListener(changeListener);
    }
    public void setValueCustom(T value, NumericalPokValue numericalPokValueToSetFilters, IterationGroupHandbookDto iterationGroupHandbookDto) {
        for (ChangeListener<T> changeListener: listChangeListener) {
            removeListener(changeListener);
        }
        setValue(value);
        if (callbackForSetValueCustom != null) {
            callbackForSetValueCustom.accept(numericalPokValueToSetFilters);
        }
        if (callbackForSetValueCustom2 != null) {
            callbackForSetValueCustom2.accept(iterationGroupHandbookDto);
        }
        for (ChangeListener<T> changeListener: listChangeListener) {
            addListener(changeListener);
        }
    }
    public void setValueCustom(T value) {
        setValueCustom(value, null, null);
    }

    //Function не подходит, так как возвращает значение. А моя функция возвращает ничего.
    //Поэтому выбор пал на Consumer
    Consumer<NumericalPokValue> callbackForSetValueCustom = null;
    Consumer<IterationGroupHandbookDto> callbackForSetValueCustom2 = null;
    public void setCallBackForSetValueCustom(Consumer<NumericalPokValue> callback, Consumer<IterationGroupHandbookDto> callback2) {
        this.callbackForSetValueCustom = callback;
        this.callbackForSetValueCustom2 = callback2;
    }
}
