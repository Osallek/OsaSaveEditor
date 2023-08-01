package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet;
import javafx.beans.value.ObservableValue;

import java.util.Optional;

public record PropertySheetItem(String category, CustomPropertySheet propertySheet) implements CustomPropertySheet.Item {

    @Override
    public Class<?> getType() {
        return PropertySheetItem.class;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {
    }

    @Override
    public Optional<ObservableValue<? extends Object>> getObservableValue() {
        return Optional.empty();
    }
}
