package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.control.ClearableDatePicker;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Supplier;

public class ClearableDatePickerItem implements CustomItem<LocalDate> {

    private final String category;

    private final String name;

    private final String description;

    private final ClearableDatePicker datePicker;

    private final BooleanProperty editable;

    public ClearableDatePickerItem(String category, String name, LocalDate date, Supplier<LocalDate> clearSupplier) {
        this(category, name, null, date, clearSupplier, null, null, new SimpleBooleanProperty(true));
    }

    public ClearableDatePickerItem(String category, String name, LocalDate date, Supplier<LocalDate> clearSupplier, LocalDate startDate, LocalDate endDate) {
        this(category, name, null, date, clearSupplier, startDate, endDate, new SimpleBooleanProperty(true));
    }

    public ClearableDatePickerItem(String category, String name, String description, LocalDate date, Supplier<LocalDate> clearSupplier, LocalDate startDate,
                                   LocalDate endDate, BooleanProperty editable) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.datePicker = new ClearableDatePicker(date, clearSupplier, startDate, endDate);
        this.editable = editable;
    }

    @Override
    public Class<?> getType() {
        return ClearableDatePickerItem.class;
    }

    @Override
    public String category() {
        return this.category;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public LocalDate getValue() {
        return this.datePicker.getValue();
    }

    @Override
    public void setValue(LocalDate value) {
        this.datePicker.setValue(value);
    }

    @Override
    public Optional<ObservableValue<LocalDate>> getObservableValue() {
        return Optional.empty();
    }

    @Override
    public BooleanProperty isEditable() {
        return this.editable;
    }

    @Override
    public BooleanProperty isVisible() {
        return this.datePicker.visibleProperty();
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public ClearableDatePicker getDatePicker() {
        return this.datePicker;
    }
}
