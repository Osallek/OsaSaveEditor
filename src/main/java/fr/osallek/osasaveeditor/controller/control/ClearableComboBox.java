package fr.osallek.osasaveeditor.controller.control;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.glyphfont.FontAwesome;

import java.util.function.Supplier;

public class ClearableComboBox<U> extends HBox {

    private final ComboBox<U> comboBox;

    private final Button button;

    private final Supplier<U> clearSupplier;

    public ClearableComboBox(ComboBox<U> comboBox) {
        this(comboBox, null);
    }

    public ClearableComboBox(ComboBox<U> comboBox, Supplier<U> clearSupplier) {
        this.comboBox = comboBox;
        this.comboBox.setMaxWidth(Double.MAX_VALUE);
        this.comboBox.getProperties().put(CheckComboBox.COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY, 10);
        HBox.setHgrow(this.comboBox, Priority.ALWAYS);

        this.button = new Button(String.valueOf(FontAwesome.Glyph.REMOVE.getChar()));
        this.button.setStyle("-fx-font-family: FontAwesome");
        this.button.setTextAlignment(TextAlignment.CENTER);

        this.clearSupplier = clearSupplier;
        if (this.clearSupplier != null) {
            this.button.setOnMouseReleased(e -> reset());
        }

        getChildren().add(this.comboBox);
        getChildren().add(this.button);
    }

    public ComboBox<U> getComboBox() {
        return comboBox;
    }

    public U getSelectedValue() {
        return this.comboBox.getValue();
    }

    public void select(U u) {
        this.comboBox.getSelectionModel().select(u);
    }

    public void setSupplier(Supplier<U> clearSupplier) {
        if (clearSupplier != null) {
            this.button.setOnMouseReleased(e -> this.select(clearSupplier.get()));
        }
    }

    public EventHandler<ActionEvent> getOnAction() {
        return this.comboBox.getOnAction();
    }

    public void setOnAction(EventHandler<ActionEvent> onAction) {
        this.comboBox.setOnAction(onAction);
    }

    public ObjectProperty<U> valueProperty() {
        return this.comboBox.valueProperty();
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

    public final void setSelectionModel(SingleSelectionModel<U> value) {
        this.comboBox.setSelectionModel(value);
    }

    public final SingleSelectionModel<U> getSelectionModel() {
        return this.comboBox.getSelectionModel();
    }

    public final ObjectProperty<SingleSelectionModel<U>> selectionModelProperty() {
        return this.comboBox.selectionModelProperty();
    }

    public final void setItems(ObservableList<U> value) {
        this.comboBox.setItems(value);
    }

    public final ObservableList<U> getItems() {
        return this.comboBox.getItems();
    }

    public ObjectProperty<ObservableList<U>> itemsProperty() {
        return this.comboBox.itemsProperty();
    }

    public Supplier<U> getClearSupplier() {
        return clearSupplier;
    }

    public void reset() {
        this.select(clearSupplier.get());
    }
}
