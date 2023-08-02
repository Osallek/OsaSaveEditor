package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class HBoxItem<U> implements CustomItem<U> {

    private final String category;

    private final String name;

    private final HBox hBox;

    private final Integer colSpan;

    public HBoxItem(String category, HBox hBox) {
        this(category, null, hBox);
    }

    public HBoxItem(String category, String name, HBox hBox) {
        this(category, name, hBox, null);
    }

    public HBoxItem(String category, String name, HBox hBox, Integer colSpan) {
        this.category = category;
        this.name = name;
        this.hBox = hBox;
        this.colSpan = colSpan;
    }

    @Override
    public Class<?> getType() {
        return HBoxItem.class;
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
        return null;
    }

    @Override
    public U getValue() {
        return null;
    }

    @Override
    public void setValue(U value) {
    }

    @Override
    public Optional<ObservableValue<U>> getObservableValue() {
        return Optional.empty();
    }

    @Override
    public BooleanProperty isVisible() {
        return this.hBox.visibleProperty();
    }

    @Override
    public int forceValueColSpan() {
        return this.colSpan == null ? 1 : this.colSpan;
    }

    public HBox gethBox() {
        return hBox;
    }
}
