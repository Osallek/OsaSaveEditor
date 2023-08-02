package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import java.util.Optional;

public class CheckBoxItem implements CustomItem<Boolean> {

    private final String category;

    private final String name;

    private final String description;

    private BooleanProperty value;

    private final BooleanProperty editable;

    public CheckBoxItem(String category, String name, boolean value) {
        this(category, name, value, null);
    }

    public CheckBoxItem(String category, String name, boolean value, String description) {
        this(category, name, value, description, new SimpleBooleanProperty(true));
    }

    public CheckBoxItem(String category, String name, boolean value, String description, BooleanProperty editable) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.value = new SimpleBooleanProperty(value);
        this.editable = editable;
    }

    @Override
    public Class<?> getType() {
        return boolean.class;
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
    public Boolean getValue() {
        return this.value.get();
    }

    @Override
    public void setValue(Boolean value) {
        this.value.set(value);
    }

    @Override
    public Optional<ObservableValue<Boolean>> getObservableValue() {
        return Optional.of(this.value);
    }

    @Override
    public BooleanProperty isEditable() {
        return this.editable;
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public boolean isSelected() {
        return this.value.get();
    }
}
