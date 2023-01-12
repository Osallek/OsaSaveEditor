package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.save.Save;
import fr.osallek.eu4parser.model.save.gameplayoptions.Difficulty;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class DifficultyStringCellFactory implements Callback<ListView<Difficulty>, ListCell<Difficulty>> {

    private final Save save;

    public DifficultyStringCellFactory(Save save) {
        this.save = save;
    }

    @Override
    public ListCell<Difficulty> call(ListView<Difficulty> param) {
        return new ListCell<>() {

            @Override
            protected void updateItem(Difficulty value, boolean empty) {
                super.updateItem(value, empty);
                setText(value == null ? null : OsaSaveEditorUtils.localize(value.name(), save.getGame()));
            }
        };
    }
}
