package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.control.ClearableCheckBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import java.util.Optional;
import java.util.function.BooleanSupplier;

public class ClearableCheckBoxItem implements CustomItem<Void> {

    private final String category;

    private final String name;

    private final String description;

    private final ClearableCheckBox checkBox;

    private final BooleanProperty editable;

    private final BooleanProperty visible;

    public ClearableCheckBoxItem(String category, String name) {
        this(category, name, null, null, new SimpleBooleanProperty(true), new SimpleBooleanProperty(true));
    }

    public ClearableCheckBoxItem(String category, String name, String description, BooleanSupplier clearSupplier, BooleanProperty editable, BooleanProperty visible) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.checkBox = new ClearableCheckBox(clearSupplier);
        this.editable = editable;
        this.visible = visible;
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
    public Object getValue() {
        return this.checkBox.getValue();
    }

    @Override
    public void setValue(Object value) {
        this.checkBox.setValue(((boolean) value));
    }

    @Override
    public ObservableList<Void> getChoices() {
        return null;
    }

    @Override
    public Optional<ObservableValue<? extends Object>> getObservableValue() {
        return Optional.ofNullable(this.checkBox.getCheckBox().selectedProperty());
    }

    @Override
    public BooleanProperty isEditable() {
        return this.editable;
    }

    @Override
    public BooleanProperty isVisible() {
        return this.visible;
    }

    public ClearableCheckBox getCheckBox() {
        return checkBox;
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public void setVisible(boolean visible) {
        this.visible.set(visible);
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
