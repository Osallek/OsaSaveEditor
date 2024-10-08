package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.clausewitzparser.common.ClausewitzUtils;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.time.LocalDate;

public class DateStringCellFactory implements Callback<ListView<LocalDate>, ListCell<LocalDate>> {

    @Override
    public ListCell<LocalDate> call(ListView<LocalDate> param) {
        return new ListCell<>() {

            @Override
            protected void updateItem(LocalDate value, boolean empty) {
                super.updateItem(value, empty);
                setText(value == null ? null : value.format(ClausewitzUtils.DATE_FORMAT));
            }
        };
    }
}
