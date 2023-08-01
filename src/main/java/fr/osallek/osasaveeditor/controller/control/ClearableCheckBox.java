package fr.osallek.osasaveeditor.controller.control;

import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import org.controlsfx.glyphfont.FontAwesome;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class ClearableCheckBox extends HBox {

    private final CheckBox checkBox;

    private final Button button;

    public ClearableCheckBox() {
        this(null);
    }

    public ClearableCheckBox(BooleanSupplier clearSupplier) {
        this.checkBox = new CheckBox();
        this.checkBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this.checkBox, Priority.ALWAYS);

        this.button = new Button(String.valueOf(FontAwesome.Glyph.REMOVE.getChar()));
        this.button.setStyle("-fx-font-family: FontAwesome");
        this.button.setTextAlignment(TextAlignment.CENTER);

        if (clearSupplier != null) {
            this.button.setOnMouseReleased(e -> this.checkBox.setSelected(clearSupplier.getAsBoolean()));
        }

        getChildren().add(this.checkBox);
        getChildren().add(this.button);
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public boolean getValue() {
        return this.checkBox.isSelected();
    }

    public BooleanProperty selectedProperty() {
        return this.checkBox.selectedProperty();
    }

    public void setValue(boolean b) {
        this.checkBox.setSelected(b);
    }

    public void setSupplier(BooleanSupplier clearSupplier) {
        if (clearSupplier != null) {
            this.button.setOnMouseReleased(e -> setValue(clearSupplier.getAsBoolean()));
        }
    }

    public EventHandler<ActionEvent> getOnAction() {
        return this.checkBox.getOnAction();
    }

    public void setOnAction(EventHandler<ActionEvent> onAction) {
        this.checkBox.setOnAction(onAction);
    }
}
