package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet;
import javafx.collections.ObservableList;

public interface CustomItem<T> extends CustomPropertySheet.Item {

    ObservableList<T> getChoices();

    default int forceValueColSpan() {
        return 1;
    }
}
