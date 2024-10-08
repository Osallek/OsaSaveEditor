package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.control.ClearableSlider;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.util.converter.NumberStringConverter;

import java.util.Optional;
import java.util.function.DoubleSupplier;

public class ClearableSliderItem implements CustomItem<Double> {

    private final String category;

    private final String name;

    private final ClearableSlider slider;

    private final BooleanProperty editable;

    public ClearableSliderItem(String category, String name, double min, double max) {
        this(category, name, min, max, null, null);
    }

    public ClearableSliderItem(String category, String name, double min, double max, Double value, DoubleSupplier supplier) {
        this(category, name, new ClearableSlider(min, max, value, supplier, new NumberStringConverter("###.###")), new SimpleBooleanProperty(true));
    }

    public ClearableSliderItem(String category, String name, ClearableSlider slider, BooleanProperty editable) {
        this.category = category;
        this.name = name;
        this.slider = slider;
        this.editable = editable;
        this.slider.managedProperty().bind(this.slider.visibleProperty());
    }

    @Override
    public Class<?> getType() {
        return ClearableSliderItem.class;
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
    public Double getValue() {
        return this.slider.getValue();
    }

    @Override
    public void setValue(Double value) {
        this.slider.setValue(value == null ? 0 : value);
    }

    @Override
    public Optional<ObservableValue<Double>> getObservableValue() {
        return Optional.of(this.slider.getDoubleProperty().asObject());
    }

    @Override
    public BooleanProperty isEditable() {
        return this.editable;
    }

    @Override
    public BooleanProperty isVisible() {
        return this.slider.visibleProperty();
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public ClearableSlider getSlider() {
        return this.slider;
    }

    public DoubleProperty getObservableDoubleValue() {
        return this.slider.getDoubleProperty();
    }

    public double getDoubleValue() {
        return this.slider.getValue();
    }

    public void setSupplier(DoubleSupplier clearSupplier) {
        if (clearSupplier != null) {
            this.slider.setSupplier(clearSupplier);
        }
    }
}
