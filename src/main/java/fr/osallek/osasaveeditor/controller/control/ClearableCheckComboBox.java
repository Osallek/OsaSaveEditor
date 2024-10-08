package fr.osallek.osasaveeditor.controller.control;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.glyphfont.FontAwesome;

import java.util.List;
import java.util.function.Supplier;

public class ClearableCheckComboBox<U> extends HBox {

    private final CheckComboBox<U> checkComboBox;

    private final Button button;

    public ClearableCheckComboBox() {
        this(null);
    }

    public ClearableCheckComboBox(Supplier<List<U>> clearSupplier) {
        this.getStyleClass().add("clearable-check-combo");
        this.checkComboBox = new CheckComboBox<>();
        this.checkComboBox.getStyleClass().add("check-combo-box");
        this.checkComboBox.setMaxWidth(Double.MAX_VALUE);
        this.checkComboBox.getProperties().put(CheckComboBox.COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY, 10);
        HBox.setHgrow(this.checkComboBox, Priority.ALWAYS);

        this.button = new Button(String.valueOf(FontAwesome.Glyph.REMOVE.getChar()));
        this.button.setStyle("-fx-font-family: FontAwesome");
        this.button.setTextAlignment(TextAlignment.CENTER);

        if (clearSupplier != null) {
            this.button.setOnMouseReleased(e -> {
                clearChecks();
                clearSupplier.get().forEach(this::check);
            });
        }

        getChildren().add(this.checkComboBox);
        getChildren().add(this.button);
    }

    public CheckComboBox<U> getCheckComboBox() {
        return checkComboBox;
    }

    public ObservableList<U> getSelectedValues() {
        return this.checkComboBox.getCheckModel().getCheckedItems();
    }

    public void check(U u) {
        this.checkComboBox.getCheckModel().check(u);
    }

    public void clearCheck(U u) {
        this.checkComboBox.getCheckModel().clearCheck(u);
    }

    public void clearChecks() {
        this.checkComboBox.getCheckModel().clearChecks();
    }

    public void setSupplier(Supplier<List<U>> clearSupplier) {
        if (clearSupplier != null) {
            this.button.setOnMouseReleased(e -> {
                this.clearChecks();
                clearSupplier.get().forEach(this::check);
            });
        }
    }

    public void setConverter(StringConverter<U> converter) {
        this.checkComboBox.setConverter(converter);
    }

    public StringConverter<U> getConverter() {
        return this.checkComboBox.getConverter();
    }

    public final ObservableList<U> getItems() {
        return this.checkComboBox.getItems();
    }
}
