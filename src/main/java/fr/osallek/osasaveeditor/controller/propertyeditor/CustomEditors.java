package fr.osallek.osasaveeditor.controller.propertyeditor;

import fr.osallek.osasaveeditor.controller.control.ClearableCheckComboBox;
import fr.osallek.osasaveeditor.controller.pane.AbstractObjectField;
import fr.osallek.osasaveeditor.controller.pane.AbstractPropertyEditor;
import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet;
import fr.osallek.osasaveeditor.controller.pane.NumericField;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ButtonItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableCheckBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableCheckComboBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableColorPickerItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableComboBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableDatePickerItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableSliderIntItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableSliderItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableSpinnerItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableTextItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.HBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.PropertySheetItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.SelectableGridViewItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.TextItem;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.apache.commons.collections4.CollectionUtils;
import org.controlsfx.control.IndexedCheckModel;
import org.controlsfx.dialog.FontSelectorDialog;
import org.controlsfx.property.editor.PropertyEditor;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public class CustomEditors {

    private CustomEditors() {}

    public static PropertyEditor<?> createTextEditor(CustomPropertySheet.Item property) {

        return new AbstractPropertyEditor<String, TextField>(property, new TextField()) {
            {
                enableAutoSelectAll(getEditor());
            }

            @Override
            protected StringProperty getObservableValue() {
                return getEditor().textProperty();
            }

            @Override
            public void setValue(String value) {
                getEditor().setText(value);
            }
        };
    }

    public static PropertyEditor<String> createTextEditor(TextItem textItem) {

        return new AbstractPropertyEditor<>(textItem, textItem.text()) {

            @Override
            protected StringProperty getObservableValue() {
                return getEditor().textProperty();
            }

            @Override
            public void setValue(String value) {
                getEditor().setText(value);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static PropertyEditor<?> createNumericEditor(CustomPropertySheet.Item property) {

        return new AbstractPropertyEditor<Number, NumericField>(property, new NumericField((Class<? extends Number>) property.getType())) {

            private Class<? extends Number> sourceClass = (Class<? extends Number>) property.getType(); //Double.class;

            {
                enableAutoSelectAll(getEditor());
            }

            @Override
            protected ObservableValue<Number> getObservableValue() {
                return getEditor().valueProperty();
            }

            @Override
            public Number getValue() {
                try {
                    return this.sourceClass.getConstructor(String.class).newInstance(getEditor().getText());
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                         | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void setValue(Number value) {
                sourceClass = value.getClass();
                getEditor().setText(value.toString());
            }

        };
    }

    public static PropertyEditor<Boolean> createCheckEditor(CustomPropertySheet.Item<Boolean> property) {

        return new AbstractPropertyEditor<>(property, new CheckBox()) {

            @Override
            protected BooleanProperty getObservableValue() {
                return getEditor().selectedProperty();
            }

            @Override
            public void setValue(Boolean value) {
                getEditor().setSelected(value);
            }
        };

    }

    public static <T> PropertyEditor<?> createChoiceEditor(CustomPropertySheet.Item property, final Collection<T> choices) {

        return new AbstractPropertyEditor<T, ComboBox<T>>(property, new ComboBox<>()) {

            {
                getEditor().setItems(FXCollections.observableArrayList(choices));
            }

            @Override
            protected ObservableValue<T> getObservableValue() {
                return getEditor().getSelectionModel().selectedItemProperty();
            }

            @Override
            public void setValue(T value) {
                getEditor().getSelectionModel().select(value);
            }
        };
    }

    public static PropertyEditor<?> createColorEditor(CustomPropertySheet.Item property) {
        return new AbstractPropertyEditor<Color, ColorPicker>(property, new ColorPicker()) {

            @Override
            protected ObservableValue<Color> getObservableValue() {
                return getEditor().valueProperty();
            }

            @Override
            public void setValue(Color value) {
                getEditor().setValue(value);
            }
        };
    }


    public static PropertyEditor<?> createDateEditor(CustomPropertySheet.Item property) {
        return new AbstractPropertyEditor<LocalDate, DatePicker>(property, new DatePicker()) {

            //TODO: Provide date picker customization support

            @Override
            protected ObservableValue<LocalDate> getObservableValue() {
                return getEditor().valueProperty();
            }

            @Override
            public void setValue(LocalDate value) {
                getEditor().setValue(value);
            }
        };
    }

    public static PropertyEditor<?> createFontEditor(CustomPropertySheet.Item property) {

        return new AbstractPropertyEditor<Font, AbstractObjectField<Font>>(property, new AbstractObjectField<>() {
            @Override
            protected Class<Font> getType() {
                return Font.class;
            }

            @Override
            protected String objectToString(Font font) {
                return font == null ? "" : String.format("%s, %.1f", font.getName(), font.getSize()); //$NON-NLS-1$ //$NON-NLS-2$
            }

            @Override
            protected Font edit(Font font) {
                FontSelectorDialog dlg = new FontSelectorDialog(font);
                Optional<Font> optionalFont = dlg.showAndWait();
                return optionalFont.orElse(null);
            }
        }) {

            @Override
            protected ObservableValue<Font> getObservableValue() {
                return getEditor().getObjectProperty();
            }

            @Override
            public void setValue(Font value) {
                getEditor().getObjectProperty().set(value);
            }
        };

    }

    public static PropertyEditor<String> createClearableLabeledTextEditor(ClearableTextItem property) {
        return new AbstractPropertyEditor<>(property, property.getTextField()) {

            @Override
            protected StringProperty getObservableValue() {
                return getEditor().textProperty();
            }

            @Override
            public void setValue(String value) {
                getEditor().setText(value);
            }
        };
    }

    public static PropertyEditor<LocalDate> createClearableDatePickerEditor(ClearableDatePickerItem property) {

        return new AbstractPropertyEditor<>(property, property.getDatePicker()) {

            @Override
            protected ObservableValue<LocalDate> getObservableValue() {
                return getEditor().getDatePicker().valueProperty();
            }

            @Override
            public void setValue(LocalDate value) {
                getEditor().getDatePicker().setValue(value);
            }
        };
    }

    public static <T> PropertyEditor<T> createClearableSpinnerEditor(ClearableSpinnerItem<T> property) {

        return new AbstractPropertyEditor<>(property, property.getSpinner()) {

            @Override
            protected ObservableValue<T> getObservableValue() {
                return getEditor().getSpinner().valueProperty();
            }

            @Override
            public void setValue(T value) {
                getEditor().setValue(value);
            }
        };
    }

    public static PropertyEditor<Double> createClearableSliderEditor(ClearableSliderItem property) {

        return new AbstractPropertyEditor<>(property, property.getSlider()) {

            @Override
            protected ObservableValue<Double> getObservableValue() {
                return getEditor().getDoubleProperty().asObject();
            }

            @Override
            public void setValue(Double value) {
                getEditor().setValue(value);
            }
        };
    }

    public static PropertyEditor<Integer> createClearableSliderEditor(ClearableSliderIntItem property) {

        return new AbstractPropertyEditor<>(property, property.getSlider()) {

            @Override
            protected ObservableValue<Integer> getObservableValue() {
                return getEditor().getObservableValue();
            }

            @Override
            public void setValue(Integer value) {
                getEditor().setValue(value);
            }
        };
    }

    public static <T> PropertyEditor<T> createClearableComboBoxEditor(ClearableComboBoxItem<T> clearableComboBoxItem) {

        return new AbstractPropertyEditor<>(clearableComboBoxItem, clearableComboBoxItem.getComboBox()) {
            {
                getEditor().setItems(clearableComboBoxItem.getChoices());

                if (clearableComboBoxItem.getOnAction() != null) {
                    getEditor().setOnAction(clearableComboBoxItem.getOnAction());
                }

                if (clearableComboBoxItem.getConverter() != null) {
                    getEditor().setConverter(clearableComboBoxItem.getConverter());
                }

                if (clearableComboBoxItem.getCellFactory() != null) {
                    getEditor().setCellFactory(clearableComboBoxItem.getCellFactory());
                }
            }

            @Override
            protected ObservableValue<T> getObservableValue() {
                return getEditor().getSelectionModel().selectedItemProperty();
            }

            @Override
            public void setValue(T value) {
                getEditor().getSelectionModel().select(value);
            }
        };
    }

    public static <T> PropertyEditor<ObservableList<T>> createClearableCheckComboBoxEditor(ClearableCheckComboBoxItem<T> comboBoxItem) {

        return new AbstractPropertyEditor<>(comboBoxItem, comboBoxItem.getCheckComboBox()) {

            private ListProperty<T> list;

            {
                if (getEditor().getItems() != null && comboBoxItem.getChoices() != null) {
                    getEditor().getItems().setAll(comboBoxItem.getChoices());
                }

                if (comboBoxItem.getConverter() != null) {
                    getEditor().setConverter(comboBoxItem.getConverter());
                }
            }

            @Override
            protected ListProperty<T> getObservableValue() {
                if (list == null) {
                    list = new SimpleListProperty<>(getEditor().getSelectedValues());
                }

                return list;
            }

            @Override
            public void setValue(ObservableList<T> checked) {
                ClearableCheckComboBox<T> comboBoxItem = getEditor();
                IndexedCheckModel<T> checkModel = comboBoxItem.getCheckComboBox().getCheckModel();
                if (CollectionUtils.isNotEmpty(checked)) {
                    comboBoxItem.getItems().forEach(t -> {
                        if (checked.contains(t)) {
                            if (!checkModel.isChecked(t)) {
                                checkModel.check(t);
                            }
                        } else {
                            if (checkModel.isChecked(t)) {
                                checkModel.clearCheck(t);
                            }
                        }
                    });
                } else {
                    comboBoxItem.clearChecks();
                }
            }
        };
    }

    public static PropertyEditor<Boolean> createClearableCheckEditor(ClearableCheckBoxItem checkBoxItem) {

        return new AbstractPropertyEditor<>(checkBoxItem, checkBoxItem.getCheckBox()) {

            @Override
            protected BooleanProperty getObservableValue() {
                return getEditor().selectedProperty();
            }

            @Override
            public void setValue(Boolean value) {
                getEditor().setValue(value);
            }
        };

    }

    public static <T> PropertyEditor<ObservableSet<T>> createSelectableGridViewEditor(SelectableGridViewItem<T> selectableGridViewItem) {

        return new AbstractPropertyEditor<>(selectableGridViewItem, selectableGridViewItem
                .getSelectableGridView()) {

            private SetProperty<T> list = new SimpleSetProperty<>();

            @Override
            protected SetProperty<T> getObservableValue() {
                if (list == null) {
                    list = new SimpleSetProperty<>(getEditor().getSelectedItems());
                }

                return list;
            }

            @Override
            public void setValue(ObservableSet<T> value) {
                if (value != null) {
                    value.forEach(getEditor()::select);
                }
            }
        };
    }

    public static <T> PropertyEditor<T> createHBox(HBoxItem<T> hBoxItem) {

        return new AbstractPropertyEditor<>(hBoxItem, hBoxItem.gethBox()) {

            @Override
            protected ObservableValue<T> getObservableValue() {
                return new ReadOnlyObjectWrapper<>(hBoxItem.gethBox(), "value");
            }

            @Override
            public void setValue(T value) {
            }
        };
    }

    public static PropertyEditor<String> createButton(ButtonItem buttonItem) {

        return new AbstractPropertyEditor<>(buttonItem, buttonItem.getButton()) {

            @Override
            protected ObservableValue<String> getObservableValue() {
                return new ReadOnlyObjectWrapper<>(buttonItem.getButton(), "value");
            }

            @Override
            public void setValue(String value) {
            }
        };
    }

    public static PropertyEditor<Color> createColorPickerSheet(ClearableColorPickerItem colorPickerItem) {

        return new AbstractPropertyEditor<>(colorPickerItem, colorPickerItem.getColorPicker()) {

            @Override
            protected ObservableValue<Color> getObservableValue() {
                return getEditor().getColorPicker().valueProperty();
            }

            @Override
            public void setValue(Color value) {
                getEditor().select(value);
            }
        };
    }

    public static PropertyEditor<String> createPropertySheet(PropertySheetItem propertySheetItem) {

        return new AbstractPropertyEditor<>(propertySheetItem, propertySheetItem.propertySheet()) {

            @Override
            protected ObservableValue<String> getObservableValue() {
                return new ReadOnlyObjectWrapper<>(propertySheetItem.propertySheet(), "value");
            }

            @Override
            public void setValue(String value) {
            }
        };
    }

    private static void enableAutoSelectAll(final TextInputControl control) {
        control.focusedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldValue, Boolean newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                Platform.runLater(control::selectAll);
            }
        });
    }
}
