package fr.osallek.osasaveeditor.controller.propertyeditor.item;

import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet;
import javafx.collections.ObservableList;

public interface CustomItem<U> extends CustomPropertySheet.Item<U> {

    default int forceValueColSpan() {
        return 1;
    }
}
