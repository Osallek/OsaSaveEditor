package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.control.CustomClearableTextField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import org.controlsfx.control.textfield.CustomTextField;

import java.util.Optional;
import java.util.function.Supplier;

public class ClearableTextItem implements CustomItem<String> {

    private final String category;

    private final String name;

    private final CustomTextField textField;

    private Supplier<String> supplier;

    private final BooleanProperty editable;

    private final BooleanProperty visible;

    public ClearableTextItem(String category, String name) {
        this(category, name, null, null);
    }

    public ClearableTextItem(String category, String name, String value, Supplier<String> clearSupplier) {
        this(category, name, value, clearSupplier, new SimpleBooleanProperty(true));
    }

    public ClearableTextItem(String category, String name, String value, Supplier<String> clearSupplier, BooleanProperty editable) {
        this(category, name, value, clearSupplier, editable, new SimpleBooleanProperty(true));
    }

    public ClearableTextItem(String category, String name, String value, Supplier<String> clearSupplier, BooleanProperty editable, BooleanProperty visible) {
        this.category = category;
        this.name = name;
        this.textField = CustomClearableTextField.createClearableTextField(clearSupplier);
        this.textField.setText(value);
        this.supplier = clearSupplier;
        this.editable = editable;
        this.visible = visible;
        this.textField.managedProperty().bind(this.textField.visibleProperty());
    }

    @Override
    public Class<?> getType() {
        return ClearableTextItem.class;
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
    public String getValue() {
        return this.textField.getText();
    }

    @Override
    public void setValue(String value) {
        this.textField.setText(value);
    }

    @Override
    public Optional<ObservableValue<String>> getObservableValue() {
        return Optional.of(this.textField.textProperty());
    }

    @Override
    public BooleanProperty isEditable() {
        return this.editable;
    }

    @Override
    public BooleanProperty isVisible() {
        return this.visible;
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public void setVisible(boolean visible) {
        this.visible.set(visible);
    }

    public void setSupplier(Supplier<String> supplier) {
        this.supplier = supplier;
        this.textField.getRight().setOnMouseReleased(e -> this.textField.setText(this.supplier.get()));
    }

    public Supplier<String> getSupplier() {
        return supplier;
    }

    public CustomTextField getTextField() {
        return textField;
    }
}
