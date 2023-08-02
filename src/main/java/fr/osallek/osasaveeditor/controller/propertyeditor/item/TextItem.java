package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;

import java.util.Optional;

public record TextItem(String category, String name, String description, Text text) implements CustomItem<String> {

    @Override
    public Class<?> getType() {
        return TextItem.class;
    }

    @Override
    public String getValue() {
        return this.text.getText();
    }

    @Override
    public void setValue(String value) {
        this.text.setText(value);
    }

    @Override
    public Optional<ObservableValue<String>> getObservableValue() {
        return Optional.of(this.text().textProperty());
    }
}
