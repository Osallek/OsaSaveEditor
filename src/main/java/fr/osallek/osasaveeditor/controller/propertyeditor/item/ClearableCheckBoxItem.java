package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.control.ClearableCheckBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;

import java.util.Optional;
import java.util.function.BooleanSupplier;

public class ClearableCheckBoxItem implements CustomItem<Boolean> {

    private final String category;

    private final String name;

    private final String description;

    private final ClearableCheckBox checkBox;

    private final BooleanProperty editable;

    public ClearableCheckBoxItem(String category, String name) {
        this(category, name, null, null, new SimpleBooleanProperty(true));
    }

    public ClearableCheckBoxItem(String category, String name, String description, BooleanSupplier clearSupplier, BooleanProperty editable) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.checkBox = new ClearableCheckBox(clearSupplier);
        this.editable = editable;
        this.checkBox.managedProperty().bind(this.checkBox.visibleProperty());
    }

    @Override
    public Class<?> getType() {
        return ClearableCheckBoxItem.class;
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
        return this.checkBox.getValue();
    }

    @Override
    public void setValue(Boolean value) {
        this.checkBox.setValue((value));
    }

    @Override
    public Optional<ObservableValue<Boolean>> getObservableValue() {
        return Optional.ofNullable(this.checkBox.getCheckBox().selectedProperty());
    }

    @Override
    public BooleanProperty isEditable() {
        return this.editable;
    }

    @Override
    public BooleanProperty isVisible() {
        return this.checkBox.visibleProperty();
    }

    public ClearableCheckBox getCheckBox() {
        return checkBox;
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public boolean isSelected() {
        return this.checkBox.getValue();
    }

    public void setSupplier(BooleanSupplier clearSupplier) {
        if (clearSupplier != null) {
            this.checkBox.setSupplier(clearSupplier);
        }
    }

    public BooleanProperty selectedProperty() {
        return this.checkBox.selectedProperty();
    }
}
