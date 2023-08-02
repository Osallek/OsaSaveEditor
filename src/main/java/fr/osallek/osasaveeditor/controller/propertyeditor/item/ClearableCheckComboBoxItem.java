package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.control.ClearableCheckComboBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.controlsfx.control.IndexedCheckModel;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ClearableCheckComboBoxItem<U> implements CustomItem<ObservableList<U>> {

    private final String category;

    private final String name;

    private final ObservableList<U> values;

    private final ClearableCheckComboBox<U> checkComboBox;

    private final BooleanProperty editable;

    private EventHandler<ActionEvent> onAction;

    private StringConverter<U> converter;

    private Callback<ListView<U>, ListCell<U>> cellFactory;

    public ClearableCheckComboBoxItem(String category, String name, ObservableList<U> values, ClearableCheckComboBox<U> checkComboBox) {
        this(category, name, values, null, checkComboBox, new SimpleBooleanProperty(true));
    }

    public ClearableCheckComboBoxItem(String category, String name, ObservableList<U> values, ObservableList<U> selectedValues,
                                      ClearableCheckComboBox<U> checkComboBox, BooleanProperty editable) {
        this.category = category;
        this.name = name;
        this.values = values;
        this.checkComboBox = checkComboBox;
        this.editable = editable;
        this.checkComboBox.managedProperty().bind(this.checkComboBox.visibleProperty());
        setValue(selectedValues);
    }

    @Override
    public Class<?> getType() {
        return ClearableCheckComboBoxItem.class;
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
    public ObservableList<U> getValue() {
        return this.checkComboBox.getSelectedValues();
    }

    @Override
    public void setValue(ObservableList<U> value) {
        IndexedCheckModel<U> checkModel = this.checkComboBox.getCheckComboBox().getCheckModel();

        if (CollectionUtils.isNotEmpty(value)) {
            this.checkComboBox.getItems().forEach(t -> {
                if (value.contains(t)) {
                    if (!checkModel.isChecked(t)) {
                        checkModel.check(t);
                    }
                } else {
                    if (checkModel.isChecked(t)) {
                        checkModel.clearCheck(t);
                    }
                }
            });
        } else {
            this.checkComboBox.clearChecks();
        }
    }

    public ObservableList<U> getChoices() {
        return this.values;
    }

    @Override
    public Optional<ObservableValue<ObservableList<U>>> getObservableValue() {
        return Optional.of(new SimpleObjectProperty<>(this.checkComboBox.getSelectedValues()));
    }

    @Override
    public BooleanProperty isEditable() {
        return this.editable;
    }

    @Override
    public BooleanProperty isVisible() {
        return this.checkComboBox.visibleProperty();
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public ClearableCheckComboBox<U> getCheckComboBox() {
        return this.checkComboBox;
    }

    public void check(U u) {
        this.checkComboBox.check(u);
    }

    public void clearCheck(U u) {
        this.checkComboBox.clearCheck(u);
    }

    public void setSupplier(Supplier<List<U>> clearSupplier) {
        if (clearSupplier != null) {
            this.checkComboBox.setSupplier(clearSupplier);
        }
    }

    public EventHandler<ActionEvent> getOnAction() {
        return this.onAction;
    }

    public void setOnAction(EventHandler<ActionEvent> onAction) {
        this.onAction = onAction;
    }

    public void setConverter(StringConverter<U> converter) {
        this.converter = converter;
    }

    public StringConverter<U> getConverter() {
        return this.converter;
    }

    public void setCellFactory(Callback<ListView<U>, ListCell<U>> cellFactory) {
        this.cellFactory = cellFactory;
    }

    public Callback<ListView<U>, ListCell<U>> getCellFactory() {
        return this.cellFactory;
    }
}
