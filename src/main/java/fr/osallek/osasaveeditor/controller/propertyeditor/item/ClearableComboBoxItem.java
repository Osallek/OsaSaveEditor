package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.control.ClearableComboBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ClearableComboBoxItem<U> implements CustomItem<U> {

    private final String category;

    private final String name;

    private final String description;

    private FilteredList<U> values;

    private final ClearableComboBox<U> comboBox;

    private final BooleanProperty editable;

    private EventHandler<ActionEvent> onAction;

    private Predicate<U> filter;

    public ClearableComboBoxItem(String category, String name, ObservableList<U> values, ClearableComboBox<U> comboBox) {
        this(category, name, values, null, null, comboBox);
    }

    public ClearableComboBoxItem(String category, String name, ObservableList<U> values, U value, ClearableComboBox<U> comboBox) {
        this(category, name, values, value, null, comboBox);
    }

    public ClearableComboBoxItem(String category, String name, ObservableList<U> values, U value, String description, ClearableComboBox<U> comboBox) {
        this(category, name, values, value, description, comboBox, new SimpleBooleanProperty(true));
    }

    public ClearableComboBoxItem(String category, String name, ObservableList<U> values, U value, String description, ClearableComboBox<U> comboBox,
                                 BooleanProperty editable) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.values = values.filtered(null);
        this.comboBox = comboBox;
        this.editable = editable;
        this.comboBox.managedProperty().bind(this.comboBox.visibleProperty());
        setValue(value);
    }

    @Override
    public Class<?> getType() {
        return ClearableComboBoxItem.class;
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
    public U getValue() {
        return this.comboBox.getSelectedValue();
    }

    @Override
    public void setValue(U value) {
        this.comboBox.getComboBox().setValue(value);
    }

    public ObservableList<U> getChoices() {
        return this.values;
    }

    @Override
    public Optional<ObservableValue<U>> getObservableValue() {
        return Optional.of(this.comboBox.getComboBox().valueProperty());
    }

    @Override
    public BooleanProperty isEditable() {
        return this.editable;
    }

    @Override
    public BooleanProperty isVisible() {
        return this.comboBox.visibleProperty();
    }

    public void setValues(ObservableList<U> values) {
        this.values = values.filtered(this.filter);
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public ClearableComboBox<U> getComboBox() {
        return this.comboBox;
    }

    public void select(U u) {
        this.comboBox.getComboBox().getSelectionModel().select(u);
    }

    public void setSupplier(Supplier<U> clearSupplier) {
        if (clearSupplier != null) {
            this.comboBox.setSupplier(clearSupplier);
        }
    }

    public EventHandler<ActionEvent> getOnAction() {
        return this.onAction;
    }

    public void setOnAction(EventHandler<ActionEvent> onAction) {
        this.onAction = onAction;
    }

    public ObjectProperty<U> valueProperty() {
        return this.comboBox.getComboBox().valueProperty();
    }

    public void setConverter(StringConverter<U> converter) {
        this.comboBox.setConverter(converter);
    }

    public StringConverter<U> getConverter() {
        return this.comboBox.getConverter();
    }

    public void setCellFactory(Callback<ListView<U>, ListCell<U>> cellFactory) {
        this.comboBox.setCellFactory(cellFactory);
    }

    public Callback<ListView<U>, ListCell<U>> getCellFactory() {
        return this.comboBox.getCellFactory();
    }

    public BooleanProperty editableProperty() {
        return this.editable;
    }

    public Predicate<U> getFilter() {
        return filter;
    }

    public ObservableList<U> getValues() {
        return FXCollections.unmodifiableObservableList(this.values);
    }

    public void setFilter(Predicate<U> filter) {
        this.filter = filter;
        this.values.setPredicate(this.filter);
    }
}
