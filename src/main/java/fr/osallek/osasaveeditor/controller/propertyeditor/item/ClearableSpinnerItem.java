package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.control.ClearableSpinner;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;

import java.util.Optional;
import java.util.function.Supplier;

public class ClearableSpinnerItem<T> implements CustomItem<T> {

    private final String category;

    private final String name;

    private final ClearableSpinner<T> spinner;

    private final BooleanProperty editable;


    public ClearableSpinnerItem(String category, String name, ClearableSpinner<T> spinner) {
        this(category, name, spinner, new SimpleBooleanProperty(true));
    }

    public ClearableSpinnerItem(String category, String name, ClearableSpinner<T> spinner, BooleanProperty editable) {
        this.category = category;
        this.name = name;
        this.spinner = spinner;
        this.editable = editable;
        this.spinner.managedProperty().bind(this.spinner.visibleProperty());
    }

    @Override
    public Class<?> getType() {
        return ClearableSpinnerItem.class;
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
        return this.name;
    }

    @Override
    public T getValue() {
        return this.spinner.getValue();
    }

    @Override
    public void setValue(T value) {
        this.spinner.setValue(value);
    }

    @Override
    public Optional<ObservableValue<T>> getObservableValue() {
        return Optional.of(this.spinner.getSpinner().valueProperty());
    }

    @Override
    public BooleanProperty isEditable() {
        return this.editable;
    }

    @Override
    public BooleanProperty isVisible() {
        return this.spinner.visibleProperty();
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public ClearableSpinner<T> getSpinner() {
        return this.spinner;
    }

    public ObjectProperty<T> valueProperty() {
        return this.spinner.getSpinner().getValueFactory().valueProperty();
    }

    public void setSupplier(Supplier<T> clearSupplier) {
        if (clearSupplier != null) {
            this.spinner.setSupplier(clearSupplier);
        }
    }

    public void setMax(T max) {
        this.spinner.setMax(max);
    }

    public void setMin(T max) {
        this.spinner.setMax(max);
    }
}
