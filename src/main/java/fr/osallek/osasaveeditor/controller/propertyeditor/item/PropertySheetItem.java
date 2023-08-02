package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;

import java.util.Optional;

public record PropertySheetItem(String category, CustomPropertySheet propertySheet) implements CustomPropertySheet.Item<String> {

    @Override
    public Class<PropertySheetItem> getType() {
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
    public String getValue() {
        return null;
    }

    @Override
    public void setValue(String value) {
    }

    @Override
    public Optional<ObservableValue<String>> getObservableValue() {
        return Optional.empty();
    }

    @Override
    public BooleanProperty isVisible() {
        return this.propertySheet.visibleProperty();
    }
}
