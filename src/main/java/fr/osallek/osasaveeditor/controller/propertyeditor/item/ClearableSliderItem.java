package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.control.ClearableSlider;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.util.converter.NumberStringConverter;

import java.util.Optional;
import java.util.function.DoubleSupplier;

public class ClearableSliderItem implements CustomItem<Integer> {

    private final String category;

    private final String name;

    private final ClearableSlider slider;

    private final BooleanProperty editable;

    private final BooleanProperty visible;

    public ClearableSliderItem(String category, String name, double min, double max) {
        this(category, name, min, max, null, null);
    }

    public ClearableSliderItem(String category, String name, double min, double max, Double value, DoubleSupplier supplier) {
        this(category, name, new ClearableSlider(min, max, value, supplier, new NumberStringConverter("###.###")), new SimpleBooleanProperty(true));
    }

    public ClearableSliderItem(String category, String name, ClearableSlider slider, BooleanProperty editable) {
        this(category, name, slider, editable, new SimpleBooleanProperty(true));
    }

    public ClearableSliderItem(String category, String name, ClearableSlider slider, BooleanProperty editable, BooleanProperty visible) {
        this.category = category;
        this.name = name;
        this.slider = slider;
        this.editable = editable;
        this.visible = visible;
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
    public Object getValue() {
        return this.slider.getValue();
    }

    @Override
    public void setValue(Object value) {
        this.slider.setValue(value == null ? 0 : (double) value);
    }

    @Override
    public ObservableList<Integer> getChoices() {
        return null;
    }

    @Override
    public Optional<ObservableValue<? extends Object>> getObservableValue() {
        return Optional.empty();
    }

    @Override
    public BooleanProperty isEditable() {
        return this.editable;
    }

    @Override
    public BooleanProperty isVisible() {
        return this.visible;
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public void setVisible(boolean visible) {
        this.visible.set(visible);
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
