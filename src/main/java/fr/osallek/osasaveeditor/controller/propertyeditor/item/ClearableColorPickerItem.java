package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.control.ClearableColorPicker;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.Optional;
import java.util.function.Supplier;

public class ClearableColorPickerItem implements CustomItem<Color> {

    private final String category;

    private final String name;

    private final String description;

    private final ClearableColorPicker colorPicker;

    private final BooleanProperty editable;

    private EventHandler<ActionEvent> onAction;

    private StringConverter<Color> converter;

    private Callback<ListView<Color>, ListCell<Color>> cellFactory;

    public ClearableColorPickerItem(String category, String name, ClearableColorPicker colorPicker) {
        this(category, name, null, colorPicker, new SimpleBooleanProperty(true));
    }

    public ClearableColorPickerItem(String category, String name, String description, ClearableColorPicker colorPicker) {
        this(category, name, description, colorPicker, new SimpleBooleanProperty(true));
    }

    public ClearableColorPickerItem(String category, String name, String description, ClearableColorPicker colorPicker, BooleanProperty editable) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.colorPicker = colorPicker;
        this.editable = editable;
    }

    @Override
    public Class<?> getType() {
        return ClearableColorPickerItem.class;
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
    public Color getValue() {
        return this.colorPicker.getSelectedValue();
    }

    @Override
    public void setValue(Color value) {
        this.colorPicker.select(value);
    }

    @Override
    public Optional<ObservableValue<Color>> getObservableValue() {
        return Optional.of(this.colorPicker.getColorPicker().valueProperty());
    }

    @Override
    public BooleanProperty isEditable() {
        return this.editable;
    }

    @Override
    public BooleanProperty isVisible() {
        return this.colorPicker.visibleProperty();
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public ClearableColorPicker getColorPicker() {
        return this.colorPicker;
    }

    public void setSupplier(Supplier<Color> clearSupplier) {
        if (clearSupplier != null) {
            this.colorPicker.setSupplier(clearSupplier);
        }
    }

    public EventHandler<ActionEvent> getOnAction() {
        return this.onAction;
    }

    public void setOnAction(EventHandler<ActionEvent> onAction) {
        this.onAction = onAction;
    }

    public ObjectProperty<Color> valueProperty() {
        return this.colorPicker.valueProperty();
    }

    public void setConverter(StringConverter<Color> converter) {
        this.converter = converter;
    }

    public StringConverter<Color> getConverter() {
        return this.converter;
    }

    public void setCellFactory(Callback<ListView<Color>, ListCell<Color>> cellFactory) {
        this.cellFactory = cellFactory;
    }

    public Callback<ListView<Color>, ListCell<Color>> getCellFactory() {
        return this.cellFactory;
    }
}
