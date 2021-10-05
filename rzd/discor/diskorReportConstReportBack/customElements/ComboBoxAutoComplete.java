package ru.rzd.discor.diskorReportConstReportBack.customElements;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;
import ru.rzd.discor.diskorReportConstReportBack.models.iterationGroup.dto.IterationGroupHandbookDto;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.NumericalPokValue;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Uses a combobox tooltip as the suggestion for auto complete and updates the
 * combo box itens accordingly <br />
 * It does not work with space, space and escape cause the combobox to hide and
 * clean the filter ... Send me a PR if you want it to work with all characters
 * -> It should be a custom controller - I KNOW!
 *
 * @param <T>
 * @author wsiqueir
 */
public class ComboBoxAutoComplete<T> extends ComboBox<T> {

    String filter = "";
    private ObservableList<T> originalItems;

    public ComboBoxAutoComplete() {
        super();
        this.setTooltip(new Tooltip());
        this.setOnKeyPressed(this::handleOnKeyPressed);
        this.setOnHidden(this::handleOnHiding);
    }

    public void setItemsCustom(ObservableList<T> value) {
        setItems(value);
        originalItems = FXCollections.observableArrayList(this.getItems());
    }

    public void handleOnKeyPressed(KeyEvent e) {
        ObservableList<T> filteredList = FXCollections.observableArrayList();
        KeyCode code = e.getCode();

        if (code.isLetterKey()) {
            filter += e.getText();
        }
        if (code == KeyCode.BACK_SPACE && filter.length() > 0) {
            filter = filter.substring(0, filter.length() - 1);
            this.getItems().setAll(originalItems);
        }
        if (code == KeyCode.ESCAPE) {
            filter = "";
        }
        if (filter.length() == 0) {
            filteredList = originalItems;
            this.getTooltip().hide();
        } else {
            Stream<T> itens = this.getItems().stream();
            String txtUsr = filter.toString().toLowerCase();
            itens.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
            this.getTooltip().setText(txtUsr);
            Window stage = this.getScene().getWindow();
            double posX = stage.getX() + this.getBoundsInParent().getMinX();
            double posY = stage.getY() + this.getBoundsInParent().getMinY();
            this.getTooltip().show(stage, posX, posY);
            this.show();
        }
        this.getItems().setAll(filteredList);
    }

    public void handleOnHiding(Event e) {
        filter = "";
        this.getTooltip().hide();
        T s = this.getSelectionModel().getSelectedItem();
        this.getItems().setAll(originalItems);
        this.getSelectionModel().select(s);
    }

    public void clear() {
        setItemsCustom(FXCollections.observableArrayList(new ArrayList<T>()));
        setValueCustom(null);
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
    public void setValueCustom(T value, NumericalPokValue numericalPokValueToSetFilters, IterationGroupHandbookDto iterationGroupHandbookDto) {
        for (EventHandler<ActionEvent> actionEventHandler: listActionEventHandler) {
            removeEventHandler(ActionEvent.ACTION, actionEventHandler);
        }
        setValue(value);
        if (callbackForSetValueCustom != null) {
            callbackForSetValueCustom.accept(numericalPokValueToSetFilters);
        }
        if (callbackForSetValueCustom2 != null) {
            callbackForSetValueCustom2.accept(iterationGroupHandbookDto);
        }
        for (EventHandler<ActionEvent> actionEventHandler: listActionEventHandler) {
            addEventHandler(ActionEvent.ACTION, actionEventHandler);
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
