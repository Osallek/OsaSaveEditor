package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.Religion;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ReligionStringCellFactory implements Callback<ListView<Religion>, ListCell<Religion>> {

    public static final ReligionStringCellFactory INSTANCE = new ReligionStringCellFactory();

    @Override
    public ListCell<Religion> call(ListView<Religion> param) {
        return new ListCell<>() {

            @Override
            protected void updateItem(Religion value, boolean empty) {
                super.updateItem(value, empty);
                setText(value == null ? null : ReligionStringConverter.INSTANCE.toString(value));
            }
        };
    }
}
