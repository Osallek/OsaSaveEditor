/**
 * Copyright (c) 2013, 2015, ControlsFX All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. * Redistributions in binary form
 * must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. * Neither the name of ControlsFX, any associated website, nor the names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fr.osallek.osasaveeditor.controller.pane;

import fr.osallek.osasaveeditor.OsaSaveEditorApplication;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import org.controlsfx.property.BeanPropertyUtils;
import org.controlsfx.property.editor.Editors;
import org.controlsfx.property.editor.PropertyEditor;

import java.util.Comparator;
import java.util.Optional;

/**
 * The PropertySheet control is a powerful control designed to make it really easy for developers to present to end users a list of properties that the end user
 * is allowed to manipulate. Commonly a property sheet is used in visual editors and other tools where a lot of properties exist.
 *
 * <p>To better describe what a property sheet is, please refer to the picture
 * below:
 *
 * <br>
 * <center><img src="propertySheet.PNG" alt="Screenshot of PropertySheet"></center>
 *
 * <p>In this property sheet there exists two columns: the left column shows a
 * label describing the property itself, whereas the right column provides a {@link PropertyEditor} that allows the end user the means to manipulate the
 * property. In the screenshot you can see CheckEditor, ChoiceEditor, TextEditor and FontEditor, among the many editors that are available in the
 * {@link Editors} package.
 *
 * <p>To create a PropertySheet is simple: you firstly instantiate an instance
 * of PropertySheet, and then you pass in a list of {@link Item} instances, where each Item represents a single property that is to be editable by the end
 * user.
 *
 * <h3>Working with JavaBeans</h3>
 * Because a very common use case for a property sheet is editing properties on a JavaBean, there is convenience API for making this interaction easier. Refer
 * to the {@link BeanPropertyUtils class}, in particular the {@link BeanPropertyUtils#getProperties(Object)} method that will return a list of Item instances,
 * one Item instance per property on the given JavaBean.
 *
 * @see Item
 * @see Mode
 */
public class CustomPropertySheet extends CustomControlsFXControl {

    private static final String DEFAULT_STYLE_CLASS = "property-sheet";

    public enum Mode {
        NAME, CATEGORY
    }

    public interface Item<U> {

        Class<?> getType();

        String category();

        String name();

        String description();

        U getValue();

        void setValue(U value);

        Optional<ObservableValue<U>> getObservableValue();

        default BooleanProperty isEditable() {
            return new SimpleBooleanProperty(true);
        }

        default BooleanProperty isVisible() {
            return new SimpleBooleanProperty(true);
        }
    }

    private final ObservableList<Item<? extends Object>> items;

    public CustomPropertySheet() {
        this(null);
    }

    public CustomPropertySheet(ObservableList<Item<? extends Object>> items) {
        getStyleClass().add(DEFAULT_STYLE_CLASS);

        this.items = items == null ? FXCollections.observableArrayList() : items;
    }

    public ObservableList<Item<? extends Object>> getItems() {
        return items;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new CustomPropertySheetSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return OsaSaveEditorApplication.class.getResource("/styles/style.css").toExternalForm();
    }

    private final SimpleObjectProperty<Mode> modeProperty = new SimpleObjectProperty<>(this, "mode", Mode.NAME);

    public final SimpleObjectProperty<Mode> modeProperty() {
        return modeProperty;
    }

    public final Mode getMode() {
        return this.modeProperty.get();
    }

    public final void setMode(Mode mode) {
        this.modeProperty.set(mode);
    }

    private final SimpleObjectProperty<Callback<Item<?>, PropertyEditor<?>>> propertyEditorFactory = new SimpleObjectProperty<>(this, "propertyEditor",
                                                                                                                                new CustomDefaultPropertyEditorFactory());

    public final SimpleObjectProperty<Callback<Item<?>, PropertyEditor<?>>> propertyEditorFactory() {
        return propertyEditorFactory;
    }

    public final Callback<Item<?>, PropertyEditor<?>> getPropertyEditorFactory() {
        return this.propertyEditorFactory.get();
    }

    public final void setPropertyEditorFactory(Callback<Item<?>, PropertyEditor<?>> factory) {
        this.propertyEditorFactory.set(factory == null ? new CustomDefaultPropertyEditorFactory() : factory);
    }

    private final SimpleBooleanProperty modeSwitcherVisible = new SimpleBooleanProperty(this, "modeSwitcherVisible", true);

    public final SimpleBooleanProperty modeSwitcherVisibleProperty() {
        return modeSwitcherVisible;
    }

    public final boolean isModeSwitcherVisible() {
        return this.modeSwitcherVisible.get();
    }

    public final void setModeSwitcherVisible(boolean visible) {
        this.modeSwitcherVisible.set(visible);
    }

    private final SimpleBooleanProperty searchBoxVisible = new SimpleBooleanProperty(this, "searchBoxVisible", true);

    public final SimpleBooleanProperty searchBoxVisibleProperty() {
        return searchBoxVisible;
    }

    public final boolean isSearchBoxVisible() {
        return this.searchBoxVisible.get();
    }

    public final void setSearchBoxVisible(boolean visible) {
        this.searchBoxVisible.set(visible);
    }


    // --- titleFilterProperty
    private final SimpleStringProperty titleFilterProperty = new SimpleStringProperty(this, "titleFilter", "");

    public final SimpleStringProperty titleFilter() {
        return titleFilterProperty;
    }

    public final String getTitleFilter() {
        return this.titleFilterProperty.get();
    }

    public final void setTitleFilter(String filter) {
        this.titleFilterProperty.set(filter);
    }

    private final SimpleObjectProperty<Comparator<String>> categoryComparatorProperty = new SimpleObjectProperty<>(this, "categoryComparator",
                                                                                                                   null);

    public final SimpleObjectProperty<Comparator<String>> categoryComparatorProperty() {
        return categoryComparatorProperty;
    }

    public final Comparator<String> getCategoryComparator() {
        return this.categoryComparatorProperty.get();
    }

    public final void setCategoryComparator(Comparator<String> categoryComparator) {
        this.categoryComparatorProperty.set(categoryComparator);
    }

}
