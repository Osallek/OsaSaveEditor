package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.control.SelectableGridView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.io.File;
import java.util.Optional;
import java.util.function.Function;

public class SelectableGridViewItem<U> implements CustomItem<ObservableSet<U>> {

    private final String category;

    private final ObservableList<U> values;

    private final SelectableGridView<U> selectableGridView;

    private final BooleanProperty editable;

    private Function<U, String> textFunction;

    public SelectableGridViewItem(String category, SelectableGridView<U> selectableGridView) {
        this(category, selectableGridView, new SimpleBooleanProperty(true));
    }

    public SelectableGridViewItem(String category, SelectableGridView<U> selectableGridView, BooleanProperty editable) {
        this.category = category;
        this.values = selectableGridView.getItems();
        this.selectableGridView = selectableGridView;
        this.editable = editable;
    }

    @Override
    public Class<?> getType() {
        return SelectableGridViewItem.class;
    }

    @Override
    public String category() {
        return this.category;
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
    public ObservableSet<U> getValue() {
        return this.selectableGridView.getSelectedItems();
    }

    @Override
    public void setValue(ObservableSet<U> value) {
        //Todo check if ok
    }

    public ObservableList<U> getChoices() {
        return this.values;
    }

    @Override
    public Optional<ObservableValue<ObservableSet<U>>> getObservableValue() {
        return Optional.of(new SimpleObjectProperty<>(this.selectableGridView.getSelectedItems()));
    }

    @Override
    public BooleanProperty isEditable() {
        return this.editable;
    }

    @Override
    public BooleanProperty isVisible() {
        return this.selectableGridView.visibleProperty();
    }

    public SelectableGridView<U> getSelectableGridView() {
        return this.selectableGridView;
    }

    public int getNbItems() {
        return this.values.size();
    }

    public void select(U u) {
        this.selectableGridView.select(u);
    }

    public void unSelect(U u) {
        this.selectableGridView.unSelect(u);
    }

    public void setCellFactory(Function<U, String> textFunction, Function<U, File> imageFunction, File defaultFile) {
        this.getSelectableGridView().setCellFactory(textFunction, imageFunction, defaultFile);
    }

    public Function<U, String> getCellFactory() {
        return this.textFunction;
    }
}
