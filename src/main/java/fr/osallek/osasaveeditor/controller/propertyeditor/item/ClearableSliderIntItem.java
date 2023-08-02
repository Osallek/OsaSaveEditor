package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.control.ClearableSliderInt;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.util.converter.IntegerStringConverter;

import java.util.Optional;
import java.util.function.IntSupplier;

public class ClearableSliderIntItem implements CustomItem<Integer> {

    private final String category;

    private final String name;

    private final ClearableSliderInt slider;

    private final BooleanProperty editable;

    public ClearableSliderIntItem(String category, String name, int min, int max) {
        this(category, name, min, max, null, null);
    }

    public ClearableSliderIntItem(String category, String name, int min, int max, Integer value, IntSupplier supplier) {
        this(category, name, new ClearableSliderInt(min, max, value, supplier, new IntegerStringConverter()), new SimpleBooleanProperty(true));
    }

    public ClearableSliderIntItem(String category, String name, ClearableSliderInt slider, BooleanProperty editable) {
        this.category = category;
        this.name = name;
        this.slider = slider;
        this.editable = editable;
    }

    @Override
    public Class<?> getType() {
        return ClearableSliderIntItem.class;
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
    public Integer getValue() {
        return this.slider.getValue();
    }

    @Override
    public void setValue(Integer value) {
        this.slider.setValue(value == null ? 0 : value);
    }

    @Override
    public Optional<ObservableValue<Integer>> getObservableValue() {
        return Optional.empty();
    }

    @Override
    public BooleanProperty isEditable() {
        return this.editable;
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public ClearableSliderInt getSlider() {
        return this.slider;
    }

    public void setSupplier(IntSupplier clearSupplier) {
        if (clearSupplier != null) {
            this.slider.setSupplier(clearSupplier);
        }
    }
}
