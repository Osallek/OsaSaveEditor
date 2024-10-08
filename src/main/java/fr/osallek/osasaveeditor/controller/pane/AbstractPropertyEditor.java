/**
 * Copyright (c) 2013, ControlsFX All rights reserved.
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

import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet.Item;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import org.controlsfx.property.editor.PropertyEditor;

import java.util.Objects;

/**
 * An abstract implementation of the {@link PropertyEditor} interface.
 *
 * @param <T> The type of the property being edited.
 * @param <C> The type of Node that is used to edit this property.
 */
public abstract class AbstractPropertyEditor<T, C extends Node> implements PropertyEditor<T> {

    private final Item<T> property;
    private final C control;
    private boolean suspendUpdate;

    protected AbstractPropertyEditor(Item<T> property, C control) {
        this.control = control;
        this.control.managedProperty().bind(this.control.visibleProperty());
        this.property = property;

        getObservableValue().addListener((ObservableValue<? extends T> o, T oldValue, T newValue) -> {
            if (!this.suspendUpdate && !Objects.equals(oldValue, newValue)) {
                this.suspendUpdate = true;
                this.property.setValue(getValue());
                this.suspendUpdate = false;
            }
        });

        property.getObservableValue().ifPresent(v -> v.addListener((ObservableValue<? extends Object> o, Object oldValue, Object newValue) -> {
            if (!this.suspendUpdate && !Objects.equals(oldValue, newValue)) {
                this.suspendUpdate = true;
                setValue(property.getValue());
                this.suspendUpdate = false;
            }
        }));
    }

    protected abstract ObservableValue<T> getObservableValue();

    public final Item<T> getProperty() {
        return property;
    }

    @Override
    public C getEditor() {
        return control;
    }

    @Override
    public T getValue() {
        return getObservableValue().getValue();
    }
}
